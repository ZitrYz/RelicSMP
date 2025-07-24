package relics.relicsItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import RelicSmp.UsefullFunctions;

public class RelicOfTheForsakenItem {
	
	public ItemStack forsaken_relic_item;
	
	public RelicOfTheForsakenItem() {
		
		Material material = Material.HEAVY_CORE;
		List<String> lore = Arrays.asList(
				"", 
			    "§ePrimary Force: §7Gain permanent Strength I.",
			    "§eUltimate Growth: §7Hp increases to 24.",
			    "§eEternal Courage: §7Shields wont work anymore."
		);
		
		forsaken_relic_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lRELIC OF THE FORSAKEN §c§k#", "FORSAKEN_RELIC");
	}

}
