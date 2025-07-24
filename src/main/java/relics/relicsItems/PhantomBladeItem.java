package relics.relicsItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import RelicSmp.UsefullFunctions;

public class PhantomBladeItem {
	
	public ItemStack phantom_blade_item;
	
	public PhantomBladeItem() {
		
		Material material = Material.HEAVY_CORE;
		List<String> lore = Arrays.asList(
			    "",
			    "§eShadow Curse: §7Minus one heart to max hearts (-1 max hearts) ",
			    "§eNight Warrior: §7Having invisibility grants +1 attack damage"
		);
		
		phantom_blade_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lPHANTOM BLADE §c§k#", "PHANTOM_BLADE");
	}

}