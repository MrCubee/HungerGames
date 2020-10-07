package fr.mrcubee.survivalgames.listeners.player;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;
import fr.mrcubee.survivalgames.kit.Kit;

public class PlayerQuit implements Listener {
	
	private SurvivalGames survivalGames;
	
	public PlayerQuit(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerQuitEvent(PlayerQuitEvent event) {
		GameStats gameStats = this.survivalGames.getGame().getGameStats();
		Set<Player> playerInGame;
		Kit kit;
		String kitName;

		if (this.survivalGames.getGame().isSpectator(event.getPlayer()))
			return;
		this.survivalGames.getGame().addSpectator(event.getPlayer());
		this.survivalGames.getGame().getPluginScoreBoardManager().removePlayerSideBar(event.getPlayer());
		if (gameStats != GameStats.DURING) {
			event.setQuitMessage(ChatColor.RED + "[-] " + event.getPlayer().getName());
			return;
		}
		event.setQuitMessage(null);
		kit = survivalGames.getGame().getKitManager().getKitByPlayer(event.getPlayer());
		kitName = (kit == null) ? "No Kit" : kit.getName();
		//survivalGames.getDataBase().updatefinishPlayerData(event.getPlayer(), false);
		playerInGame = survivalGames.getGame().getPlayerInGame();
		playerInGame.remove(event.getPlayer());
		int players = playerInGame.size();
		if (players > 1) {
			survivalGames.getServer().broadcastMessage(ChatColor.RED + event.getPlayer().getName() + " (" + ChatColor.GRAY + kitName + ChatColor.RED + ")" + ChatColor.GOLD + " is Dead ! There are " + ChatColor.RED + players + " players left.");
			try {
				for (Player player : Bukkit.getOnlinePlayers())
					player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 100, 1);
			} catch (Exception e) {}
		}else {
			try {
				for (Player player : Bukkit.getOnlinePlayers())
					player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 100, 1);
			} catch (Exception e) {}
			survivalGames.getServer().broadcastMessage(ChatColor.RED + event.getPlayer().getName() + " (" + ChatColor.GRAY + kitName + ChatColor.RED + ")" + ChatColor.GOLD + " is Dead ! ");
			for (Player player : playerInGame) {
				//survivalGames.getDataBase().updatefinishPlayerData(player, true);
				survivalGames.getServer().broadcastMessage(ChatColor.GOLD + "---> " + ChatColor.RED + player.getName() + ChatColor.GOLD + " win the game. <---");
			}
		}
		survivalGames.getGame().getKitManager().removeKit(event.getPlayer());
	}
}
