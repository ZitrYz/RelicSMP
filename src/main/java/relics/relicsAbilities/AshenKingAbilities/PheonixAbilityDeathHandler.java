package relics.relicsAbilities.AshenKingAbilities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import RelicSmp.Main;
import RelicSmp.UsefullFunctions;

public class PheonixAbilityDeathHandler {

	public void revivePlayer(Player p, EntityDamageEvent event) {
		
        UUID ownerUUID = p.getUniqueId();
        List<UUID> trustedList = new ArrayList<>(Main.trusted_players.getOrDefault(ownerUUID, new ArrayList<>()));
		
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() / 2);
		p.damage(1);
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() / 2);
		for (Entity ent : p.getNearbyEntities(10, 10, 10)) {
			if (ent instanceof Player) {
				if (trustedList.contains(((Player) ent).getUniqueId())) continue;
			}
			ent.setFireTicks(20 * 5);
		}
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 30 * 20, 1));
		p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 30 * 20, 1));
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1);
		
		p.sendMessage(ChatColor.GREEN + "Your Pheonix ability has popped to save you. Ability next ready in: " + ChatColor.GRAY + UsefullFunctions.formatSecondsNicely(Main.plugin.getConfig().getInt("ASHEN_KING_COOLDOWN")));
		
		UsefullFunctions.spawnParticleCircle(p, 10, 200);
		
		Main.pheonix_ability_used.remove(p);
		
		event.setCancelled(true);
	}

}
