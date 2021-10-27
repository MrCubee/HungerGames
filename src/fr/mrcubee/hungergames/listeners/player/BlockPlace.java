package fr.mrcubee.hungergames.listeners.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.mrcubee.hungergames.GameStats;
import fr.mrcubee.hungergames.HungerGames;

public class BlockPlace implements Listener {
	
	private HungerGames survivalGames;
	
	public BlockPlace(HungerGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void blockPlaceEvent(BlockPlaceEvent event) {
		GameStats gameStats = survivalGames.getGame().getGameStats();
		if (gameStats != GameStats.DURING) {
			event.setCancelled(true);
		}
	}
}
