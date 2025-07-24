package relics.relicsItems;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import RelicSmp.Main;
import RelicSmp.UsefullFunctions;

public class HermesSandalsItem {
	
	public ItemStack hermes_sandals_item;
	
	@SuppressWarnings("removal")
	public HermesSandalsItem() {
		
		Material material = Material.GOLDEN_BOOTS;
		List<String> lore = Arrays.asList(
				"", 
				"§eAbility: §7Launches you 20 blocks forward, gives speed 1 for 5 seconds.",
			    "§8("+ UsefullFunctions.formatSecondsNicely(Main.plugin.getConfig().getInt("HERMES_SANDALS_COOLDOWN")) +" cooldown)"
		);
		
		hermes_sandals_item = UsefullFunctions.createCustomItem(material, lore, "§c§k# §c§lHERMES SANDALS §c§k#", "HERMES_SANDALS");
		ItemMeta meta = hermes_sandals_item.getItemMeta();
		
		meta.setUnbreakable(true);
		
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
		
		meta.addAttributeModifier(
            Attribute.GENERIC_ARMOR,
            new AttributeModifier(UUID.randomUUID(), "armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET)
        );

        meta.addAttributeModifier(
            Attribute.GENERIC_ARMOR_TOUGHNESS,
            new AttributeModifier(UUID.randomUUID(), "armor_toughness", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET)
        );

        meta.addAttributeModifier(
            Attribute.GENERIC_KNOCKBACK_RESISTANCE,
            new AttributeModifier(UUID.randomUUID(), "knockback_resistance", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET)
        );
        
        meta.addEnchant(Enchantment.PROTECTION, 4, true);
		
		hermes_sandals_item.setItemMeta(meta);
	}

}
