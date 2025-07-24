package relics.relicsAbilities;

import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import RelicSmp.Main;
import RelicSmp.UsefullFunctions;
import relics.Relic;
import relics.RelicAbility;

public class RelicOfInfernalBeastAbility implements RelicAbility{

	@Override
	public boolean execute(Player player) {
		if (!UsefullFunctions.checkIfCanUse()) return false;
		if (!UsefullFunctions.checkForCooldown(player, Main.plugin.getConfig().getInt("RELIC_OF_INFERNAL_BEAST_COOLDOWN"), Relic.RELIC_OF_INFERNAL_BEAST)) return false;
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 4));
		player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 40, 4));
		
		Main.beast_mode.add(player);
		
		new BukkitRunnable() {
			
			int executes = 1;
			
			@Override
			public void run() {
				
				if (executes == 5) {
					cancel();
					return;
				}
				
				if (executes < 4) {
					player.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, player.getLocation().add(0, 1, 0), 20, 0.1, 0.1, 0.1);
				} else {
					if (executes == 4) {
						Map<String, Object> rawMap1 = Main.plugin.getConfig().getConfigurationSection("INFERNAL_BEAST_EFFECTS").getValues(false);
						Map<String, Integer> effectMap = rawMap1.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (Integer) e.getValue()));
						for (String key : effectMap.keySet()) {
							Player p = (Player) player;
							p.addPotionEffect(new PotionEffect(Registry.EFFECT.get(NamespacedKey.minecraft(key)), 27 * 20, effectMap.get(key) - 1));
						}
						new BukkitRunnable() {
							
							int partExec = 0;
							
							@Override
							public void run() {
								if (partExec * 5 == 27 * 20) {
									Main.beast_mode.remove(player);
									cancel();
									return;
								}
								player.getWorld().spawnParticle(Particle.CRIMSON_SPORE, player.getLocation().add(0, 1, 0), 50, 0.3, 0.3, 0.3, 0.025);
								player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0, 1, 0), 20, 0.3, 0.3, 0.3, 0.05);
								partExec++;
							}
						}.runTaskTimer(Main.plugin, 0, 5);
					}
				}
				
				executes++;
			}
		}.runTaskTimer(Main.plugin, 0, 20);
		
		return true;
	}


}
