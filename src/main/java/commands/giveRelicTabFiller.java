package commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import relics.Relic;

public class giveRelicTabFiller implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("giveRelic")) {
			
			if (args.length < 2) {
				return Arrays.asList(Relic.values()).stream().map(Enum::toString).collect(Collectors.toList());
			}
			
		}
	
		return null;
	}

}
