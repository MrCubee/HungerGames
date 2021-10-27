package fr.mrcubee.hungergames.listeners.player;

import fr.mrcubee.hungergames.HungerGames;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import fr.mrcubee.hungergames.GameStats;

public class FoodLevelChange implements Listener {
	
	private HungerGames survivalGames;
	
	public FoodLevelChange(HungerGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void foodLevelChangeEvent(FoodLevelChangeEvent event) {
		GameStats gameStats = survivalGames.getGame().getGameStats();
		if (gameStats != GameStats.DURING) {
			event.setFoodLevel(20);
		}
	}
}
