package relics.relicsItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import RelicSmp.Main;
import RelicSmp.UsefullFunctions;

public class RelicOfTheInfernalBeastItem {
	
	public ItemStack infernal_beast_item;
	
	public RelicOfTheInfernalBeastItem() {
		
		Material material = Material.HEAVY_CORE;
		List<String> lore = Arrays.asList(
				"", 
				"§eAbility - Awakening: §7Transform into a beastly form for 30 seconds every 5 minutes.",
			    "§8("+ UsefullFunctions.formatSecondsNicely(Main.plugin.getConfig().getInt("INFERNAL_BEAST_COOLDOWN")) +" cooldown)",
			    "",
			    "§eHellfire Fury: §7After a kill, summon a Hellhound minion for 1 minute.",
			    "§eBlood thirst: §7In beast form, kills heal 25% of your max health."
		);
		
		infernal_beast_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lRELIC OF THE INFERNAL BEAST §c§k#", "INFERNAL_BEAST_RELIC");
	}

}
