package commands;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import RelicSmp.Main;
import RelicSmp.UsefullFunctions;

public class restartCooldowns implements CommandExecutor {

	public restartCooldowns() {
		Bukkit.getServer().getPluginCommand("restartCooldowns").setExecutor(this);
		Bukkit.getServer().getPluginCommand("restartCooldowns").setTabCompleter(new restartCooldownTabCompleter());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!UsefullFunctions.checkIfCanUse()) {
			return false;
		}

		if (command.getName().equalsIgnoreCase("restartCooldowns")) {
			
			Player p = (Player) sender;
			
			if (!p.isOp()) {
				p.sendMessage("§cYou dont have permissions to use that command!");
				return false;
			}
			
			if (args.length < 1) {
				p.sendMessage("§cProper usage: /restartCooldowns <player | restart>");
				return false;
			}
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
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);

			if (args[0].equals("restart")) {
				Main.cooldown_map.clear();
				Main.global_cooldowns.clear();
				Main.voidFormCd.clear();
				for (String key : config.getKeys(false)) {
				    config.set(key, null);
				}

				try {
				    config.save(file);
				} catch (IOException e) {
				    e.printStackTrace();
				}
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			UUID targetUUID = target.getUniqueId();
			if (Main.cooldown_map.containsKey(targetUUID)) {
				Main.cooldown_map.remove(targetUUID);
			}

			config.set("cooldowns." + targetUUID, null);
		}
		
		return false;
	}

}
