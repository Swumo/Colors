package ColorsUtils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Utils {
	
	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked" })
	public static ItemStack createItem(Inventory inv, Material materialId, int amount, int invSlot, String displayName, String... loreString) {
		
		ItemStack item;
		List<String> lore = new ArrayList();
		
		item = new ItemStack(materialId, amount);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.chat(displayName));
		for(String	s : loreString) {
			lore.add(Utils.chat(s));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);	
		
		inv.setItem(invSlot - 1, item);
		return item;
	}
	
	public static List<ChatColor> createGradient(Color start, Color end, int step) {
	   List<ChatColor> colors = new ArrayList<>(step);
	   if(step == 1) {
		   colors.add(ChatColor.of(start));
		   return colors;
	   }
	   if(step > 1) {
		   int stepR = Math.abs(start.getRed() - end.getRed()) / (step - 1);
		   int stepG = Math.abs(start.getGreen() - end.getGreen()) / (step - 1);
		   int stepB = Math.abs(start.getBlue() - end.getBlue()) / (step - 1);
		   int[] direction = { (start.getRed() < end.getRed()) ? 1 : -1, (start.getGreen() < end.getGreen()) ? 1 : -1, (start.getBlue() < end.getBlue()) ? 1 : -1 };
		   for (int i = 0; i < step; i++) {
		     Color color = new Color(start.getRed() + stepR * i * direction[0], start.getGreen() + stepG * i * direction[1], start.getBlue() + stepB * i * direction[2]);
		     colors.add(ChatColor.of(color));
		   } 
		   return colors;		   
	   }
	   return colors;
	 }
	
	public static Color hex2Rgb(String colorStr) {
	    return new Color(
	            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
	            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
	            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
	}
}
