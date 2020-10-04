package fr.mrcubee.survivalgames;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import fr.mrcubee.survivalgames.api.event.GameStatsChangeEvent;

public class SurvivalGamesAPI {
	
	private static SurvivalGamesAPI      instance;
	private Game                         game;
	protected List<GameStatsChangeEvent> gameListeners;
	
	protected SurvivalGamesAPI(Game game) {
		this.game = game;
		this.gameListeners = new ArrayList<GameStatsChangeEvent>();
		instance = this;
	}
	
	public void registerGameListener(GameStatsChangeEvent gameListener) {
		if ((gameListener == null) || gameListeners.contains(gameListener))
			return;
		gameListeners.add(gameListener);
	}
	
	public void unRegisterGameListener(GameStatsChangeEvent gameListener) {
		if ((gameListener == null) || (!gameListeners.contains(gameListener)))
			return;
		gameListeners.remove(gameListener);
	}
	
	public Game getGame() {
		return game;
	}
	
	public static SurvivalGamesAPI getInstance() {
		return instance;
	}

}
