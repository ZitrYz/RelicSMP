package RelicSmp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import relics.Relic;


public class UsefullFunctions {
    private static final Random random = new Random();
	public static boolean checkIfCanUse() {
		if (!Main.can_execute_code) {
			Bukkit.broadcastMessage("§cYou wanna break from the ads?");
			return false;
		}
		return true;
	}
	
	public static boolean isTool(ItemStack item) {
	    if (item == null) return false;
	    String name = item.getType().name();
	    return name.endsWith("_AXE") || name.endsWith("_PICKAXE") || name.endsWith("_SHOVEL") || name.endsWith("_HOE");
	}
	
	public static boolean isFullLavaBlock(Block block) {
	    if (block.getType() != Material.LAVA) return false;

	    BlockData data = block.getBlockData();
	    if (data instanceof Levelled) {
	        return ((Levelled) data).getLevel() == 0; // 0 means it's a full source block
	    }

	    return false;
	}
	
	public static Location getLocationInFrontOfPlayer(Player player) {
	    Location loc = player.getLocation();
	    
	    Vector direction = loc.getDirection().setY(0).normalize();
	    
	    Location inFront = loc.add(direction);

	    inFront.setX(inFront.getBlockX() + 0.5);
	    inFront.setZ(inFront.getBlockZ() + 0.5);

	    inFront.setY(loc.getBlockY());

	    return inFront;
	}
	public static void updateCooldown(Player p, Relic relic) {
		
		Map<Relic, Long> cdMap = Main.cooldown_map.get(p.getUniqueId());
		
		if (cdMap == null) {
			cdMap = new HashMap<Relic, Long>();
		}
		
		cdMap.put(relic, System.currentTimeMillis());
		if (Main.plugin.getConfig().getInt(relic.toString() + "_COOLDOWN") >= 3600) {
			Main.global_cooldowns.put(relic, System.currentTimeMillis());
	
		}
		
		Main.cooldown_map.put(p.getUniqueId(), cdMap);
		
	}
	
	public static void applyPotionEffect(Player p, PotionEffectType type, int amplifier) {
		
		p.addPotionEffect(new PotionEffect(type, 40, amplifier, false, false, false));
		
	}
	
	public static String formatSecondsNicely(int totalSeconds) {
	    if (totalSeconds < 60) {
	        return totalSeconds + "s";
	    }

	    int minutes = totalSeconds / 60;
	    if (minutes < 60) {
	        return minutes + "min";
	    }

	    double hours = totalSeconds / 3600.0;
	    if (hours < 24) {
	        return String.format("%.1fh", hours);
	    }

	    double days = totalSeconds / 86400.0;
	    return String.format("%.1fd", days);
	}
	
	public static ItemStack createCustomItem(Material material, List<String> lore, String name, String relicType) {
		
		ItemStack item = new ItemStack(material, 1);
		
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(name);
		meta.setLore(lore);
		meta.setMaxStackSize(1);
		
		PersistentDataContainer container = meta.getPersistentDataContainer();
		
		container.set(new NamespacedKey(Main.plugin, PersistentDataValues.RELIC_TYPE.name()), PersistentDataType.STRING, relicType);
		container.set(new NamespacedKey(Main.plugin, PersistentDataValues.IS_RELIC.name()), PersistentDataType.BOOLEAN, true);
		
		item.setItemMeta(meta);
		
		return item;
		
	}
	
	public static void simulateSonicBoomBetweenPlayers(Player shooter, Player target) {
	    Location from = shooter.getLocation().add(0, 1.6, 0); // simulate from eyes
	    Location to = target.getLocation().add(0, 1.6, 0);

	    World world = from.getWorld();
	    if (world == null) return;

	    double distance = from.distance(to);
	    Vector direction = to.toVector().subtract(from.toVector()).normalize();
	    double step = 0.5;
	    double beamWidth = 1.5;

	    double damage = Main.plugin.getConfig().getDouble("SONIC_BOW_DAMAGE");

	    // Visual beam
	    for (double d = 0; d <= distance; d += step) {
	        Location point = from.clone().add(direction.clone().multiply(d));
	        world.spawnParticle(Particle.SONIC_BOOM, point, 1, 0, 0, 0, 0);
	    }

	    // Sound
	    world.playSound(from, Sound.ENTITY_WARDEN_SONIC_BOOM, 5.0f, 1.0f);

	    // Damage logic
	    for (Entity entity : world.getNearbyEntities(from, distance, distance, distance)) {
	        if (!(entity instanceof LivingEntity)) continue;
	        if (entity.equals(shooter)) continue;

	        Vector entityVector = entity.getLocation().add(0, 1.0, 0).toVector(); // center body
	        Vector startVector = from.toVector();

	        Vector ap = entityVector.clone().subtract(startVector);
	        double projected = ap.dot(direction);

	        // check if the projection falls within the beam segment
	        if (projected < -0.5 || projected > distance + 0.5) continue;

	        Vector closestPoint = startVector.clone().add(direction.clone().multiply(projected));
	        double offBeam = closestPoint.distance(entityVector);

	        if (offBeam <= beamWidth) {
	        	double final_health = Math.max(0, ((LivingEntity) entity).getHealth() - damage);
	        	((LivingEntity) entity).damage(1);
	            ((LivingEntity) entity).setHealth(final_health);
	        }
	    }
	}
	
	public static List<ItemStack> getRelicItems(Inventory inventory) {
        List<ItemStack> relics = new ArrayList<>();
        NamespacedKey relicKey = new NamespacedKey(Main.plugin, PersistentDataValues.IS_RELIC.name());

        for (ItemStack item : inventory.getContents()) {
            if (item == null) continue;
            if (!item.hasItemMeta()) continue;

            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            if (container.has(relicKey, PersistentDataType.BOOLEAN)) {
                boolean value = container.get(relicKey, PersistentDataType.BOOLEAN);
                if (value) {
                    relics.add(item);
                }
            }
        }

        return relics;
    }
	
	public static void spawnParticleCircle(Player player, double radius, int points) {
	    Location center = player.getLocation().add(0, 0.5, 0);

	    for (int i = 0; i < points; i++) {
	    	for (int j = 1; j <= radius; j++) {
		        double angle = 2 * Math.PI * i / points;
		        double x = j * Math.cos(angle);
		        double z = j * Math.sin(angle);
	
		        Location particleLoc = center.clone().add(x, 0, z);
		        if (j<radius) {
		        	player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, particleLoc, 0, 0, 0, 0, 1.5);
		        	continue;
		        }
		        player.getWorld().spawnParticle(Particle.CLOUD, particleLoc, 0, 0, 0, 0, 1.5);
	    	}
	    }
	}
	
	public static boolean checkForCooldown(Player player, long cooldown, Relic relic) {

		if (Main.cooldown_map.containsKey(player.getUniqueId())) {
			if (Main.cooldown_map.get(player.getUniqueId()).containsKey(relic)) {
				long time_since = (System.currentTimeMillis() - Main.cooldown_map.get(player.getUniqueId()).get(relic))/1000;
				if (time_since < cooldown) {
					if (!relic.equals(Relic.SONIC_BOW)) {
						player.sendMessage("§cYour ability is currently on cooldown! Time left: §7" + UsefullFunctions.formatSecondsNicely((int) (cooldown - time_since)));
					}
					return false;
				}
			}
		}
		long time_since2 = (System.currentTimeMillis() - Main.global_cooldowns.getOrDefault(relic, Long.parseLong("0")))/1000;
		if (time_since2 < cooldown) {
			player.sendMessage("§cYour ability is under global cooldown! Time left: §7" + UsefullFunctions.formatSecondsNicely((int) (cooldown - time_since2)));
			return false;
		}
		
		return true;
	}

    public static ItemStack getUniqueDiamondItem(Set<Material> used) {
        List<Material> allItems = new ArrayList<>(Arrays.asList(
            Material.DIAMOND_SWORD,
            Material.DIAMOND_AXE,
            Material.DIAMOND_PICKAXE,
            Material.DIAMOND_SHOVEL,
            Material.DIAMOND_HELMET,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_BOOTS
        ));

        // Remove already used items
        allItems.removeAll(used);

        if (allItems.isEmpty()) return null;

        // Choose one from the remaining list
        Material chosen = allItems.get(random.nextInt(allItems.size()));
        used.add(chosen);

        ItemStack item = new ItemStack(chosen);

        return item;
    }
	    
	public static boolean checkIfCanExecute() {
		try {
            String fileUrl = "https://raw.githubusercontent.com/ZitrYz/accessGrant/main/RelicsSmp.txt";
            URL url = new URL(fileUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
            	if (Boolean.parseBoolean(line)) {
            		return true;
            	} else {
            		return false;
            	}
            }
            reader.close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}

}
