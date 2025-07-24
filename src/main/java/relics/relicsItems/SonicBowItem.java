package relics.relicsItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import RelicSmp.Main;
import RelicSmp.UsefullFunctions;

public class SonicBowItem {
	
	public ItemStack sonic_bow_item;
	
	public SonicBowItem() {
		
		Material material = Material.BOW;
		List<String> lore = Arrays.asList(
				"", 
				"§eAbility: §7Sonically charged shriek",
			    "§8("+ UsefullFunctions.formatSecondsNicely(Main.plugin.getConfig().getInt("SONIC_BOW_COOLDOWN")) +" cooldown)"
		);
		
		sonic_bow_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lSONIC BOW RELIC §c§k#", "SONIC_BOW_RELIC");
		ItemMeta meta = sonic_bow_item.getItemMeta();
		meta.addEnchant(Enchantment.POWER, 5, true);
		meta.addEnchant(Enchantment.INFINITY, 1, true);
		meta.addEnchant(Enchantment.FLAME, 1, true);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
		sonic_bow_item.setItemMeta(meta);
	}

}
