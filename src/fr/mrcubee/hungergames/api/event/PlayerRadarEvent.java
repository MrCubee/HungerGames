package fr.mrcubee.hungergames.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRadarEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player radar;
    private final Player target;
    private Location targetLocation;
    private boolean cancel;

    public PlayerRadarEvent(Player radar, Player target) {
        this.radar = radar;
        this.target = target;
        this.targetLocation = target.getLocation();
        this.cancel = false;
    }

    public Player getRadar() {
        return radar;
    }

    public Player getTarget() {
        return target;
    }

    public void setTargetLocation(Location targetLocation) {
        if (targetLocation == null
                || (this.targetLocation != null && !this.targetLocation.getWorld().equals(targetLocation.getWorld())))
            return;
        this.targetLocation = targetLocation.clone();
    }

    public Location getTargetLocation() {
        return targetLocation.clone();
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
