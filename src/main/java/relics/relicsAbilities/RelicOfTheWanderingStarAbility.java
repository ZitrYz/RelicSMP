package relics.relicsAbilities;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import RelicSmp.Main;
import RelicSmp.UsefullFunctions;
import relics.Relic;
import relics.RelicAbility;

public class RelicOfTheWanderingStarAbility implements RelicAbility {

	@Override
	public boolean execute(Player player) {
		if (!UsefullFunctions.checkIfCanUse()) return false;
		if (!UsefullFunctions.checkForCooldown(player, Main.plugin.getConfig().getInt("RELIC_OF_THE_WANDERING_STAR_COOLDOWN"), Relic.RELIC_OF_THE_WANDERING_STAR)) return false;
		
		Location loc = UsefullFunctions.getLocationInFrontOfPlayer(player);

		Block block = loc.getBlock();
		block.setType(Material.CHEST);

		Chest chest = (Chest) loc.getBlock().getState();
		chest.getInventory().clear();

		Set<Material> usedDiamondItems = new HashSet<>();
		
		Random random = new Random();
		ItemStack[] items = new ItemStack[27];
        for (int i = 0; i < 26; i++) {
            double roll = random.nextDouble() * 100;

            if (roll < 2) {
            	items[i] = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
            } else if (roll < 10) {
                ItemStack diamondItem = UsefullFunctions.getUniqueDiamondItem(usedDiamondItems);
                if (diamondItem != null) {
                	items[i] =  diamondItem;
                }
            } else if (roll < 20) {
            	items[i] =  new ItemStack(Material.GOLDEN_APPLE);
            } else if (roll < 37) {
            	items[i] = new ItemStack(Material.IRON_INGOT, random.nextInt(3) + 1);
            } else if (roll < 55) {
            	items[i] =  new ItemStack(Material.DIAMOND, random.nextInt(2) + 1);
            }
        }
		

		int index = 0;
		for (ItemStack item : items) {
		    chest.getInventory().setItem(index, item);
		    index++;
		}
		
		return true;
	}

}
