package commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import RelicSmp.Main;
import net.md_5.bungee.api.ChatColor;
import relics.Relic;

public class ability2 implements CommandExecutor {

	public ability2() {
		Bukkit.getServer().getPluginCommand("ability2").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) return false;
		
		Player p = (Player) sender;
		
		List<Relic> relic_list = Main.relics_equipped.get(p);
		
		if (relic_list == null) {
			p.sendMessage(ChatColor.RED + "You don't have any ability at all!");
			return false;
		}
		if (relic_list.size() < 2) {
			p.sendMessage(ChatColor.RED + "You don't have any relic in 2st slot!");
			return false;
		}
		if (relic_list.get(1).getAbility() == null) {
			p.sendMessage(ChatColor.RED + "Relic in 2st slot doesn't have any active abilities!");
			return false;
		}
		Relic relic = relic_list.get(1);
		if (relic.getAbility().execute(p)) {
			Map<Relic, Long> cdMap = Main.cooldown_map.get(p.getUniqueId());
			
			if (cdMap == null) {
				cdMap = new HashMap<Relic, Long>();
			}
			
			cdMap.put(relic, System.currentTimeMillis());
			if (Main.plugin.getConfig().getInt(relic.toString() + "_COOLDOWN") >= 3600) {
				Main.global_cooldowns.put(relic, System.currentTimeMillis());
		
			}
			Main.cooldown_map.put(p.getUniqueId(), cdMap);
			return true;
		}
		return false;
	}

}
