package relics.relicsAbilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import RelicSmp.Main;
import RelicSmp.UsefullFunctions;
import relics.Relic;
import relics.RelicAbility;

public class RelicOfOblivionAbility implements RelicAbility {

	@Override
	public boolean execute(Player player) {
		if (!UsefullFunctions.checkIfCanUse()) return false;
		if (!UsefullFunctions.checkForCooldown(player, Main.plugin.getConfig().getInt("RELIC_OF_OBLIVION_COOLDOWN"), Relic.RELIC_OF_OBLIVION)) return false;
		
		int tick_duration = Main.plugin.getConfig().getInt("OBLIVION_DURATION") * 20;
		
		Main.oblivion_ability.add(player);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, tick_duration, 2));
		player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, tick_duration, 1));
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Main.oblivion_ability.remove(player);
				
			}
		}.runTaskLater(Main.plugin, tick_duration);
		
		return true;
	}

}
