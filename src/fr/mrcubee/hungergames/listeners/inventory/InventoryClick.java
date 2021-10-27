package fr.mrcubee.hungergames.listeners.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.mrcubee.hungergames.GameStats;
import fr.mrcubee.hungergames.HungerGames;

public class InventoryClick implements Listener {
	
	private HungerGames survivalGames;
	
	public InventoryClick(HungerGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void inventoryClickEvent(InventoryClickEvent event) {
		GameStats gameStats = survivalGames.getGame().getGameStats();

		if (gameStats != GameStats.DURING)
			event.setCancelled(true);
	}
}
