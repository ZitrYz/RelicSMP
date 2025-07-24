package relics.relicsItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import RelicSmp.Main;
import RelicSmp.UsefullFunctions;

public class AshenKingItem {
	
	public ItemStack ashen_king_item;
	
	public AshenKingItem() {
		
		Material material = Material.HEAVY_CORE;
		List<String> lore = Arrays.asList(
				"", 
				"§eAbility - Pheonix Soul: §7Upon death, instantly revive at 50% HP,",
			    "§7burn all enemies within 10 blocks, and gain Strength II and Resistance II for 30 seconds.",
			    "§8("+ UsefullFunctions.formatSecondsNicely(Main.plugin.getConfig().getInt("OBLIVION_COOLDOWN")) +" cooldown)",
			    "",
			    "§eCrown of Ash: §7Gain permanent Fire Resistance, Lava Walking, and +1 extra heart.",
			    "§eAshen Wrath: §7Every time you kill a player or mob, nearby enemies within 8 blocks are set ablaze",
			    "§7and suffer Weakened (Strength -1 attack damage) for 10 seconds."
		);
		
		ashen_king_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lASHEN KING §c§k#", "ASHEN_KING");
	}

}
