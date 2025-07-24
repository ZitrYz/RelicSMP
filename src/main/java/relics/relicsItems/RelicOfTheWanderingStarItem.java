package relics.relicsItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import RelicSmp.Main;
import RelicSmp.UsefullFunctions;

public class RelicOfTheWanderingStarItem {
	
	public ItemStack wandering_star_relic_item;
	
	public RelicOfTheWanderingStarItem() {
		
		Material material = Material.HEAVY_CORE;
		List<String> lore = Arrays.asList(
				"", 
				"§eAbility - Divine Intervention: §7Spawns 1 tiny loot chest near you each day",
			    "§8("+ UsefullFunctions.formatSecondsNicely(Main.plugin.getConfig().getInt("WANDERING_STAR_COOLDOWN")) +" cooldown)"
		);
		
		wandering_star_relic_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lRELIC OF THE WANDERING STAR §c§k#", "WANDERING_STAR_RELIC");
	}

}
