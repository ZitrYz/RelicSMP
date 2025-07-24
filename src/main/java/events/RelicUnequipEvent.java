package events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffectType;

import relics.Relic;

public class RelicUnequipEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Relic relic;

    public RelicUnequipEvent(Player player, Relic relic) {
        this.player = player;
        this.relic = relic;
        
        if (relic.equals(Relic.RELIC_OF_THE_PATHFINDER)) {
        	player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
        
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