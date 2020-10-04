package fr.mrcubee.survivalgames.listeners.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;

public class EntityTarget implements Listener {
	
	private SurvivalGames survivalGames;
	
	public EntityTarget(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler
	public void entityTargetEvent(EntityTargetEvent event) {
		if (survivalGames.getGame().getGameStats() != GameStats.DURING)
			event.setCancelled(true);
	}
}
