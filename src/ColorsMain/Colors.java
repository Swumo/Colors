package ColorsMain;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import ColorsCommands.Commands;
import ColorsGUI.CustomColorGUI;
import ColorsListeners.Listeners;
import ColorsUtils.ColorConfig;
import ColorsUtils.ConfigWrapper;

public class Colors extends JavaPlugin{
	
	private ColorConfig config;
	private static ConfigWrapper playerFile;
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	public static Plugin getInstance() {
		return Bukkit.getServer().getPluginManager().getPlugin("Colors");
	}
	@Override
	public void onEnable() {
		new Commands(this);
		new Listeners(this);
		config = new ColorConfig(this);
		config.loadConfig();
		playerFile = new ConfigWrapper(this, "userdata", "playerColors.yml");
		playerFile.createNewFile(null, "ChatColor playerColors.yml\nPlease do not edit this file!");
		CustomColorGUI.initialize();
		System.out.println(ANSI_CYAN + "[Colors]" + ANSI_RESET + ANSI_GREEN + " Successfully enabled!" + ANSI_RESET);
	}
	
	public void onDisable() {
		saveConfig();
		System.out.println(ANSI_CYAN + "[Colors]" + ANSI_RESET + ANSI_RED + " Successfully disabled!" + ANSI_RESET);
	}
	
	public static void savePlayerColor(Player player, String colorCode) {
		FileConfiguration c = playerFile.getConfig();
		c.set(player.getUniqueId().toString(), colorCode);
		playerFile.saveConfig();
		return;
	}
	
	public static String getPlayerColor(Player player) {
		FileConfiguration c = playerFile.getConfig();
		if (c.contains(player.getUniqueId().toString()) && c.isString(player.getUniqueId().toString()) && c.getString(player.getUniqueId().toString()) != null) {
			return c.getString(player.getUniqueId().toString());
		}
		return null;
	}
	
}
