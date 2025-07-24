package commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class restartCooldownTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("restartcooldowns")) {
			if (args.length < 2) {
				
				return Arrays.asList("restart");
				
			}
		}
		
		return null;
	}

}
