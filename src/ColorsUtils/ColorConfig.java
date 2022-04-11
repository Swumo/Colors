package ColorsUtils;

import org.bukkit.configuration.file.FileConfiguration;

import ColorsMain.Colors;

public class ColorConfig {
	Colors plugin;
	
	public ColorConfig(Colors p) {
		plugin = p;
	}
	
	@SuppressWarnings("deprecation")
	public void loadConfig() {
		
		FileConfiguration config = plugin.getConfig();
		
		config.options().header("Author: Swumo"
				+ "\n"
				+ "\n"
				+ "pluginPrefix - The prefix the plugin uses"
				+ "\n"
				+ "Default plugin prefix: &cC&6h&ea&at&bC&9o&dl&5o&cr &6>>"
				+ "\n"
				+ "\n"
				+ "defaultColor - The color used for /color reset command"
				+ "\n"
				+ "Default color: &f"
				+ "\n"
				+ "\n"
				+ "rainbowColor - Color code sequence the Rainbow chat color uses"
				+ "\n"
				+ "Supported color codes: a b c d e f 1 2 3 4 5 6 7 8 9"
				+ "\n"
				+ "Default sequence: c6eab9d");
		
		config.addDefault("rainbowColor", "c6eab9d");
		config.addDefault("pluginPrefix", "&cC&6h&ea&at&bC&9o&dl&5o&cr &6>>");
		config.addDefault("defaultColor", "&f");
		config.options().copyDefaults(true);
		plugin.saveConfig();
		plugin.reloadConfig();
	}
}
