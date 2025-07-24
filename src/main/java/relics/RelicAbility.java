package relics;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface RelicAbility {
	boolean execute(Player player);
}
