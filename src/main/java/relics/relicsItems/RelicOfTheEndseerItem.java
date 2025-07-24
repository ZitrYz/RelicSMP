package relics.relicsItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import RelicSmp.UsefullFunctions;

public class RelicOfTheEndseerItem {
	
	public ItemStack endseer_relic_item;
	
	public RelicOfTheEndseerItem() {
		
		Material material = Material.HEAVY_CORE;
		List<String> lore = Arrays.asList(
				"", 
			    "§eEye of Ender: §7Endermen ignore you.",
			    "§eSwiftness of the Void: §7Speed 2 granted for 5 seconds after pearling.",
			    "§eVoid Cat: §7No damage from pearling.",
			    "§eGentle Hand: §7Endermites won't spawn after pearling."
		);
		
		endseer_relic_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lRELIC OF THE ENDSEER §c§k#", "ENDSEER_RELIC");
	}

}
