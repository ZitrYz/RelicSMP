package relics.relicsItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import RelicSmp.UsefullFunctions;

public class RelicOfTheAlchemistItem {
	
	public ItemStack alchemist_relic_item;
	
	public RelicOfTheAlchemistItem() {
		
		Material material = Material.HEAVY_CORE;
		List<String> lore = Arrays.asList(
				"", 
				"§eGordon Ramsey: §7Potions last 25% longer."
		);
		
		alchemist_relic_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lRELIC OF THE ALCHEMIST §c§k#", "ALCHEMIST_RELIC");
	}

}
