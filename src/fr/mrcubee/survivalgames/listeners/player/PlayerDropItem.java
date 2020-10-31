package fr.mrcubee.survivalgames.listeners.player;

import fr.mrcubee.survivalgames.kit.KitManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;

public class PlayerDropItem implements Listener {
	
	private SurvivalGames survivalGames;
	
	public PlayerDropItem(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerDropItemEvent(PlayerDropItemEvent event) {
		KitManager kitManager = survivalGames.getGame().getKitManager();
		GameStats gameStats = survivalGames.getGame().getGameStats();

		if (gameStats != GameStats.DURING) {
			event.setCancelled(true);
		} else if (!kitManager.canLostItem(event.getPlayer(), event.getItemDrop().getItemStack()))
			event.setCancelled(true);
	}
}
