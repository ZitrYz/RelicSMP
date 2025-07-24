package relics.relicsItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import RelicSmp.UsefullFunctions;

public class HollowBonesItem {
	
	public ItemStack hollow_bones_item;
	
	public HollowBonesItem() {
		
		Material material = Material.HEAVY_CORE;
		List<String> lore = Arrays.asList(
			    "",
			    "§eLight Weight: §7Permament speed III",
			    "§eOsteoporosis: §7Permament weakness II"
		);
		
		hollow_bones_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lHOLLOW BONES §c§k#", "HOLLOW_BONES");
	}

}