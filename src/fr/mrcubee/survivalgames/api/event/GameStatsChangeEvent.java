package fr.mrcubee.survivalgames.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.mrcubee.survivalgames.GameStats;

public class GameStatsChangeEvent extends Event {
	
	private static final HandlerList HANDLERS = new HandlerList();
	private GameStats gameStats;
	
	public GameStatsChangeEvent(GameStats gameStats) {
		this.gameStats = gameStats;
	}
	
	public GameStats getGameStats() {
		return gameStats;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
	
}
