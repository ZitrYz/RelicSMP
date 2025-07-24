package relics.relicsItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import RelicSmp.Main;
import RelicSmp.UsefullFunctions;

public class RelicOfOblivionItem {
	
	public ItemStack relic_of_oblivion_item;
	
	public RelicOfOblivionItem() {
		
		Material material = Material.HEAVY_CORE;
		List<String> lore = Arrays.asList(
				"",
			    "§eAbility - Oblivion's Hunger: §7For 30 seconds, every enemy you kill disintegrates into ",
			    "§7nothing (they can’t revive if they have a revival based relic (Doesn’t work on phoenix ability))",
			    "§7and you gain Speed III and Strength II during this window. Kills during Oblivion's Hunger ",
			    "§7also reset your Voidform cooldown.",
			    "§8("+ UsefullFunctions.formatSecondsNicely(Main.plugin.getConfig().getInt("ASHEN_KING_ABILITY_COOLDOWN")) +" cooldown)",
			    "",
			    "§eVoidform: §7When crouching for 5 seconds, you phase out of reality (become untargetable, ",
			    "§7invisible, and immune to damage) for 8 seconds",
			    "§8("+ UsefullFunctions.formatSecondsNicely(Main.plugin.getConfig().getInt("VOIDFORM_COOLDOWN")) +" cooldown)"
		);
		
		relic_of_oblivion_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lRELIC OF OBLIVION §c§k#", "OBLIVION_RELIC");
	}

}