package RelicSmp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import relics.Relic;

public class RelicPassiveManager {

	private static final Map<Relic, UUID> RELIC_MODIFIER_UUIDS = new HashMap<>();

    // Called during plugin enable
    public static void startRelicTick() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    List<Relic> equipped = Main.relics_equipped.get(player);
                    if (equipped == null) {
                    	for (Relic relic : Relic.values()) {
                    		removePassive(player, relic);
                        }
                    	continue;
                    }

                    for (Relic relic : Relic.values()) {
                        if (equipped.contains(relic)) {
                            applyPassive(player, relic);
                        } else {
                            removePassive(player, relic);
                        }
                    }
                }
            }
        }.runTaskTimer(Main.plugin, 0L, 20L); // every 1 second
    }

    private static void applyPassive(Player player, Relic relic) {
        if (relic == Relic.ASHEN_KING) {
            applyHealthModifier(player, Attribute.GENERIC_MAX_HEALTH, 2.0, "AshenKingHealth", getRelicUUID(relic));
            UsefullFunctions.applyPotionEffect(player, PotionEffectType.FIRE_RESISTANCE, 0);
        }
        if (relic == Relic.PHANTOM_BLADE) {
        	applyHealthModifier(player, Attribute.GENERIC_MAX_HEALTH, -2.0, "PhantomBladeHealth", getRelicUUID(relic));
        	new BukkitRunnable() {
				@Override
				public void run() {
					if (!Main.relics_equipped.get(player).contains(relic)) {
						removeHealthModifier(player, Attribute.GENERIC_ATTACK_DAMAGE, getRelicUUID(relic));
						cancel();
						return;
					}
		        	if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
		            	applyHealthModifier(player, Attribute.GENERIC_ATTACK_DAMAGE, 2.0, "PhantomBladeDamage", getRelicUUID(relic));
		        	} else {
		        		removeHealthModifier(player, Attribute.GENERIC_ATTACK_DAMAGE, getRelicUUID(relic));
		        	}
				}
			}.runTaskTimer(Main.plugin, 0, 10);
        }
        if (relic == Relic.HOLLOW_BONES) {
        	UsefullFunctions.applyPotionEffect(player, PotionEffectType.SPEED, 2);
            UsefullFunctions.applyPotionEffect(player, PotionEffectType.WEAKNESS, 1);
        }
        if (relic == Relic.RELIC_OF_THE_FORSAKEN) {
        	applyHealthModifier(player, Attribute.GENERIC_MAX_HEALTH, 4.0, "ForsakenRelicHealth", getRelicUUID(relic));
        	UsefullFunctions.applyPotionEffect(player, PotionEffectType.STRENGTH, 0);
        }
        if (relic == Relic.RELIC_OF_THE_TITANS_WILL) {
        	UsefullFunctions.applyPotionEffect(player, PotionEffectType.RESISTANCE, 0);
        	UsefullFunctions.applyPotionEffect(player, PotionEffectType.SLOWNESS, 0);
        }
    }

    public static void removePassive(Player player, Relic relic) {
        if (relic == Relic.ASHEN_KING) {
            removeHealthModifier(player, Attribute.GENERIC_MAX_HEALTH, getRelicUUID(relic));
        }
        if (relic == Relic.PHANTOM_BLADE) {
            removeHealthModifier(player, Attribute.GENERIC_MAX_HEALTH, getRelicUUID(relic));
            removeHealthModifier(player, Attribute.GENERIC_ATTACK_DAMAGE, getRelicUUID(relic));
        }
        if (relic == Relic.RELIC_OF_THE_FORSAKEN) {
        	removeHealthModifier(player, Attribute.GENERIC_MAX_HEALTH, getRelicUUID(relic));
        }
    }

    private static UUID getRelicUUID(Relic relic) {
        return RELIC_MODIFIER_UUIDS.computeIfAbsent(relic, r -> UUID.nameUUIDFromBytes(("RELIC-" + r.name()).getBytes()));
    }

    @SuppressWarnings("removal")
	private static void applyHealthModifier(Player player, Attribute attribute, double value, String name, UUID uuid) {
        AttributeInstance attr = player.getAttribute(attribute);
        if (attr == null) return;

        for (AttributeModifier mod : attr.getModifiers()) {
            if (mod.getUniqueId().equals(uuid)) return;
        }

        AttributeModifier modifier = new AttributeModifier(uuid, name, value, AttributeModifier.Operation.ADD_NUMBER);
        attr.addModifier(modifier);
    }

    @SuppressWarnings("removal")
	private static void removeHealthModifier(Player player, Attribute attribute, UUID uuid) {
        AttributeInstance attr = player.getAttribute(attribute);
        if (attr == null) return;
        
        attr.getModifiers().stream()
            .filter(mod -> mod.getUniqueId().equals(uuid))
            .forEach(attr::removeModifier);
    }

}
