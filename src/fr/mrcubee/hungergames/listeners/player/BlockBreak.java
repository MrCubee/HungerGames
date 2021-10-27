package fr.mrcubee.hungergames.listeners.player;

import fr.mrcubee.hungergames.HungerGames;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.mrcubee.hungergames.GameStats;

public class BlockBreak implements Listener {
	
	private final HungerGames survivalGames;
	
	public BlockBreak(HungerGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void blockBreakEvent(BlockBreakEvent event) {
		GameStats gameStats = survivalGames.getGame().getGameStats();

		if (gameStats != GameStats.DURING) {
			event.setCancelled(true);
		}
	}
}
