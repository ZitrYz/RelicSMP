package relics.relicsAbilities;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import RelicSmp.Main;
import RelicSmp.PersistentDataValues;
import RelicSmp.UsefullFunctions;
import relics.Relic;
import relics.RelicAbility;

public class RelicOfTheMinersFortuneAbility implements RelicAbility {

	@Override
	public boolean execute(Player player) {

		if (!UsefullFunctions.checkIfCanUse()) return false;
		if (!UsefullFunctions.checkForCooldown(player, Main.plugin.getConfig().getInt("RELIC_OF_THE_MINERS_FORTUNE_COOLDOWN"), Relic.RELIC_OF_THE_MINERS_FORTUNE)) return false;
		
		int count = 0;
		
		for (ItemStack item : player.getInventory().getContents()) {
			
			if (UsefullFunctions.isTool(item)) {
				
				ItemMeta meta = item.getItemMeta();
				
				meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
				
				meta.getPersistentDataContainer().set(new NamespacedKey(Main.plugin, PersistentDataValues.GREEDED_TOOL.name()), PersistentDataType.BOOLEAN, true);
				
				if (meta.getEnchants().containsKey(Enchantment.FORTUNE)) {
					meta.getPersistentDataContainer().set(new NamespacedKey(Main.plugin, PersistentDataValues.PRE_FORTUNE.name()), PersistentDataType.INTEGER, meta.getEnchants().get(Enchantment.FORTUNE));
				}
				
				meta.addEnchant(Enchantment.FORTUNE, 4, true);
				
				item.setItemMeta(meta);
				
				count++;
			}
			
		}
		if (count > 0) {
			player.sendMessage(ChatColor.GREEN + "Applied fortune IV to all tools in your inventory!");
			return true;
		} else {
			player.sendMessage(ChatColor.RED + "You dont have any tools in your inventory!");
			return false;
		}

		

	}

}
