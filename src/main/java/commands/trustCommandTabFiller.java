package commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;


public class trustCommandTabFiller implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("trust")) {
			
			if (args.length < 2) {
				return List.of("add", "remove", "list");
			}
			
		}
	
		return null;
	}

}