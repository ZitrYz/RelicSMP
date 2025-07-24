package events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import RelicSmp.Main;
import RelicSmp.PersistentDataValues;
import RelicSmp.UsefullFunctions;
import relics.Relic;
import relics.relicsAbilities.AshenKingAbilities.PheonixAbilityDeathHandler;

public class AbilitiesHandler implements Listener {
	
	private PheonixAbilityDeathHandler pheonixAbilityDeathHandler;
	
	private final HashMap<UUID, BukkitRunnable> sneakingTasks = new HashMap<>();
	private static final Set<UUID> ignoreNextEffect = new HashSet<>();
	
	public AbilitiesHandler() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
		this.pheonixAbilityDeathHandler = new PheonixAbilityDeathHandler();
	}
	
	@EventHandler
	public void onDeath(EntityDamageEvent event) {
		if (!UsefullFunctions.checkIfCanUse()) return;
		
		if (event.getEntity().getPersistentDataContainer().has(new NamespacedKey(Main.plugin, PersistentDataValues.HELLHOUND_MINION.name()))) {
			event.setDamage(0);
		}
		
		if (!(event.getEntity() instanceof Player)) return;
		Player p = (Player) event.getEntity();
		if (Main.relics_equipped.getOrDefault(p, List.of()).contains(Relic.RELIC_OF_THE_ENDSEER)) {
			if (justTeleported.contains(p.getUniqueId())) {
				event.setCancelled(true);
			}
			
		}
		if (Main.relics_equipped == null) return;
		if (Main.relics_equipped.isEmpty()) return;
		
		if (Main.relics_equipped.containsKey(p) && (p.getHealth() - event.getFinalDamage() <= 0)) {
			
			if (Main.relics_equipped.get(p).contains(Relic.ASHEN_KING)) {
				if (UsefullFunctions.checkForCooldown(p, Main.plugin.getConfig().getLong("ASHEN_KING_COOLDOWN"), Relic.ASHEN_KING)) {
					
					if (event instanceof EntityDamageByEntityEvent) {
			            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();

			            if (damager instanceof Player) {
			                Player attacker = (Player) damager;

			                if (Main.oblivion_ability != null && Main.oblivion_ability.contains(attacker)) {
			                    return;
			                }
			            }
			        }
					
					this.pheonixAbilityDeathHandler.revivePlayer(p, event);
					
					UsefullFunctions.updateCooldown(p, Relic.ASHEN_KING);
					
				}
			}
			
		}
		

	
	}
	
	private final Set<UUID> justTeleported = new HashSet<>();
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
	    if (event.getCause() == TeleportCause.ENDER_PEARL && Main.relics_equipped.getOrDefault(event.getPlayer(), List.of()).contains(Relic.RELIC_OF_THE_ENDSEER)) {
	        justTeleported.add(event.getPlayer().getUniqueId());

	        Bukkit.getScheduler().runTaskLater(Main.plugin, () ->
	            justTeleported.remove(event.getPlayer().getUniqueId()), 2L
	        );
	        
	        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 1, false, false, false));

	    }
	}
	
	@EventHandler
	public void onEndermiteSpawn(CreatureSpawnEvent event) {
		if (event.getEntityType() != EntityType.ENDERMITE) return;

	    Location spawnLoc = event.getLocation();
	    World world = spawnLoc.getWorld();

	    for (Entity nearby : world.getNearbyEntities(spawnLoc, 1.0, 1.0, 1.0)) {
	        if (nearby instanceof Player) {
	        	Player player = (Player) nearby;
	            if (justTeleported.contains(player.getUniqueId())) {
	                event.setCancelled(true); // ✅ Cancel the endermite
	                break;
	            }
	        }
	    }
	}
	
	@EventHandler
	public void onArrowShoot(EntityShootBowEvent event) {
	    if (!(event.getEntity() instanceof Player)) return;
	    if (!(event.getProjectile() instanceof Arrow)) return;
	    Arrow arrow = (Arrow) event.getProjectile();
	    ItemStack bow = event.getBow();
	    if (bow == null) return;

	    if (!bow.getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey(Main.plugin, PersistentDataValues.RELIC_TYPE.name()), PersistentDataType.STRING, "").equals("SONIC_BOW_RELIC")) return;
	    
	    if (!UsefullFunctions.checkForCooldown((Player) event.getEntity(), Main.plugin.getConfig().getLong("SONIC_BOW_COOLDOWN"), Relic.SONIC_BOW)) return;

	    arrow.setMetadata("sonic_bow", new FixedMetadataValue(Main.plugin, event.getEntity().getEyeLocation()));
	}
	
	@EventHandler
	public void onPickup(EntityPickupItemEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		
		if (Main.relics_equipped == null) return;
		if (!Main.relics_equipped.containsKey(player)) return;
		if (Main.relics_equipped.get(player).isEmpty()) return;
		
		if (Main.relics_equipped.get(player).contains(Relic.RELIC_OF_THE_MINERS_FORTUNE)) {
			
			ItemStack clicked_item = event.getItem().getItemStack();
			
			if (UsefullFunctions.isTool(clicked_item)) {
				
				ItemMeta meta = clicked_item.getItemMeta();
				
				meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
				meta.getPersistentDataContainer().set(new NamespacedKey(Main.plugin, PersistentDataValues.GREEDED_TOOL.name()), PersistentDataType.BOOLEAN, true);
				
				if (meta.getEnchants().containsKey(Enchantment.FORTUNE)) {
					meta.getPersistentDataContainer().set(new NamespacedKey(Main.plugin, PersistentDataValues.PRE_FORTUNE.name()), PersistentDataType.INTEGER, meta.getEnchants().get(Enchantment.FORTUNE));
				}
				
				clicked_item.setItemMeta(meta);
			}
			
		} else {
			
			ItemStack clicked_item = event.getItem().getItemStack();
			
			ItemMeta dropItemMeta = clicked_item.getItemMeta();
			PersistentDataContainer container = dropItemMeta.getPersistentDataContainer();
			
			if (container.has(new NamespacedKey(Main.plugin, PersistentDataValues.GREEDED_TOOL.name()))) {
				dropItemMeta.removeEnchant(Enchantment.VANISHING_CURSE);
				dropItemMeta.removeEnchant(Enchantment.FORTUNE);
				
				if (container.has(new NamespacedKey(Main.plugin, PersistentDataValues.PRE_FORTUNE.name()))) {
					
					dropItemMeta.addEnchant(Enchantment.FORTUNE, container.get(new NamespacedKey(Main.plugin, PersistentDataValues.PRE_FORTUNE.name()), PersistentDataType.INTEGER), true);
					
				}
				
				container.remove(new NamespacedKey(Main.plugin, PersistentDataValues.PRE_FORTUNE.name()));
				container.remove(new NamespacedKey(Main.plugin, PersistentDataValues.GREEDED_TOOL.name()));
				
				clicked_item.setItemMeta(dropItemMeta);
			}
			
			
		}
	}
	
	private void checkAndApplyEffect(Player player) {
		
		if (Main.relics_equipped == null) return;
		if (!Main.relics_equipped.containsKey(player)) return;
		if (Main.relics_equipped.get(player).isEmpty()) return;
		
		if (Main.relics_equipped.get(player).contains(Relic.RELIC_OF_THE_PATHFINDER)) {
			ItemStack item = player.getInventory().getItemInMainHand();
	        if (item != null && item.getType() == Material.TORCH) {
	            if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
	                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
	                Main.night_vision_pathfinder.add(player);
	            }
	        } else {
	        	if (Main.night_vision_pathfinder != null && Main.night_vision_pathfinder.contains(player)) {
	                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
	        	}
	        }
		}
        
    }

    @EventHandler
    public void onItemHeldChange(PlayerItemHeldEvent event) {
    	event.getPlayer().getServer().getScheduler().runTaskLater(Main.plugin, () -> checkAndApplyEffect(event.getPlayer()), 1L);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        // Delay to allow inventory to update
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> checkAndApplyEffect(player), 1L);

        if (Main.relics_equipped == null) return;
        if (!Main.relics_equipped.containsKey(player)) return;
        if (Main.relics_equipped.get(player).isEmpty()) return;

        boolean hasRelic = Main.relics_equipped.get(player).contains(Relic.RELIC_OF_THE_MINERS_FORTUNE);
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // Clone to find it later in the inventory
        ItemStack movedClone = clickedItem.clone();

        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            // Look for the item in the player's inventory (it may have shifted slots)
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack item = player.getInventory().getItem(i);
                if (item == null || item.getType() != movedClone.getType()) continue;
                if (!item.hasItemMeta()) continue;

                ItemMeta meta = item.getItemMeta();
                if (!UsefullFunctions.isTool(item)) continue;

                NamespacedKey greedKey = new NamespacedKey(Main.plugin, PersistentDataValues.GREEDED_TOOL.name());
                NamespacedKey preFortuneKey = new NamespacedKey(Main.plugin, PersistentDataValues.PRE_FORTUNE.name());
                PersistentDataContainer container = meta.getPersistentDataContainer();

                if (hasRelic) {
                    // Add greedy tool effect
                    if (!container.has(greedKey, PersistentDataType.BOOLEAN)) {
                        meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
                        container.set(greedKey, PersistentDataType.BOOLEAN, true);
                        if (meta.hasEnchant(Enchantment.FORTUNE)) {
                            container.set(preFortuneKey, PersistentDataType.INTEGER, meta.getEnchantLevel(Enchantment.FORTUNE));
                        }
                        item.setItemMeta(meta);
                    }
                } else {
                    // Remove greedy tool effect
                    if (container.has(greedKey, PersistentDataType.BOOLEAN)) {
                        meta.removeEnchant(Enchantment.VANISHING_CURSE);
                        meta.removeEnchant(Enchantment.FORTUNE);

                        if (container.has(preFortuneKey)) {
                            int level = container.get(preFortuneKey, PersistentDataType.INTEGER);
                            meta.addEnchant(Enchantment.FORTUNE, level, true);
                        }

                        container.remove(greedKey);
                        container.remove(preFortuneKey);
                        item.setItemMeta(meta);
                    }
                }
            }
        }, 1L);
    }
    
    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
    	
    	if (Main.relics_equipped == null) return;
		if (!Main.relics_equipped.containsKey((Player) event.getViewers().get(0))) return;
		if (Main.relics_equipped.get((Player) event.getViewers().get(0)).isEmpty()) return;
		
		if (Main.relics_equipped.get((Player) event.getViewers().get(0)).contains(Relic.RELIC_OF_THE_MINERS_FORTUNE)) {
			
			if (event.getRecipe() == null) return;
			
			if (UsefullFunctions.isTool(event.getRecipe().getResult())) {
				
				ItemStack crafted_item = event.getRecipe().getResult();
				
				ItemMeta meta = crafted_item.getItemMeta();
				
				meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
				meta.getPersistentDataContainer().set(new NamespacedKey(Main.plugin, PersistentDataValues.GREEDED_TOOL.name()), PersistentDataType.BOOLEAN, true);
				
				if (meta.getEnchants().containsKey(Enchantment.FORTUNE)) {
					meta.getPersistentDataContainer().set(new NamespacedKey(Main.plugin, PersistentDataValues.PRE_FORTUNE.name()), PersistentDataType.INTEGER, meta.getEnchants().get(Enchantment.FORTUNE));
				}
				
				crafted_item.setItemMeta(meta);
				
				event.getInventory().setResult(crafted_item);
			}
			
		}
    }
    
    @EventHandler
    public void onPlayerUseShield(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!Main.relics_equipped.getOrDefault(player, List.of()).contains(Relic.RELIC_OF_THE_FORSAKEN)) return;
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.SHIELD) {
            // Cancel right-click action with shield
            if (event.getAction().toString().contains("RIGHT_CLICK")) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if (ignoreNextEffect.contains(player.getUniqueId())) {
            ignoreNextEffect.remove(player.getUniqueId());
            return;
        }

        if (!Main.relics_equipped.getOrDefault(player, List.of()).contains(Relic.RELIC_OF_THE_ALCHEMIST)) return;

        PotionEffect newEffect = event.getNewEffect();
        if (newEffect == null) return; // Only modify new effects

        event.setCancelled(true); // Cancel original effect application

        int boostedDuration = (int) (newEffect.getDuration() * 1.25);
        PotionEffect boosted = new PotionEffect(
            newEffect.getType(),
            boostedDuration,
            newEffect.getAmplifier(),
            newEffect.isAmbient(),
            newEffect.hasParticles(),
            newEffect.hasIcon()
        );

        ignoreNextEffect.add(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            player.addPotionEffect(boosted);
        }, 1L);
    }
    
    @EventHandler
    public void onSwapHandItems(PlayerSwapHandItemsEvent event) {
    	event.getPlayer().getServer().getScheduler().runTaskLater(Main.plugin, () -> checkAndApplyEffect(event.getPlayer()), 1L);
    }
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		
		if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
		    if (arrow.getShooter() instanceof Player) {
			    if (event.getEntity() instanceof Player) {
				    Player shooter = (Player) arrow.getShooter();
				    
				    if (arrow.hasMetadata("sonic_bow")) {
				        UsefullFunctions.simulateSonicBoomBetweenPlayers(shooter, (Player) event.getEntity());
				        UsefullFunctions.updateCooldown(shooter, Relic.SONIC_BOW);
				    }
			    }

		    }
		    
		}

		
		if (!(event.getDamager() instanceof Player)) return;
		
		Player p = (Player) event.getDamager();
		
        UUID ownerUUID = p.getUniqueId();
        List<UUID> trustedList = new ArrayList<>(Main.trusted_players.getOrDefault(ownerUUID, new ArrayList<>()));
		
		if (Main.relics_equipped == null) return;
		if (Main.relics_equipped.isEmpty()) return;
		if (!Main.relics_equipped.containsKey(p)) return;
		if (!(event.getEntity() instanceof LivingEntity)) return;

		if (Main.player_wolfs.containsKey(p) && (event.getEntity() instanceof Player)) {
			
			List<Wolf> wolfs = Main.player_wolfs.get(p);
			
			for (Wolf wolf : wolfs) {
				if (wolf.getTarget() == null) {
					wolf.setTarget((LivingEntity) event.getEntity());
				}
			}
		}
		
		if (Main.relics_equipped.get(p).contains(Relic.ASHEN_KING)) {
		    LivingEntity damaged = (LivingEntity) event.getEntity();
		    
		    double finalHealth = damaged.getHealth() - event.getFinalDamage();
			if (finalHealth <= 0) {
				
				for (Entity ent : p.getNearbyEntities(8, 8, 8)) {
					
					if (!(ent instanceof LivingEntity)) continue;
					
					LivingEntity livingEnt = (LivingEntity) ent;
					
					if (livingEnt instanceof Player) {
						if (trustedList.contains(((Player) livingEnt).getUniqueId())) continue;
					}
					
					livingEnt.setFireTicks(200);
					livingEnt.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 0, false, false, false));
					
				}
				
			}
			
		}
		
		if (Main.relics_equipped.get(p).contains(Relic.RELIC_OF_INFERNAL_BEAST)) {
			LivingEntity damaged = (LivingEntity) event.getEntity();
			
			if (damaged instanceof Player) {
				double finalHealth = damaged.getHealth() - event.getFinalDamage();
				if (finalHealth <= 0) {
										
					if (Main.beast_mode.contains(p)) {
						p.setHealth(Math.min(p.getHealth() + 0.25 * p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
					}
					
					int lifespan = Main.plugin.getConfig().getInt("HELLHOUND_MINION_LIFESPAN");
					
					Wolf wolf = (Wolf) damaged.getWorld().spawnEntity(damaged.getLocation(), EntityType.WOLF);
					
					wolf.getPersistentDataContainer().set(new NamespacedKey(Main.plugin, PersistentDataValues.HELLHOUND_MINION.name()), PersistentDataType.BOOLEAN, true);
					wolf.getPersistentDataContainer().set(new NamespacedKey(Main.plugin, PersistentDataValues.HELLHOUND_OWNER.name()), PersistentDataType.STRING, p.getUniqueId().toString());

					wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, lifespan * 20, 3));
					
					wolf.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + "HELLHOUND MINION");
					wolf.setCustomNameVisible(true);
					
					wolf.setOwner(p);
					
					List<Wolf> wolfs = new ArrayList<>(Main.player_wolfs.getOrDefault(p, new ArrayList<>()));
					
					if (wolfs.isEmpty()) {
						wolfs.add(wolf);
					}
					
					Main.player_wolfs.put(p, wolfs);
					
					new BukkitRunnable() {
						
						int executes = 0;
						
						@Override
						public void run() {
							
							if (wolf.isDead()) {
								return;
							}
							
							Location center = wolf.getLocation().add(0, 0.5, 0);

					    	for (int i = 1; i <= 50; i++) {
						        double angle = 2 * Math.PI * i / 50;
						        double x = 2 * Math.cos(angle);
						        double z = 2 * Math.sin(angle);
					
						        Location particleLoc = center.clone().add(x, 0, z);

						        p.getWorld().spawnParticle(Particle.FLAME, particleLoc, 0, 0, 0, 0, 1.5);
					    	}
					    	Block blockAt = wolf.getWorld().getBlockAt(wolf.getLocation());
					    	if (blockAt.getType().equals(Material.COBWEB)) {
					    		wolf.getWorld().spawnParticle(Particle.EXPLOSION, wolf.getLocation(), 5, 0.1, 0.1, 0.1);
					    		blockAt.setType(Material.AIR);
					    	}
					    	
					    	if (executes == lifespan) {
					    		List<Wolf> wolfs = new ArrayList<>(Main.player_wolfs.getOrDefault(p, new ArrayList<>()));
					    		wolfs.remove(wolf);
								Main.player_wolfs.put(p, wolfs);
								if (wolfs.isEmpty()) Main.player_wolfs.remove(p);
					    		wolf.remove();
					    		cancel();
					    	}
					    	
					    	executes++;
							
						}
					}.runTaskTimer(Main.plugin, 0, 20);
					
				}
				
			}
		
		   
		}
		
		if (Main.relics_equipped.get(p).contains(Relic.RELIC_OF_SHARPENED_FANG)) {
			int hitAmount = Main.hitAmount.getOrDefault(p, 0);
			if (hitAmount == 2) {
				LivingEntity livingEnt = (LivingEntity) event.getEntity();
				livingEnt.setHealth(Math.max(0, livingEnt.getHealth() - 2));
				hitAmount = -1;
			}
			hitAmount++;
			Main.hitAmount.put(p, hitAmount);
		}
		
	}
	
	@EventHandler
	public void onWolfAttack(EntityDamageByEntityEvent event) {
		
		Entity attacker = event.getDamager();
		
		if (attacker instanceof Wolf) {
			
			Wolf wolf = (Wolf) attacker;
			
			if (wolf.getPersistentDataContainer().has(new NamespacedKey(Main.plugin, PersistentDataValues.HELLHOUND_MINION.name()))) {
				
				if (wolf.getPersistentDataContainer().get(new NamespacedKey(Main.plugin, PersistentDataValues.HELLHOUND_MINION.name()), PersistentDataType.BOOLEAN)) {
					
					if (event.getEntity() instanceof Player) {
						
						Player attacked = (Player) event.getEntity();
						
						attacked.setHealth(Math.max(0, attacked.getHealth() - 2));
												
					}
					
				}
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		
		Player p = (Player) event.getEntity();
		
		if (p.getLastDamageCause().getCause().equals(DamageCause.ENTITY_ATTACK)) {
			if (p.getKiller() instanceof Player) {
				
				Player killer = p.getKiller();
				
				if (Main.oblivion_ability != null) {
					
					if (Main.oblivion_ability.contains(killer)) {
						
						if (Main.voidFormCd != null) {
							if (Main.voidFormCd.containsKey(killer.getUniqueId())) Main.voidFormCd.remove(killer.getUniqueId());
						}
						
						List<ItemStack> relic_items = UsefullFunctions.getRelicItems(p.getInventory());
						
						relic_items.stream().forEach(item -> { 
							p.getWorld().dropItemNaturally(p.getLocation(), item);
							p.getInventory().remove(item);
						});
						
						event.getDrops().clear();
					}
				}
			}
		}
		
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		
		ItemStack itemDrop = event.getItemDrop().getItemStack();
		ItemMeta itemDropMeta = itemDrop.getItemMeta();
		PersistentDataContainer container = itemDropMeta.getPersistentDataContainer();
		
		if (container.has(new NamespacedKey(Main.plugin, PersistentDataValues.GREEDED_TOOL.name()))) {
			itemDropMeta.removeEnchant(Enchantment.VANISHING_CURSE);
			itemDropMeta.removeEnchant(Enchantment.FORTUNE);
			
			if (container.has(new NamespacedKey(Main.plugin, PersistentDataValues.PRE_FORTUNE.name()))) {
				
				itemDropMeta.addEnchant(Enchantment.FORTUNE, container.get(new NamespacedKey(Main.plugin, PersistentDataValues.PRE_FORTUNE.name()), PersistentDataType.INTEGER), true);
				
			}
			
			container.remove(new NamespacedKey(Main.plugin, PersistentDataValues.PRE_FORTUNE.name()));
			container.remove(new NamespacedKey(Main.plugin, PersistentDataValues.GREEDED_TOOL.name()));
			
			itemDrop.setItemMeta(itemDropMeta);
		}
		
	}
	
	@EventHandler
	public void onGrindStone(PrepareGrindstoneEvent event) {
		PersistentDataContainer container = event.getResult().getItemMeta().getPersistentDataContainer();
		if (container.has(new NamespacedKey(Main.plugin, PersistentDataValues.PRE_FORTUNE.name()))) {
			container.remove(new NamespacedKey(Main.plugin, PersistentDataValues.PRE_FORTUNE.name()));
		}
		
	}
	
	@EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		if (Main.relics_equipped == null) return;
		if (Main.relics_equipped.isEmpty()) return;
		if (!Main.relics_equipped.containsKey(player)) return;

		
		if (Main.relics_equipped.get(player).contains(Relic.RELIC_OF_OBLIVION)) {

			Map<UUID, Long> cdMap = Main.voidFormCd;
			
			UUID uuid = player.getUniqueId();
	
		    if (event.isSneaking()) {
		        BukkitRunnable task = new BukkitRunnable() {
		            @Override
		            public void run() {
		    			if (!Main.relics_equipped.get(player).contains(Relic.RELIC_OF_OBLIVION)) return;
		    			if (cdMap != null) {
		    				if (cdMap.containsKey(player.getUniqueId())) {
		    					long time_left = (System.currentTimeMillis() - cdMap.get(player.getUniqueId()))/1000;
		    					if (time_left < Main.plugin.getConfig().getInt("VOIDFORM_COOLDOWN")) {
		    						player.sendMessage(ChatColor.RED + "Ability still on cooldown! Time left: " + ChatColor.GRAY + UsefullFunctions.formatSecondsNicely((int) (Main.plugin.getConfig().getInt("VOIDFORM_COOLDOWN") - time_left)));
		    						return;
		    					}
		    				}
		    			}
		                player.sendMessage(ChatColor.GREEN + "Voidform activated. You're now invisble for " + ChatColor.GRAY + UsefullFunctions.formatSecondsNicely(Main.plugin.getConfig().getInt("VOIDFORM_DURATION")));
		                Main.voidFormCd.put(player.getUniqueId(), System.currentTimeMillis());
		                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Main.plugin.getConfig().getInt("VOIDFORM_DURATION") * 20, 100, false, false, false));
		                List<Player> players = new ArrayList<Player>();
		                for (Player hide_him : Bukkit.getOnlinePlayers()) {
		                	players.add(hide_him);
		                	hide_him.hidePlayer(Main.plugin, player);
		                }
		                sneakingTasks.remove(uuid); 
		                if (Main.voidform_ability_used != null && Main.voidform_ability_used.containsKey(player)) Main.voidform_ability_used.get(player).cancel();
		                BukkitRunnable invis_disable = new BukkitRunnable() {
							
							@Override
							public void run() {
								Main.voidform_ability_used.remove(player);
								for (Player hide_him : players) {
				                	hide_him.showPlayer(Main.plugin, player);
				                }
								player.sendMessage(ChatColor.RED + "Voidform de-activated.");
							}
						};
						invis_disable.runTaskLater(Main.plugin, Main.plugin.getConfig().getInt("VOIDFORM_DURATION") * 20);
		                Main.voidform_ability_used.put(player, invis_disable);
		            }
		        };
		        task.runTaskLater(Main.plugin, 20 * 5); 
		        sneakingTasks.put(uuid, task);
		    } else {
		        if (sneakingTasks.containsKey(uuid)) {
		            sneakingTasks.get(uuid).cancel();
		            sneakingTasks.remove(uuid);
		        }
		    }
		}
    }

	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		
		if (event.getEntity() instanceof Wolf) {
			if (event.getTarget() instanceof Player) {
				if (Main.player_wolfs.getOrDefault((Player) event.getTarget(), List.of()).contains((Wolf) event.getEntity())) {
					event.setCancelled(true);
				}
				if (event.getEntity().getPersistentDataContainer().has(new NamespacedKey(Main.plugin, PersistentDataValues.HELLHOUND_MINION.name()))) {
			        UUID ownerUUID = UUID.fromString(event.getEntity().getPersistentDataContainer().get(new NamespacedKey(Main.plugin, PersistentDataValues.HELLHOUND_OWNER.name()), PersistentDataType.STRING));
			        List<UUID> trustedList = new ArrayList<>(Main.trusted_players.getOrDefault(ownerUUID, new ArrayList<>()));
			        
			        if (trustedList.contains(((Player) event.getTarget()).getUniqueId())) {
			        	event.setCancelled(true);
			        }
				}
			}
		}
		
		if (!(event.getTarget() instanceof Player)) return;
		Player p = (Player) event.getTarget();
		if (Main.relics_equipped == null) return;
		if (!Main.relics_equipped.containsKey(p)) return;
		if (Main.relics_equipped.get(p).isEmpty()) return;
		
		if (Main.relics_equipped.get(p).contains(Relic.RELIC_OF_THE_PATHFINDER)) {
			event.setCancelled(true);
			return;
		}
		
		if (Main.relics_equipped.get(p).contains(Relic.RELIC_OF_THE_ENDSEER) && event.getEntity().getType().equals(EntityType.ENDERMAN)) {
			event.setCancelled(true);
			return;
		}

		Map<Player, BukkitRunnable> voidform_list = Main.voidform_ability_used;
		
		if (voidform_list == null || voidform_list.isEmpty()) return;
		
		if (voidform_list.containsKey(p)) {
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
	    Player player = event.getPlayer();
	    
	    if (Main.relics_equipped == null) return;
		if (Main.relics_equipped.isEmpty()) return;
		if (!Main.relics_equipped.containsKey(player)) return;
	    
		Location from = event.getFrom();
		Location to = event.getTo();
		
		 if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) return;

		
		if (Main.relics_equipped.get(player).contains(Relic.ASHEN_KING)) {
		    Location loc = player.getLocation();
		    World world = player.getWorld();
	
		    int radius = 3;
	
		    for (int x = -radius; x <= radius; x++) {
		        for (int z = -radius; z <= radius; z++) {
		            Location check = loc.clone().add(x, -1, z);
		            Block block = world.getBlockAt(check);
	
		            if (UsefullFunctions.isFullLavaBlock(block)) {
		            	
		                block.setType(Material.OBSIDIAN);
		                Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
		                    if (block.getType() == Material.OBSIDIAN) {
		                        block.setType(Material.LAVA);
		                    }
		                }, 100L); // 5 seconds
		            }
		        }
		    }
		}
	}

}
