package fr.mrcubee.hungergames.listeners.player;

import fr.mrcubee.hungergames.HungerGames;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;

import fr.mrcubee.hungergames.GameStats;

public class PlayerPreLogin implements Listener {
	
	private final HungerGames survivalGames;
	
	public PlayerPreLogin(HungerGames survivalGames) {
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
