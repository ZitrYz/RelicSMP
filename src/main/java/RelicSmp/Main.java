package RelicSmp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import relics.Relic;

public class Main extends JavaPlugin {
	
	public static boolean can_execute_code = false;
	
	public static Map<UUID, List<UUID>> trusted_players = new HashMap<UUID, List<UUID>>();
	
	public static Map<Relic, Long> global_cooldowns = new HashMap<Relic, Long>();
	public static Map<UUID, Long> voidFormCd = new HashMap<UUID, Long>();
	public static Map<UUID, Map<Relic, Long>> cooldown_map = new HashMap<UUID, Map<Relic, Long>>();
	public static Map<Player, List<Relic>> relics_equipped = new HashMap<Player, List<Relic>>();
	public static Map<Player, Integer> hitAmount = new HashMap<Player, Integer>();
	public static Map<Player, BukkitRunnable> voidform_ability_used = new HashMap<Player, BukkitRunnable>();
	public static Map<Player, List<Wolf>> player_wolfs = new HashMap<Player, List<Wolf>>();
	
	public static List<Player> night_vision_pathfinder = new ArrayList<Player>();
	public static List<Player> pheonix_ability_used = new ArrayList<Player>();
	public static List<Player> oblivion_ability = new ArrayList<Player>();
	public static List<Player> beast_mode = new ArrayList<Player>();
	
	public static Plugin plugin;

	public void onEnable() {
		
		saveDefaultConfig();
		
		plugin = getServer().getPluginManager().getPlugin(this.getDataFolder().getName());
		
		new Initialize();
		
	}
	
	public void onDisable() {
		
		for (Player p : voidform_ability_used.keySet()) {
			voidform_ability_used.get(p).runTask(Main.plugin);
		}
	    File file = new File(getDataFolder(), "cooldowns.yml");
	    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
	    CooldownManager.saveCooldowns(config, file);
		CooldownManager.saveTrustedPlayers();
		
		for (Player p : player_wolfs.keySet()) {
			
			for (Wolf wolf : player_wolfs.get(p)) {
				if (!wolf.isDead()) {
					wolf.remove();
				}
			}
			
		}
		
	}


}
