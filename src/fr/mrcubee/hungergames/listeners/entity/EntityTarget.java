package fr.mrcubee.hungergames.listeners.entity;

import fr.mrcubee.hungergames.HungerGames;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import fr.mrcubee.hungergames.GameStats;

public class EntityTarget implements Listener {
	
	private HungerGames survivalGames;
	
	public EntityTarget(HungerGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler
	public void entityTargetEvent(EntityTargetEvent event) {
		if (survivalGames.getGame().getGameStats() != GameStats.DURING)
			event.setCancelled(true);
	}
}
