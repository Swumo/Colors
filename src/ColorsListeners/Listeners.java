package ColorsListeners;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

import ColorsGUI.CustomColorGUI;
import ColorsMain.Colors;
import ColorsUtils.Utils;
import ResiListeners.ResListeners;
import net.md_5.bungee.api.ChatColor;

public class Listeners implements Listener{

	@SuppressWarnings("unused")
	private Colors plugin;
	
	public Listeners(Colors plugin){
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public static Color colorFromRGB(String rgb) {
		String[] split = rgb.split(" ");
		int rValue = Integer.valueOf(split[0]);
		int gValue = Integer.valueOf(split[1]);
		int bValue = Integer.valueOf(split[2]);
		float[] hsb = Color.RGBtoHSB(rValue, gValue, bValue, null);
		Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
		return c;
	}
	
	public static ChatColor chatColorFromRGB(String rgb) {
		String[] split = rgb.split(" ");
		int rValue = Integer.valueOf(split[0]);
		int gValue = Integer.valueOf(split[1]);
		int bValue = Integer.valueOf(split[2]);
		float[] hsb = Color.RGBtoHSB(rValue, gValue, bValue, null);
		Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
		ChatColor color = ChatColor.of(c);
		return color;
	}
  
	public static String convertStringArrayToString(String[] strArr) {
		StringBuilder sb = new StringBuilder();
		for (String str : strArr)
			sb.append(str);
		return sb.substring(0, sb.length());
	}
	
	public static String convertStringArrayToStringWithSpace(String[] strArr) {
		StringBuilder sb = new StringBuilder();
		for (String str : strArr)
			sb.append(str).append(" ");
		return sb.substring(0, sb.length());
	}
	
	public static String convertRGBToMessage(String rgb, String message) {
		String[] split = rgb.split(" ");
		int rValue = Integer.valueOf(split[0]);
		int gValue = Integer.valueOf(split[1]);
		int bValue = Integer.valueOf(split[2]);
		float[] hsb = Color.RGBtoHSB(rValue, gValue, bValue, null);
		Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
		ChatColor color = ChatColor.of(c);
		message = color + message;
		return message;
	}

	
	@EventHandler
	public static void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String colorCode = Colors.getPlayerColor(player);
		String message = event.getMessage();
		if(colorCode == null) {
			return;
		}
		if(ResListeners.promptedUser.get(player.getName()) == true || ResListeners.userInAreaSelection.get(player.getName()) == true) {
			return;
		}
		if(!colorCode.contains("&")) {
			if(colorCode.equals("rainbow")) {
				event.setCancelled(true);
				// Getting config
				FileConfiguration config = Colors.getInstance().getConfig();
				// Colour sequence supplied in the config
				String sequence = String.valueOf(config.get("rainbowColor"));
				// Way to keep track of the current colour in the sequence
				int currentChar = 0;
				// Message char array
				char[] chars = message.toCharArray();
				// Colour code char array
				char[] colorCodeChars = sequence.toCharArray();
				// A way to store modified characters with their specific colour codes
				String[] fragments = new String[chars.length];
				// Logic behind the rainbow chat
				for(int i = 0; i < chars.length; i++) {
					if(currentChar >= colorCodeChars.length) {
						currentChar = 0;
					}
					String s = String.valueOf(chars[i]);
					if(s.equals(" ")) {
						fragments[i] = " ";
						continue;
					}
					String color = String.valueOf(colorCodeChars[currentChar]);
					fragments[i] = "&" + color + s;
					currentChar++;
				}
				// Convert the string array to a single string
				String newMessage =	convertStringArrayToString(fragments);
				// Replace all the colour codes with actual colours
				newMessage = Utils.chat(newMessage);
				// Send non colorized message to console
				String toConsole = "<" + player.getName() + "> " + message;
				System.out.println(toConsole);
				// Send the colorized message through
				String finalMessage = "<" + player.getName() + "> " + newMessage;
				for(Player p : Bukkit.getServer().getOnlinePlayers()) {
					p.sendMessage(Utils.chat(finalMessage));
				}
				return;
			}
			if(colorCode.contains(";")) {
				event.setCancelled(true);
				// Getting the Color
				String[] two = colorCode.split(";");
				String[] start = two[0].split(" ");
				String[] end = two[1].split(" ");
				String startS = convertStringArrayToStringWithSpace(start);
				String endS = convertStringArrayToStringWithSpace(end);
				Color s = colorFromRGB(startS);
				Color e = colorFromRGB(endS);
				// Build the final message
				StringBuilder builder = new StringBuilder();
				List<ChatColor> colors = Utils.createGradient(s, e, message.length());
				char[] characters = message.toCharArray();
				for(int i = 0; i < message.length(); i++) {
					builder.append(colors.get(i)).append(characters[i]);
				}
				String finalMessage = "<" + player.getName() + "> " + builder.toString();
				String toConsole = "<" + player.getName() + "> " + message;
				for(Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(Utils.chat(finalMessage));
				}
				// Send message to console
				System.out.println(toConsole);
				return;
			}
			String newMessage = convertRGBToMessage(colorCode, message);
			event.setMessage(newMessage);
			return;
		}
		event.setCancelled(true);
		// Send non colorized message to console
		String toConsole = "<" + player.getName() + "> " + message;
		System.out.println(toConsole);
		// Send colored message
		message = Utils.chat(colorCode+message);
		String finalMessage = "<" + player.getName() + "> " + message;
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			p.sendMessage(Utils.chat(finalMessage));
		}
		return;
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String colorCode = Colors.getPlayerColor(player);
		if(!CustomColorGUI.playerRGBValues.containsKey(player.getName())) {
			if(colorCode == null) {
				return;
			}
			if(colorCode.contains(";")) return;
			if(!colorCode.contains("&") && !colorCode.contains("rainbow")) {
				String[] valuesAr = colorCode.split(" ");
				int rValue = Integer.valueOf(valuesAr[0]);
				int gValue = Integer.valueOf(valuesAr[1]);
				int bValue = Integer.valueOf(valuesAr[2]);
				List<Integer> vals = new ArrayList<Integer>(Arrays.asList(rValue, gValue, bValue));
				CustomColorGUI.playerRGBValues.put(player.getName(), vals);
			}
			else {
				List<Integer> vals = new ArrayList<Integer>(Arrays.asList(0,0,0));
				CustomColorGUI.playerRGBValues.put(player.getName(), vals);		
			}
		}
		Inventory inv = CustomColorGUI.GUI(player);
		CustomColorGUI.playerInvs.put(player.getName(), inv);
		if(colorCode == null) {
			return;
		}
		else {
			return;
		}
	}
	
	
	
	@EventHandler
	private void onGUIClick(InventoryClickEvent E) {
		String title = E.getView().getTitle();
		if(title.equals(CustomColorGUI.gui_name)) {
			if(E.getCurrentItem() == null) {
				return;
			}
			if(E.getCurrentItem().getType() == Material.AIR ) {
				return;
			}
			if(E.getCurrentItem().getType() == Material.FISHING_ROD) {
				return;
			}
			if(title.equals(CustomColorGUI.gui_name)) {
				E.setCancelled(true);
				CustomColorGUI.clicked((Player) E.getWhoClicked(), E.getSlot(), E.getCurrentItem(), E.getInventory());
				return;
			}
		}
	}
}
