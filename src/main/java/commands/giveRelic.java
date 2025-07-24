package commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import RelicSmp.UsefullFunctions;
import relics.Relic;

public class giveRelic implements CommandExecutor {
	
	public giveRelic() {
		Bukkit.getServer().getPluginCommand("giveRelic").setExecutor(this);
		Bukkit.getServer().getPluginCommand("giveRelic").setTabCompleter(new giveRelicTabFiller());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!UsefullFunctions.checkIfCanUse()) {
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command is accessible for players only!");
			return false;
		}
		
		if (command.getName().equalsIgnoreCase("giveRelic")) {
			
			Player p = (Player) sender;
			
			if (!p.isOp()) {
				p.sendMessage("§cYou dont have permissions to use that command!");
				return false;
			}
			
			if (args.length < 1) {
				p.sendMessage("§cProper usage: /giveRelic <relic_name>");
				return false;
			}
			
			Relic toAddRelic = Relic.valueOf(args[0]);
			
			if (toAddRelic == null) {
				p.sendMessage("§cNo relic with that name!");
				return false;
			}
			
			p.getInventory().addItem(toAddRelic.getRelicItem());
			return true;
		}
		
		return false;
	}

}
