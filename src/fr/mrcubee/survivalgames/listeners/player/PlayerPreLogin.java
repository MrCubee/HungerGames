package fr.mrcubee.survivalgames.listeners.player;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;

import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;

public class PlayerPreLogin implements Listener {
	
	private final SurvivalGames survivalGames;
	
	public PlayerPreLogin(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerPreLoginEvent(PlayerPreLoginEvent event) {
		GameStats gameStats = survivalGames.getGame().getGameStats();
		
		if (gameStats == GameStats.OPENING) {
			event.setKickMessage(ChatColor.RED + "Starting the server please wait.");
			event.setResult(Result.KICK_OTHER);
			return;
		}
		if (gameStats == GameStats.STOPPING || gameStats == GameStats.CLOSING) {
			event.setKickMessage(ChatColor.RED + "Stopping the server please wait.");
			event.setResult(Result.KICK_OTHER);
			return;
		}
	}
}
