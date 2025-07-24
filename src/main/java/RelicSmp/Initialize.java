package RelicSmp;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import commands.ability1;
import commands.ability2;
import commands.giveRelic;
import commands.restartCooldowns;
import commands.trust;
import events.AbilitiesHandler;
import events.EquipRelicHandlers;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import relics.Relic;


public class Initialize {

	public Initialize() {
		
		File file = new File(Main.plugin.getDataFolder(), "cooldowns.yml");
	    if (!file.exists()) {
	    	if (!file.exists()) {
	    	    try {
	    	        file.createNewFile();
	    	    } catch (IOException e) {
	    	        e.printStackTrace();
	    	    }
	    	}
	    }

	    FileConfiguration configf = YamlConfiguration.loadConfiguration(file);
	    CooldownManager.loadCooldowns(configf);
		CooldownManager.loadTrustedPlayers();
	    
		//Anti-scam thingy
		Main.can_execute_code = UsefullFunctions.checkIfCanExecute();
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Main.can_execute_code = UsefullFunctions.checkIfCanExecute();
			}
		}.runTaskTimer(Main.plugin, 0, 20 * 60 * 15);
		
		//Config
		FileConfiguration config = Main.plugin.getConfig();
		
		config.addDefault("ASHEN_KING_COOLDOWN", 60 * 60);
		config.addDefault("HERMES_SANDALS_COOLDOWN", 45);
		config.addDefault("RELIC_OF_INFERNAL_BEAST_COOLDOWN", 60 * 5);
		config.addDefault("RELIC_OF_OBLIVION_COOLDOWN", 60 * 60);
		config.addDefault("RELIC_OF_THE_MINERS_FORTUNE_COOLDOWN", 60 * 5);
		config.addDefault("RELIC_OF_THE_WANDERING_STAR", 60*60*24);
		config.addDefault("SONIC_BOW_COOLDOWN", 90);
		config.addDefault("VOIDFORM_COOLDOWN", 60 * 2);
		config.addDefault("VOIDFORM_DURATION", 8);
		config.addDefault("OBLIVION_DURATION", 30);
		config.addDefault("SONIC_BOW_DAMAGE", 8);
		config.addDefault("HELLHOUND_MINION_LIFESPAN", 60);
		
		config.addDefault("INFERNAL_BEAST_EFFECTS.strength", 3);
		config.addDefault("INFERNAL_BEAST_EFFECTS.absorption", 2);
		config.addDefault("INFERNAL_BEAST_EFFECTS.resistance", 1);
		
		config.options().copyDefaults(true);
		Main.plugin.saveConfig();
		
		//Listeners
		new EquipRelicHandlers();
		new AbilitiesHandler();
		
		//Commands
		new giveRelic();
		new ability1();
		new ability2();
		new restartCooldowns();
		new trust();
		
		//Scheduled tasks
		startSchedulers();
		
		RelicPassiveManager.startRelicTick();
	}
	
	private void startSchedulers() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if (!UsefullFunctions.checkIfCanUse()) {
					return;
				}
				
				if (!Main.relics_equipped.isEmpty()) {
					for (Player p : Main.relics_equipped.keySet()) {
						
						String message_to_send = "";
						int index = 1;
						int size = Main.relics_equipped.get(p).size();
						
						for (Relic relic : Main.relics_equipped.get(p)) {
							
							int since = getTimeSinceLastUse(p, relic);

							int cooldown = Main.plugin.getConfig().getInt(relic.toString() + "_COOLDOWN");
							
							if (index == size) {
								message_to_send = relic.getAbility() != null || relic.equals(Relic.ASHEN_KING) ? message_to_send + relic.getName() + " " + ChatColor.GRAY + (since < 0 || since >= cooldown ? "§a§lREADY!" : UsefullFunctions.formatSecondsNicely(cooldown - since)) : message_to_send + relic.getName();
							} else {
								message_to_send = (relic.getAbility() != null || relic.equals(Relic.ASHEN_KING) ? message_to_send + relic.getName() + " " + ChatColor.GRAY + (since < 0 || since >= cooldown ? "§a§lREADY!" : UsefullFunctions.formatSecondsNicely(cooldown - since)) : message_to_send + relic.getName()) + " §7| ";
							}
								
							index++;
							
						}
						
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message_to_send));
						
					}
				}
				
			}
		}.runTaskTimer(Main.plugin, 0, 20);
		
	}
	
	private int getTimeSinceLastUse(Player player, Relic relic) {
	    if (!Main.cooldown_map.containsKey(player.getUniqueId())) {
	        return -1; // Player has no entries
	    }

	    Map<Relic, Long> relicMap = Main.cooldown_map.get(player.getUniqueId());
	    if (!relicMap.containsKey(relic)) {
	        return -1; // Relic hasn't been used by this player
	    }

	    long lastUsed = relicMap.get(relic);
	    return (int) (((System.currentTimeMillis() - lastUsed)/1000));
	}

}
