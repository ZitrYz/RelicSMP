package events;


import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import relics.Relic;

public class RelicEquipEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Relic relic;

	public RelicEquipEvent(Player player, Relic relic) {
        this.player = player;
        this.relic = relic;
    }

    public Player getPlayer() {
        return player;
    }

    public Relic getRelic() {
        return relic;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
