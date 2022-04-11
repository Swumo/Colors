package ColorsCommands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ColorsGUI.CustomColorGUI;
import ColorsListeners.Listeners;
import ColorsMain.Colors;
import ColorsUtils.Utils;
import net.md_5.bungee.api.ChatColor;

public class Commands implements TabExecutor{
	private Colors plugin;
	
	public Commands(Colors plugin) {
		this.plugin = plugin;
		
		plugin.getCommand("color").setExecutor(this);
	}
	
	
	private static List<String> allColors(){
		List<String> allColors = new ArrayList<String>();
		for(int i = 0; i < colors.size(); i++) {
			allColors.add(colors.get(i));
		}
		return allColors;
	}
	private static List<String> playerColors(Player player){
		List<String> userColors = new ArrayList<String>();
		for(int i = 0; i < colors.size(); i++) {
			if(player.hasPermission("ChatColor.Color."+colors.get(i))) {
				String color = colors.get(i);
				color = spaceOut(color);
				userColors.add(color.substring(0,1).toUpperCase() + color.substring(1));
			}
		}
		if(player.hasPermission("ChatColor.Color.Rainbow")) {
			userColors.add("Rainbow");
		}
		return userColors;
	}
	private static List<String> playerColorsWithColors(Player player){

		List<String> userColorsWithColors = new ArrayList<String>();
		for(int i = 0; i < colors.size(); i++) {
			if(player.hasPermission("ChatColor.Color."+colors.get(i))) {
				String colorCode = colorCodes[i];
				String color = colors.get(i);
				color = spaceOut(color);
				userColorsWithColors.add(colorCode + color.substring(0,1).toUpperCase() + color.substring(1));
			}
		}
		if(player.hasPermission("ChatColor.Color.Rainbow")) {
			userColorsWithColors.add("&cR&6a&ei&an&bb&9o&dw");
		}
		return userColorsWithColors;
	}
	
	public static String spaceOut(String color) {
		if(color.equalsIgnoreCase("darkred")) {
			return "dark red";
		}
		if(color.equalsIgnoreCase("darkblue")) {
			return "dark blue";
		}
		if(color.equalsIgnoreCase("darkgreen")) {
			return "dark green";
		}
		if(color.equalsIgnoreCase("darkgray")) {
			return "dark gray";
		}
		if(color.equalsIgnoreCase("lightgray")) {
			return "light gray";
		}
		return color;
	}
	
	private static String[] colorString = {"green","aqua","red","pink","yellow","white","darkblue","darkgreen","cyan","darkred","purple","gold","lightgray","darkgray","blue"};
	private static List<String> colors = Arrays.asList(colorString);
	private static String[] colorCodes = {"&a","&b","&c","&d","&e","&f","&1","&2","&3","&4","&5","&6","&7","&8","&9"};
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		FileConfiguration pluginConfig = Colors.getInstance().getConfig();
		String pluginPrefix = String.valueOf(pluginConfig.get("pluginPrefix"));
		
		if(!(sender instanceof Player)) {
			if(cmd.getName().equals("color")) {
				if(args[0].equalsIgnoreCase("reload")) {
					plugin.reloadConfig();
					plugin.saveConfig();
					sender.sendMessage(Utils.chat(pluginPrefix + " &aPlugin successfully reloaded!"));
					return true;
				}
				if(args[0].equalsIgnoreCase("setcolor")) {
					if(args.length < 2) {
						sender.sendMessage(Utils.chat(pluginPrefix +" &cPlease specify a color!"));
						return false;
					}
					if(args.length < 3) {
						sender.sendMessage(Utils.chat(pluginPrefix +" &cPlease specify a player!"));
						return false;
					}
					String color = args[1].toLowerCase();
					Player player = Bukkit.getPlayer(args[2]);
					if(player == null) {
						sender.sendMessage(Utils.chat(pluginPrefix + " &cPlayer is not online!"));
						return false;
					}
					String capColor = color.substring(0,1).toUpperCase() + color.substring(1);
					if(colors.contains(color)) {
						int place = colors.indexOf(color);
						capColor = spaceOut(capColor);
						String colorCode = colorCodes[place];
						Colors.savePlayerColor(player, colorCode);
						player.sendMessage(Utils.chat(pluginPrefix + " &eYour color has been set to " + colorCode + capColor + "&e!"));
						return true;
					}
					else {
						sender.sendMessage(Utils.chat(pluginPrefix + " &cColor is not supported or does not exist!"));
						return false;
					}	
				}
			}
			return true;
		}
		
		Player p = (Player) sender;
		if(args.length < 1) {
			p.sendMessage(Utils.chat("&7&m---------------&cC&6h&ea&at&bC&9o&dl&5o&cr&7&m----------------"));
			p.sendMessage(Utils.chat("&f/color set <color> &e- &7Set your chat color!"));
			p.sendMessage(Utils.chat("&f/color list &e- &7View available colors!"));
			p.sendMessage(Utils.chat("&f/color reset &e- &7Reset your chat color!"));
			p.sendMessage(Utils.chat("&f/color reload &e- &7Reload the plugin!"));
			p.sendMessage(Utils.chat("&f/color customize &e- &7Open the customization GUI!"));
			p.sendMessage(Utils.chat("&7&m----------------------------------------"));
			return true;
		}
		
		
		if(args[0].equalsIgnoreCase("customize")) {
			CustomColorGUI.openInv(p);
			return true;
		}
		
		
		if(args[0].equalsIgnoreCase("reload")) {
			plugin.reloadConfig();
			plugin.saveConfig();
			p.sendMessage(Utils.chat(pluginPrefix + " &aPlugin successfully reloaded!"));
			return true;
		}
		
		
		if(args[0].equalsIgnoreCase("list")) {
			p.sendMessage(Utils.chat(pluginPrefix +" &7All available colors: \n&7&m---------------------------------\n&aGreen, &bAqua, &cRed, &dPink, &eYellow, &fWhite, &1Dark Blue, &2Dark Green, &3Cyan, &4Dark Red, &5Purple, &6Gold, &7Light Gray, &8Dark Gray, &9Blue\n&7&m---------------------------------"));
			List<String> userColors = playerColorsWithColors(p);
			if(!userColors.isEmpty()) {
				if(userColors.size() - 1 >= 15) {
					p.sendMessage(Utils.chat(pluginPrefix + " &aYour unlocked colors: \n&7&m---------------------------------&r\n &eYou have all the colors! \n&7&m---------------------------------\n"));
					return true;
				}
				p.sendMessage(Utils.chat(pluginPrefix + " &aYour unlocked colors: \n&7&m---------------------------------\n" + String.join(", ", userColors) + "\n&7&m---------------------------------\n"));
				return true;
			}
			else {
				p.sendMessage(Utils.chat(pluginPrefix + " &aYour unlocked colors: \n&cNone"));
				return true;	
			}
		}
		
		if(args[0].equalsIgnoreCase("reset")) {
			if(args.length <= 1) {
				String colorCode = String.valueOf(pluginConfig.get("defaultColor"));
				Colors.savePlayerColor(p, colorCode);
				p.sendMessage(Utils.chat(pluginPrefix +" &eYour chat color has been reset!"));
				return true;	
			}
			if(args.length > 1) {
				if(p.isOp()) {
					Player getPlayer = Bukkit.getPlayer(args[1]);
					String colorCode = String.valueOf(pluginConfig.get("defaultColor"));
					Colors.savePlayerColor(getPlayer, colorCode);
					getPlayer.sendMessage(Utils.chat(pluginPrefix + " &a" + p.getName() + " &ehas reset your chat color!"));
					return true;	
				}
				else {
					p.sendMessage(Utils.chat(pluginPrefix + " &cYou are not allowed to do this!"));
				}	
			}
		}
		
		if(args[0].equalsIgnoreCase("setcolor")) {
			if(args.length < 2) {
				p.sendMessage(Utils.chat(pluginPrefix +" &cPlease specify a color!"));
				return false;
			}
			if(args.length < 3) {
				p.sendMessage(Utils.chat(pluginPrefix +" &cPlease specify a player!"));
				return false;
			}
			String color = args[1].toLowerCase();
			Player player = Bukkit.getPlayer(args[2]);
			String capColor = color.substring(0,1).toUpperCase() + color.substring(1);
			Player commandRunner = (Player) sender;
			if(color.equalsIgnoreCase("rainbow")) {
				String rainbow = "rainbow";
				Colors.savePlayerColor(player, rainbow);
				p.sendMessage(Utils.chat(pluginPrefix +" &eYou set &a"+player.getName() +"'s &echat color to &cR&6a&ei&an&bb&9o&dw"));
				player.sendMessage(Utils.chat(pluginPrefix +" &eYour chat color has been set to &cR&6a&ei&an&bb&9o&dw"));
				return true;	
			}
			if(colors.contains(color)) {
				int place = colors.indexOf(color);
				capColor = spaceOut(capColor);
				String colorCode = colorCodes[place];
				Colors.savePlayerColor(player, colorCode);
				p.sendMessage(Utils.chat(pluginPrefix +" &eYou set &a"+player.getName() +"'s &echat color to " +colorCode + capColor +"&e!"));
				player.sendMessage(Utils.chat(pluginPrefix + " &a" + commandRunner.getName() + " &eset &eyour chat color to " + colorCode + capColor + "&e!"));
				return true;
			}
			else {
				p.sendMessage(Utils.chat(pluginPrefix + " &cColor is not supported or does not exist!"));
				return false;
			}	
		}
		
		if(args[0].equalsIgnoreCase("set")) {
			if(args.length < 2) {
				p.sendMessage(Utils.chat("&cPlease specify a color!"));
				return false;
			}
			
			if(args.length > 2 && args.length == 4) {
				String rVal, gVal, bVal = null;
				int rValue, gValue, bValue = 0;
				if(args[1].contains(",") || args[2].contains(",") || args[3].contains(",")) {
					rVal = args[1].replace(",", "");
					gVal = args[2].replace(",", "");
					bVal = args[3].replace(",", "");
					rValue = Integer.valueOf(rVal);
					gValue = Integer.valueOf(gVal);
					bValue = Integer.valueOf(bVal);
				}
				else {
					rValue = Integer.valueOf(args[1]);
					gValue = Integer.valueOf(args[2]);
					bValue = Integer.valueOf(args[3]);
				}
				if(rValue > 255 || gValue > 255 || bValue > 255) {
					p.sendMessage(Utils.chat(pluginPrefix + " &cRGB values cannot exceed 255!"));
					return false;
				}
				if(rValue < 100 || gValue < 100 || bValue < 100) {
					if(rValue >= 100 || gValue >= 100 || bValue >= 100) {
						String color = rValue + " " + gValue + " " + bValue;
						Colors.savePlayerColor(p, color);
						CustomColorGUI.playerRGBValues.put(p.getName(), Arrays.asList(rValue, gValue, bValue));
						p.sendMessage(Utils.chat(pluginPrefix + " &aYour new color has been set!"));
						return true;
					}
					else {
						p.sendMessage(Utils.chat(pluginPrefix + " &cOne of the values has to be at least 100 to make your color readable!"));
						return false;	
					}
				}
				String color = rValue + " " + gValue + " " + bValue;
				Colors.savePlayerColor(p, color);
				CustomColorGUI.playerRGBValues.put(p.getName(), Arrays.asList(rValue, gValue, bValue));
				p.sendMessage(Utils.chat(pluginPrefix + " &aYour new color has been set!"));
				return true;
			}
			
			if(args.length > 2 && args.length <= 4) {
				String color = args[1].toLowerCase() + args[2].toLowerCase();
				String perm = args[1] + args[2];
				if(p.hasPermission("ChatColor.Color."+perm) || p.hasPermission("ChatColor.Color.*")) {
					if(colors.contains(color)) {
						String capColor = color.substring(0,1).toUpperCase() + color.substring(1);
						int place = colors.indexOf(color);
						String colorCode = colorCodes[place];
						Colors.savePlayerColor(p, colorCode);
						p.sendMessage(Utils.chat(pluginPrefix +" &eYour chat color has been set to " + colorCode + capColor + "&e!"));
						return true;
					}
					else {
						p.sendMessage(Utils.chat(pluginPrefix + " &cColor is not supported or does not exist!"));
						return false;
					}
				}
				else {
					p.sendMessage(Utils.chat(pluginPrefix + " &cYou do not have permission to use this color!"));
					return false;	
				}
			}
			
			
			String color = args[1].toLowerCase();
			if(color.equalsIgnoreCase("rainbow")) {
				if(p.hasPermission("ChatColor.Color.Rainbow")) {
					String rainbow = "rainbow";
					Colors.savePlayerColor(p, rainbow);
					p.sendMessage(Utils.chat(pluginPrefix +" &eYour chat color has been set to &cR&6a&ei&an&bb&9o&dw"));
					return true;	
				}
				else {
					p.sendMessage(Utils.chat(pluginPrefix +" &cYou do not have permission!"));
					return false;
				}
			}
			String capColor = color.substring(0,1).toUpperCase() + color.substring(1);
			if(p.hasPermission("ChatColor.Color." + capColor) || p.hasPermission("ChatColor.Color.*")) {
				if(colors.contains(color)) {
					int place = colors.indexOf(color);
					capColor = spaceOut(capColor);
					String colorCode = colorCodes[place];
					Colors.savePlayerColor(p, colorCode);
					p.sendMessage(Utils.chat(pluginPrefix +" &eYour chat color has been set to " + colorCode + capColor + "&e!"));
					return true;
				}
				else {
					p.sendMessage(Utils.chat(pluginPrefix +" &cColor is not supported or does not exist!"));
					return false;
				}	
			}
			else {
				p.sendMessage(Utils.chat(pluginPrefix +" &cYou do not have permission to use this color!"));
				return false;
			}
		}
		
		
		if(args[0].equalsIgnoreCase("setgradient")) {
			if(args.length < 2) {
				p.sendMessage(Utils.chat(pluginPrefix +" &eGradient help:"));
				p.sendMessage(Utils.chat("&a/color setgradient <starting color> <end color>"));
				p.sendMessage(Utils.chat("&l &l &l &l &l &l &l &l &l &l &l &l &l &eExamples"));
				p.sendMessage(Utils.chat("&fRGB codes:"));
				p.sendMessage(Utils.chat("&7/color setgradient 255, 100, 10 200, 23, 40"));
				p.sendMessage(Utils.chat("&fHex codes:"));
				p.sendMessage(Utils.chat("&7/color setgradient #00FFFF #66FF00"));
				p.sendMessage(Utils.chat("&fMinecraft codes:"));
				p.sendMessage(Utils.chat("&7/color setgradient") + Utils.chat("&7") + " &e &c");
				p.sendMessage(Utils.chat("&cNote: If you are using Hex codes, please include the #!"));
				return false;	
			}
			if(args.length >= 2 && args.length < 3) {
				p.sendMessage(Utils.chat(pluginPrefix +" &cPlease specify the ending color!"));
				return false;
			}
			if(args.length > 3 && args.length == 7) {
				String[] codes = new String[6];
				if(args[1].contains(";")) {
					for(int i = 1; i < args.length; i++) {
						if(i == 3) {
							codes[i-1] = args[i].replace(";", "") + ";";
							continue;
						}
						codes[i-1] = args[i].replace(";", "") + " ";
					}
				}
				else {
					for(int i = 1; i < args.length; i++) {
						if(i == 3) {
							codes[i-1] = args[i].replace(",", "") + ";";
							continue;
						}
						codes[i-1] = args[i].replace(",", "") + " ";
					}	
				}
				String a = Listeners.convertStringArrayToString(codes);
				String startCodes = a.split(";")[0].toString();
				String endCodes = a.split(";")[1].toString();
				Color start = Listeners.colorFromRGB(startCodes);
				Color end = Listeners.colorFromRGB(endCodes);
				Colors.savePlayerColor(p, start.getRed() + " " + start.getGreen() + " " + start.getBlue() + ";" + end.getRed() + " " + end.getGreen() + " " + end.getBlue());
				p.sendMessage(Utils.chat(pluginPrefix +" &aYour new gradient color has been set!"));
				return true;
			}
			String regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
			Pattern pattern = Pattern.compile(regex);
			String arg1 = args[1];
			String arg2 = args[2];
			Matcher m1 = pattern.matcher(arg1);
			Matcher m2 = pattern.matcher(arg2);
			if(m1.matches() && m2.matches()) {
				Color start = Utils.hex2Rgb(arg1);
				Color end = Utils.hex2Rgb(arg2);
				Colors.savePlayerColor(p, start.getRed() + " " + start.getGreen() + " " + start.getBlue() + ";" + end.getRed() + " " + end.getGreen() + " " + end.getBlue());
				p.sendMessage(Utils.chat(pluginPrefix +" &aYour new gradient color has been set!"));
				return true;
			}
			ChatColor start = null;
			ChatColor end = null;
			if(arg1.contains("&") || arg2.contains("&")) {
				arg1 = arg1.replace("&", "");
				arg2 = arg2.replace("&", "");
				start = ChatColor.getByChar(arg1.charAt(0));
				end = ChatColor.getByChar(arg2.charAt(0));
				Color startColor = start.getColor();
				Color endColor = end.getColor();
				Colors.savePlayerColor(p, startColor.getRed() + " " + startColor.getGreen() + " " + startColor.getBlue() + ";" + endColor.getRed() + " " + endColor.getGreen() + " " + endColor.getBlue());
				p.sendMessage(Utils.chat(pluginPrefix +" &aYour new gradient color has been set!"));
				return true;
			}
			if(arg1.contains(",") || arg2.contains(",")) {
				String[] startArr = arg1.split(",");
				String[] endArr = arg2.split(",");
				String startS = Listeners.convertStringArrayToStringWithSpace(startArr);
				String endS = Listeners.convertStringArrayToStringWithSpace(endArr);
				start = Listeners.chatColorFromRGB(startS);
				end = Listeners.chatColorFromRGB(endS);
				Color startColor = start.getColor();
				Color endColor = end.getColor();
				Colors.savePlayerColor(p, startColor.getRed() + " " + startColor.getGreen() + " " + startColor.getBlue() + ";" + endColor.getRed() + " " + endColor.getGreen() + " " + endColor.getBlue());
				p.sendMessage(Utils.chat(pluginPrefix +" &aYour new gradient color has been set!"));
				return true;
			}
			if(arg1.contains(";") || arg2.contains(";")) {
				String[] startArr = arg1.split(";");
				String[] endArr = arg2.split(";");
				String startS = Listeners.convertStringArrayToStringWithSpace(startArr);
				String endS = Listeners.convertStringArrayToStringWithSpace(endArr);
				start = Listeners.chatColorFromRGB(startS);
				end = Listeners.chatColorFromRGB(endS);
				Color startColor = start.getColor();
				Color endColor = end.getColor();
				Colors.savePlayerColor(p, startColor.getRed() + " " + startColor.getGreen() + " " + startColor.getBlue() + ";" + endColor.getRed() + " " + endColor.getGreen() + " " + endColor.getBlue());
				p.sendMessage(Utils.chat(pluginPrefix +" &aYour new gradient color has been set!"));
				return true;
			}
			p.sendMessage(Utils.chat(pluginPrefix+" &cSpecified colors do not exist or are not supported!"));
			return false;
		}
		
		
		
		return false;
	}

	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(args.length == 1) {
			List<String> firstArgs = new ArrayList<>();
			firstArgs.add("set");
			firstArgs.add("reset");
			firstArgs.add("list");
			firstArgs.add("customize");
			firstArgs.add("setgradient");
			if(player.isOp()) {
				firstArgs.add("reload");
				firstArgs.add("setcolor");
			}
	        final List<String> completions = new ArrayList<>();
	        StringUtil.copyPartialMatches(args[0], firstArgs, completions);
	        Collections.sort(completions);
	        return completions;
		}
		if(args[0].equalsIgnoreCase("setcolor")) {
			if(args.length == 2) {
				final List<String> allColors = allColors();
				allColors.add("rainbow");
		        final List<String> completions = new ArrayList<>();
		        StringUtil.copyPartialMatches(args[1], allColors, completions);
		        Collections.sort(completions);
		        return completions;	
			}
		}
		if(args[0].equalsIgnoreCase("set")) {
			final List<String> userColors = playerColors(player);
	        final List<String> completions = new ArrayList<>();
	        StringUtil.copyPartialMatches(args[1], userColors, completions);
	        Collections.sort(completions);
	        return completions;
		}
		return null;
	}

}
