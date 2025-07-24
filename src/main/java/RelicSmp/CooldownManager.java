package RelicSmp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import relics.Relic;

public class CooldownManager {

	public static void saveCooldowns(FileConfiguration config, File file) {
	    // Save per-player relic cooldowns
	    for (UUID uuid : Main.cooldown_map.keySet()) {
	        Map<Relic, Long> relicMap = Main.cooldown_map.get(uuid);
	        for (Map.Entry<Relic, Long> entry : relicMap.entrySet()) {
	            config.set("cooldowns." + uuid + "." + entry.getKey().name(), entry.getValue());
	        }
	    }

	    // Save voidFormCd
	    for (UUID uuid : Main.voidFormCd.keySet()) {
	        config.set("voidFormCd." + uuid, Main.voidFormCd.get(uuid));
	    }

	    // Save global_cooldowns
	    for (Map.Entry<Relic, Long> entry : Main.global_cooldowns.entrySet()) {
	        config.set("global_cooldowns." + entry.getKey().name(), entry.getValue());
	    }

	    try {
	        config.save(file);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public static void loadCooldowns(FileConfiguration config) {
	    Main.cooldown_map.clear();
	    Main.voidFormCd.clear();
	    Main.global_cooldowns.clear();

	    // Load relic cooldowns
	    if (config.isConfigurationSection("cooldowns")) {
	        for (String uuidStr : config.getConfigurationSection("cooldowns").getKeys(false)) {
	            UUID uuid = UUID.fromString(uuidStr);
	            ConfigurationSection relicSection = config.getConfigurationSection("cooldowns." + uuidStr);
	            Map<Relic, Long> relicMap = new HashMap<>();
	            for (String relicName : relicSection.getKeys(false)) {
	                try {
	                    Relic relic = Relic.valueOf(relicName);
	                    long time = relicSection.getLong(relicName);
	                    relicMap.put(relic, time);
	                } catch (IllegalArgumentException e) {
	                    // Unknown relic, skip
	                }
	            }
	            Main.cooldown_map.put(uuid, relicMap);
	        }
	    }

	    // Load voidFormCd
	    if (config.isConfigurationSection("voidFormCd")) {
	        for (String uuidStr : config.getConfigurationSection("voidFormCd").getKeys(false)) {
	            UUID uuid = UUID.fromString(uuidStr);
	            long time = config.getLong("voidFormCd." + uuidStr);
	            Main.voidFormCd.put(uuid, time);
	        }
	    }

	    // Load global_cooldowns
	    if (config.isConfigurationSection("global_cooldowns")) {
	        ConfigurationSection section = config.getConfigurationSection("global_cooldowns");
	        for (String relicName : section.getKeys(false)) {
	            try {
	                Relic relic = Relic.valueOf(relicName);
	                long time = section.getLong(relicName);
	                Main.global_cooldowns.put(relic, time);
	            } catch (IllegalArgumentException e) {
	                // Unknown relic, skip
	            }
	        }
	    }
	}
	
	public static void saveTrustedPlayers() {
	    File file = new File(Main.plugin.getDataFolder(), "trusted.yml");
	    YamlConfiguration config = new YamlConfiguration();

	    for (Map.Entry<UUID, List<UUID>> entry : Main.trusted_players.entrySet()) {
	        List<String> trustedList = entry.getValue().stream()
	            .map(UUID::toString)
	            .toList();
	        config.set(entry.getKey().toString(), trustedList);
	    }

	    try {
	        config.save(file);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void loadTrustedPlayers() {
	    File file = new File(Main.plugin.getDataFolder(), "trusted.yml");
	    if (!file.exists()) return;

	    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

	    for (String key : config.getKeys(false)) {
	        UUID owner = UUID.fromString(key);
	        List<String> list = config.getStringList(key);
	        List<UUID> trusted = list.stream()
	            .map(UUID::fromString)
	            .toList();
	        Main.trusted_players.put(owner, trusted);
	    }
	}

}