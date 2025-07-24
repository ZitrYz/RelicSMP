package relics.relicsAbilities;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import RelicSmp.Main;
import RelicSmp.UsefullFunctions;
import relics.Relic;
import relics.RelicAbility;

public class HermesSandalsAbility implements RelicAbility {

	@Override
	public boolean execute(Player player) {
		
		if (!UsefullFunctions.checkIfCanUse()) return false;
		if (!UsefullFunctions.checkForCooldown(player, Main.plugin.getConfig().getInt("HERMES_SANDALS_COOLDOWN"), Relic.HERMES_SANDALS)) return false;
		
		Vector direction = player.getLocation().getDirection().normalize();
		int durationTicks = 5;
		int maxSpeed = 3;
		
        new BukkitRunnable() {
            int tick = 0;

            @Override
            public void run() {
                if (tick >= durationTicks || !player.isOnline() || player.isDead()) {
                    this.cancel();
                    return;
                }

                double progress = (double) tick / durationTicks;
                double speed = maxSpeed * (Math.max(1 - Math.pow(progress, 2), 0.7));
                Vector velocity = direction.clone().multiply(speed);

                player.setVelocity(velocity);
                tick++;
            }
        }.runTaskTimer(Main.plugin, 0L, 1L);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
		return true;
	}

}
