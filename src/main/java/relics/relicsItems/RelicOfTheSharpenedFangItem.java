package relics.relicsItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import RelicSmp.UsefullFunctions;

public class RelicOfTheSharpenedFangItem {
	
	public ItemStack sharpened_fang_item;
	
	public RelicOfTheSharpenedFangItem() {
		
		Material material = Material.HEAVY_CORE;
		List<String> lore = Arrays.asList(
				"", 
			    "§eFiness Blow: §7Every 3rd hit deals +2 damage."
		);
		
		sharpened_fang_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lRELIC OF SHARPENED FANG §c§k#", "SHARPENED_FANG_RELIC");
	}

}
