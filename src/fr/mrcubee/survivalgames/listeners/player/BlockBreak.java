package fr.mrcubee.survivalgames.listeners.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;

public class BlockBreak implements Listener {
	
	private SurvivalGames survivalGames;
	
	public BlockBreak(SurvivalGames survivalGames) {
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
