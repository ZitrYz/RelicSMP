package relics.relicsItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import RelicSmp.UsefullFunctions;

public class RelicOfThePathFinderItem {
	
	public ItemStack path_finder_relic_item;
	
	public RelicOfThePathFinderItem() {
		
		Material material = Material.HEAVY_CORE;
		List<String> lore = Arrays.asList(
				"", 
				"§eLight Path: §7Night vision with torch; hostile mobs avoid you."

		);
		
		path_finder_relic_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lRELIC OF THE PATH FINDER §c§k#", "PATH_FINDER_RELIC");
	}

}
