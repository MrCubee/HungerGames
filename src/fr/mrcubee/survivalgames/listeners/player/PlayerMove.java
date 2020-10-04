package fr.mrcubee.survivalgames.listeners.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.mrcubee.survivalgames.SurvivalGames;

public class PlayerMove implements Listener {
	
	private SurvivalGames survivalGames;
	
	public PlayerMove(SurvivalGames survivalGames) {
		this.survivalGames = this.survivalGames;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void playerMoveEvent(PlayerMoveEvent event) {
		if (event.getPlayer().isOp())
			return;
		if (event.getPlayer().getFlySpeed() > 3)
			event.getPlayer().setFlySpeed(3);
	}
}
