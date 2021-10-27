package fr.mrcubee.hungergames.listeners.player;

import fr.mrcubee.hungergames.kit.KitManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import fr.mrcubee.hungergames.GameStats;
import fr.mrcubee.hungergames.HungerGames;

public class PlayerDropItem implements Listener {
	
	private HungerGames survivalGames;
	
	public PlayerDropItem(HungerGames survivalGames) {
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
