package commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import RelicSmp.Main;

public class trust implements CommandExecutor {

	public trust() {
		Bukkit.getServer().getPluginCommand("trust").setExecutor(this);
		Bukkit.getServer().getPluginCommand("trust").setTabCompleter(new trustCommandTabFiller());
	}

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        
        Player player = (Player) sender;

        UUID ownerUUID = player.getUniqueId();
        List<UUID> trustedList = new ArrayList<>(Main.trusted_players.getOrDefault(ownerUUID, new ArrayList<>()));

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /trust <add|remove|list> [player]");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
	        case "add":
	            if (args.length < 2) {
	                player.sendMessage(ChatColor.RED + "Usage: /trust add <player>");
	                return true;
	            }
	
	            Player targetAdd = Bukkit.getPlayer(args[1]);
	
	            if (targetAdd == null) {
	                player.sendMessage(ChatColor.RED + "Player not found or not online.");
	                return true;
	            }
	
	            UUID targetAddUUID = targetAdd.getUniqueId();
	
	            if (trustedList.contains(targetAddUUID)) {
	                player.sendMessage(ChatColor.YELLOW + targetAdd.getName() + " is already trusted.");
	                return true;
	            }
	
	            trustedList.add(targetAddUUID);
	            Main.trusted_players.put(ownerUUID, trustedList);
	            player.sendMessage(ChatColor.GREEN + "You have trusted " + targetAdd.getName() + ".");
	            break;
	
	        case "remove":
	            if (args.length < 2) {
	                player.sendMessage(ChatColor.RED + "Usage: /trust remove <player>");
	                return true;
	            }
	
	            Player targetRemove = Bukkit.getPlayer(args[1]);
	
	            if (targetRemove == null) {
	                player.sendMessage(ChatColor.RED + "Player not found or not online.");
	                return true;
	            }
	
	            UUID targetRemoveUUID = targetRemove.getUniqueId();
	
	            if (!trustedList.contains(targetRemoveUUID)) {
	                player.sendMessage(ChatColor.YELLOW + targetRemove.getName() + " is not in your trusted list.");
	                return true;
	            }
	
	            trustedList.remove(targetRemoveUUID);
	            Main.trusted_players.put(ownerUUID, trustedList);
	            player.sendMessage(ChatColor.GREEN + "You have removed " + targetRemove.getName() + " from your trusted list.");
	            break;
	
	        case "list":
	            if (trustedList.isEmpty()) {
	                player.sendMessage(ChatColor.YELLOW + "You don't trust anyone yet.");
	                return true;
	            }
	
	            player.sendMessage(ChatColor.GOLD + "Trusted Players:");
	            for (UUID uuid : trustedList) {
	                OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
	                player.sendMessage(ChatColor.GRAY + "- " + op.getName());
	            }
	            break;
	
	        default:
	            player.sendMessage(ChatColor.RED + "Unknown subcommand. Use: add, remove, or list");
	            break;
	    }
        return false;
	}
}
