package fr.mrcubee.survivalgames.listeners.player;

import fr.mrcubee.survivalgames.Game;
import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class AsyncPlayerChat implements Listener {

	private final SurvivalGames survivalGames;

	public AsyncPlayerChat(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler
	public void asyncPlayerChat(AsyncPlayerChatEvent event) {
		Game game;

		game = this.survivalGames.getGame();
		if (!game.isSpectator(event.getPlayer()))
			return;
		event.setCancelled(true);
		this.survivalGames.getServer().getOnlinePlayers().stream().filter(game::isSpectator).forEach(player -> {
			String message = String.format(event.getFormat(), event.getPlayer().getName(), event.getMessage());

			player.sendMessage(ChatColor.GRAY + message);
		});
	}
	
}
