package relics.relicsItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import RelicSmp.UsefullFunctions;

public class RelicOfTheTitansWillItem {
	
	public ItemStack titans_will_relic_item;
	
	public RelicOfTheTitansWillItem() {
		
		Material material = Material.HEAVY_CORE;
		List<String> lore = Arrays.asList(
				"", 
			    "§eUnbroken will: §7Gain Resistance I permamently.",
			    "§eHypermas: §7Permamently gain Slowness I"
		);
		
		titans_will_relic_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lRELIC OF THE TITANS WILL §c§k#", "TITANS_WILL_RELIC");
	}

}
