package ColorsGUI;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ColorsMain.Colors;
import ColorsUtils.Utils;
import net.md_5.bungee.api.ChatColor;

public class CustomColorGUI {
	public static Inventory inv;
	public static String gui_name;
	public static int rows = 6 * 9;

	public static void initialize() {
		gui_name = Utils.chat("&cColor &eCustomizer");
		inv = Bukkit.createInventory(null, rows);
	}
	
	public static HashMap<String, List<Integer>> playerRGBValues = new HashMap<String, List<Integer>>();
	public static HashMap<String, Inventory> playerInvs = new HashMap<String, Inventory>();
	
	
	public static void copyRGBToClipboard(String rgbValues) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		StringSelection strSel = new StringSelection(rgbValues);
		clipboard.setContents(strSel, null);
	}
	
	public static void updateInventory(final Player player) {
    	Inventory inv = player.getOpenInventory().getTopInventory();
    	if(inv != null) {
    		ItemStack rValue = inv.getItem(4);
    		ItemStack gValue = inv.getItem(13);
    		ItemStack bValue = inv.getItem(22);
    		if(rValue != null && gValue != null && bValue != null) {
    			if(rValue.getType() == Material.RED_STAINED_GLASS_PANE && gValue.getType() == Material.GREEN_STAINED_GLASS_PANE && bValue.getType() == Material.BLUE_STAINED_GLASS_PANE) {
    	    		Inventory toReturn = Bukkit.createInventory(null, rows, gui_name);
		    		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
		    		for(int i = 0; i < rows; i++) {
		    			if(inv.getItem(i) == null) {
		    				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l ");
		    			}
		    			else continue;
		    		}
		    		int rVal = playerRGBValues.get(player.getName()).get(0);
		    		int gVal = playerRGBValues.get(player.getName()).get(1);
		    		int bVal = playerRGBValues.get(player.getName()).get(2);
		    		Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 5, "&cRed", "&7Value: &e"+rVal);
		    		Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 14, "&aGreen", "&7Value: &e"+gVal);
		    		Utils.createItem(inv, Material.BLUE_STAINED_GLASS_PANE, 1, 23, "&bBlue", "&7Value: &e"+bVal);
		    		if(rVal > 0 || gVal > 0 || bVal > 0) {
			    		//PREVIEW
			    		int slot = 41;
			    		String message = "Hello!";
			    		float[] hsb = Color.RGBtoHSB(rVal, gVal, bVal, null);
			    		Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
			    		ChatColor color = ChatColor.of(c);
			    		message = color + message;
						Utils.createItem(inv, Material.OAK_SIGN, 1, slot, "&f&lPreview", message, " ", "&7&oClick here to copy current RGB values!");	
		    		}
		    		else {
		    			Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, 41, " &l ");
		    		}
		    		toReturn.setContents(inv.getContents());
	    		}
    		}
    	}
	}
	
	public static Inventory GUI(Player P) {
		Inventory toReturn = Bukkit.createInventory(null, rows, gui_name);
		int rValue = 0,gValue = 0,bValue = 0;
		String colorCode = Colors.getPlayerColor(P);
		if(!playerRGBValues.containsKey(P.getName())) {
			if(colorCode == null) {
				List<Integer> vals = new ArrayList<Integer>(Arrays.asList(0,0,0));
				CustomColorGUI.playerRGBValues.put(P.getName(), vals);
			}
			if(!colorCode.contains("&") && !colorCode.contains("rainbow")) {
				String[] valuesAr = colorCode.split(" ");
				rValue = Integer.valueOf(valuesAr[0]);
				gValue = Integer.valueOf(valuesAr[1]);
				bValue = Integer.valueOf(valuesAr[2]);
				List<Integer> vals = new ArrayList<Integer>(Arrays.asList(rValue, gValue, bValue));
				CustomColorGUI.playerRGBValues.put(P.getName(), vals);
			}
			else {
				List<Integer> vals = new ArrayList<Integer>(Arrays.asList(0,0,0));
				CustomColorGUI.playerRGBValues.put(P.getName(), vals);		
			}
		}
		else {
			rValue = playerRGBValues.get(P.getName()).get(0);
			gValue = playerRGBValues.get(P.getName()).get(1);
			bValue = playerRGBValues.get(P.getName()).get(2);
		}
		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
		for(int i = 0; i < rows; i++) {
			if(inv.getItem(i) == null) {
				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
			}
			else continue;
		}
		Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 5, "&cRed", "&7Value: &e"+rValue);
		Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 14, "&aGreen", "&7Value: &e"+gValue);
		Utils.createItem(inv, Material.BLUE_STAINED_GLASS_PANE, 1, 23, "&bBlue", "&7Value: &e"+bValue);
		Utils.createItem(inv, Material.RED_STAINED_GLASS, 1, 1, "&e-100");
		Utils.createItem(inv, Material.RED_STAINED_GLASS, 1, 2, "&e-10");
		Utils.createItem(inv, Material.RED_STAINED_GLASS, 1, 3, "&e-1");
		Utils.createItem(inv, Material.RED_STAINED_GLASS, 1, 9, "&e+100");
		Utils.createItem(inv, Material.RED_STAINED_GLASS, 1, 8, "&e+10");
		Utils.createItem(inv, Material.RED_STAINED_GLASS, 1, 7, "&e+1");
		Utils.createItem(inv, Material.GREEN_STAINED_GLASS, 1, 10, "&e-100");
		Utils.createItem(inv, Material.GREEN_STAINED_GLASS, 1, 11, "&e-10");
		Utils.createItem(inv, Material.GREEN_STAINED_GLASS, 1, 12, "&e-1");
		Utils.createItem(inv, Material.GREEN_STAINED_GLASS, 1, 18, "&e+100");
		Utils.createItem(inv, Material.GREEN_STAINED_GLASS, 1, 17, "&e+10");
		Utils.createItem(inv, Material.GREEN_STAINED_GLASS, 1, 16, "&e+1");
		Utils.createItem(inv, Material.BLUE_STAINED_GLASS, 1, 19, "&e-100");
		Utils.createItem(inv, Material.BLUE_STAINED_GLASS, 1, 20, "&e-10");
		Utils.createItem(inv, Material.BLUE_STAINED_GLASS, 1, 21, "&e-1");
		Utils.createItem(inv, Material.BLUE_STAINED_GLASS, 1, 27, "&e+100");
		Utils.createItem(inv, Material.BLUE_STAINED_GLASS, 1, 26, "&e+10");
		Utils.createItem(inv, Material.BLUE_STAINED_GLASS, 1, 25, "&e+1");
		
		//PREVIEW
		if(rValue > 0 || gValue > 0 || bValue > 0) {
			int slot = 41;
			String message = "Hello!";
			float[] hsb = Color.RGBtoHSB(rValue, gValue, bValue, null);
			Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
			ChatColor color = ChatColor.of(c);
			message = color + message;
			Utils.createItem(inv, Material.OAK_SIGN, 1, slot, "&f&lPreview", message, " ", "&7&oClick here to copy current RGB values!");	
		}
		else {
			Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, 41, " &l ");	
		}
		
		Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 38, "&c&lClose");
		Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 44, "&a&lConfirm");
		
		toReturn.setContents(inv.getContents());
		return toReturn;
	}
	
	public static void openInv(Player player) {
		if(!CustomColorGUI.playerRGBValues.containsKey(player.getName())) {
			String colorCode = Colors.getPlayerColor(player);
			if(colorCode == null) {
				List<Integer> vals = new ArrayList<Integer>(Arrays.asList(0,0,0));
				CustomColorGUI.playerRGBValues.put(player.getName(), vals);
				Inventory inv = GUI(player);
				playerInvs.put(player.getName(), inv);
				player.openInventory(inv);
				return;
			}
			if(!colorCode.contains("&") && !colorCode.contains("rainbow")) {
				String[] valuesAr = colorCode.split(" ");
				try {
					int rValue = Integer.valueOf(valuesAr[0]);
					int gValue = Integer.valueOf(valuesAr[1]);
					int bValue = Integer.valueOf(valuesAr[2]);
					List<Integer> vals = Arrays.asList(rValue, gValue, bValue);
					CustomColorGUI.playerRGBValues.put(player.getName(), vals);
					Inventory inv = GUI(player);
					playerInvs.put(player.getName(), inv);
					player.openInventory(inv);
					return;	
				}
				catch(Exception e) {
					List<Integer> vals = Arrays.asList(0,0,0);
					CustomColorGUI.playerRGBValues.put(player.getName(), vals);
					Inventory inv = GUI(player);
					playerInvs.put(player.getName(), inv);
					player.openInventory(inv);
					return;	
				}
			}
		}
		Inventory inv = GUI(player);
		playerInvs.put(player.getName(), inv);
		player.openInventory(inv);
		return;
	}
	
	public static void clicked(Player P, int slot, ItemStack clicked, Inventory inv) {
		FileConfiguration pluginConfig = Colors.getInstance().getConfig();
		String pluginPrefix = String.valueOf(pluginConfig.get("pluginPrefix"));
		if(!clicked.getItemMeta().getDisplayName().contains(Utils.chat(" &l  &l "))) { 
			updateInventory(P);
		}
		if(clicked.getItemMeta().getDisplayName().equals(Utils.chat("&c&lClose"))) {
			P.closeInventory();
			return;
		}
		if(clicked.getItemMeta().getDisplayName().equals(Utils.chat("&f&lPreview"))) {
			P.closeInventory();
			int rValue = playerRGBValues.get(P.getName()).get(0);
			int gValue = playerRGBValues.get(P.getName()).get(1);
			int bValue = playerRGBValues.get(P.getName()).get(2);
			String rgbValues = String.valueOf(rValue) + " " + String.valueOf(gValue) + " " + String.valueOf(bValue);
			copyRGBToClipboard(rgbValues);
			P.sendMessage(Utils.chat(pluginPrefix + " &aSuccessfully copied!"));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().equals(Utils.chat("&a&lConfirm"))) {
			int rValue = playerRGBValues.get(P.getName()).get(0);
			int gValue = playerRGBValues.get(P.getName()).get(1);
			int bValue = playerRGBValues.get(P.getName()).get(2);	
			if(rValue < 100 || gValue < 100 || bValue < 100) {
				if(rValue >= 100 || gValue >= 100 || bValue >= 100) {
					String color = rValue + " " + gValue + " " + bValue;
					Colors.savePlayerColor(P, color);
					P.sendMessage(Utils.chat(pluginPrefix + " &aYour new color has been set!"));
					P.closeInventory();
					return;
				}
				else {
					P.sendMessage(Utils.chat(pluginPrefix + " &cOne of the values has to be at least 100 to make your color readable!"));
					return;	
				}
			}
			String color = rValue + " " + gValue + " " + bValue;
			Colors.savePlayerColor(P, color);
			P.sendMessage(Utils.chat(pluginPrefix + " &aYour new color has been set!"));
			P.closeInventory();
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.chat("+"))) {
			Material type = clicked.getType();
			if(type == Material.RED_STAINED_GLASS) {
				int toAdd = Integer.valueOf(clicked.getItemMeta().getDisplayName().substring(3));
				int currR = playerRGBValues.get(P.getName()).get(0);
				if(currR == 255) {
					P.sendMessage(Utils.chat(pluginPrefix + " &cValue cannot go over 255!"));
					return;
				}
				int currG = playerRGBValues.get(P.getName()).get(1);
				int currB = playerRGBValues.get(P.getName()).get(2);
				currR = currR + toAdd;
				if(currR > 255) {
					toAdd = 255 - currR;
					currR = currR + toAdd;
					List<Integer> newVal = new ArrayList<Integer>(Arrays.asList(currR, currG, currB));
					playerRGBValues.put(P.getName(), newVal);
					updateInventory(P);
					return;
				}
				List<Integer> newVal = new ArrayList<Integer>(Arrays.asList(currR, currG, currB));
				playerRGBValues.put(P.getName(), newVal);
				updateInventory(P);
			}
			if(type == Material.GREEN_STAINED_GLASS) {
				int toAdd = Integer.valueOf(clicked.getItemMeta().getDisplayName().substring(3));
				int currR = playerRGBValues.get(P.getName()).get(0);
				int currG = playerRGBValues.get(P.getName()).get(1);
				if(currG == 255) {
					P.sendMessage(Utils.chat(pluginPrefix + " &cValue cannot go over 255!"));
					return;
				}
				int currB = playerRGBValues.get(P.getName()).get(2);
				currG = currG + toAdd;
				if(currG > 255) {
					toAdd = 255 - currG;
					currG = currG + toAdd;
					List<Integer> newVal = new ArrayList<Integer>(Arrays.asList(currR, currG, currB));
					playerRGBValues.put(P.getName(), newVal);
					updateInventory(P);
					return;
				}
				List<Integer> newVal = new ArrayList<Integer>(Arrays.asList(currR, currG, currB));
				playerRGBValues.put(P.getName(), newVal);
				updateInventory(P);
				return;
			}
			if(type == Material.BLUE_STAINED_GLASS) {
				int toAdd = Integer.valueOf(clicked.getItemMeta().getDisplayName().substring(3));
				int currR = playerRGBValues.get(P.getName()).get(0);
				int currG = playerRGBValues.get(P.getName()).get(1);
				int currB = playerRGBValues.get(P.getName()).get(2);
				if(currB == 255) {
					P.sendMessage(Utils.chat(pluginPrefix + " &cValue cannot go over 255!"));
					return;
				}
				currB = currB + toAdd;
				if(currB > 255) {
					toAdd = 255 - currB;
					currB = currB + toAdd;
					List<Integer> newVal = new ArrayList<Integer>(Arrays.asList(currR, currG, currB));
					playerRGBValues.put(P.getName(), newVal);
					updateInventory(P);
					return;
				}
				List<Integer> newVal = new ArrayList<Integer>(Arrays.asList(currR, currG, currB));
				playerRGBValues.put(P.getName(), newVal);
				updateInventory(P);
				return;
			}
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.chat("-"))) {
			Material type = clicked.getType();
			if(type == Material.RED_STAINED_GLASS) {
				int toRemove = Integer.valueOf(clicked.getItemMeta().getDisplayName().substring(3));
				int currR = playerRGBValues.get(P.getName()).get(0);
				if(currR == 0) {
					P.sendMessage(Utils.chat(pluginPrefix + " &cValue cannot be less than 0!"));
					return;
				}
				int currG = playerRGBValues.get(P.getName()).get(1);
				int currB = playerRGBValues.get(P.getName()).get(2);
				currR = currR - toRemove;
				if(currR < 0) {
					toRemove = currR;
					int newcurrR = currR - toRemove;
					List<Integer> newVal = new ArrayList<Integer>(Arrays.asList(newcurrR, currG, currB));
					playerRGBValues.put(P.getName(), newVal);
					updateInventory(P);
					return;
				}
				List<Integer> newVal = new ArrayList<Integer>(Arrays.asList(currR, currG, currB));
				playerRGBValues.put(P.getName(), newVal);
				updateInventory(P);
			}
			if(type == Material.GREEN_STAINED_GLASS) {
				int toRemove = Integer.valueOf(clicked.getItemMeta().getDisplayName().substring(3));
				int currR = playerRGBValues.get(P.getName()).get(0);
				int currG = playerRGBValues.get(P.getName()).get(1);
				if(currG == 0) {
					P.sendMessage(Utils.chat(pluginPrefix + " &cValue cannot be less than 0!"));
					return;
				}
				int currB = playerRGBValues.get(P.getName()).get(2);
				currG = currG - toRemove;
				if(currG < 0) {
					toRemove = currG;
					int newcurrG = currG - toRemove;
					List<Integer> newVal = new ArrayList<Integer>(Arrays.asList(currR, newcurrG, currB));
					playerRGBValues.put(P.getName(), newVal);
					updateInventory(P);
					return;
				}
				List<Integer> newVal = new ArrayList<Integer>(Arrays.asList(currR, currG, currB));
				playerRGBValues.put(P.getName(), newVal);
				updateInventory(P);
			}
			if(type == Material.BLUE_STAINED_GLASS) {
				int toRemove = Integer.valueOf(clicked.getItemMeta().getDisplayName().substring(3));
				int currR = playerRGBValues.get(P.getName()).get(0);
				int currG = playerRGBValues.get(P.getName()).get(1);
				int currB = playerRGBValues.get(P.getName()).get(2);
				if(currB == 0) {
					P.sendMessage(Utils.chat(pluginPrefix + " &cValue cannot be less than 0!"));
					return;
				}
				currB = currB - toRemove;
				if(currB < 0) {
					toRemove = currB;
					int newcurrB = currB - toRemove;
					List<Integer> newVal = new ArrayList<Integer>(Arrays.asList(currR, currG, newcurrB));
					playerRGBValues.put(P.getName(), newVal);
					updateInventory(P);
					return;
				}
				List<Integer> newVal = new ArrayList<Integer>(Arrays.asList(currR, currG, currB));
				playerRGBValues.put(P.getName(), newVal);
				updateInventory(P);
			}
		}
	 }
}
