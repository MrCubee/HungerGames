package fr.mrcubee.survivalgames.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRadarEvent extends Event implements Cancellable {
	
	private static final HandlerList HANDLERS = new HandlerList();
	private Player                   radar;
	private Player                   target;
	private boolean                  cancel;
	
	public PlayerRadarEvent(Player radar, Player target) {
		this.radar = radar;
		this.target = target;
		this.cancel = false;
	}
	
	public Player getRadar() {
		return radar;
	}
	
	public Player getTarget() {
		return target;
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
