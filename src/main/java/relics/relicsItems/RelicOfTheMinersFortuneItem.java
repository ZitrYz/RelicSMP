package relics.relicsItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import RelicSmp.Main;
import RelicSmp.UsefullFunctions;

public class RelicOfTheMinersFortuneItem {
	
	public ItemStack miners_fortune_item;
	
	public RelicOfTheMinersFortuneItem() {
		
		Material material = Material.HEAVY_CORE;
		List<String> lore = Arrays.asList(
				"", 
				"§eAbility - Magic of the Mines: §7Enchants tools to fortune IV",
			    "§8("+ UsefullFunctions.formatSecondsNicely(Main.plugin.getConfig().getInt("MINERS_FORTUNE_COOLDOWN")) +" cooldown)",
			    "",
			    "§eLepricon Greed: §7Tools always have curse of vanishing/binding if you have them (miners greed)"
		);
		
		miners_fortune_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lTHE RELIC OF MINERS FORTUNE §c§k#", "MINERS_FORTUNE_RELIC");
	}

}
