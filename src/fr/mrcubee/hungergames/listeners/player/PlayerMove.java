package fr.mrcubee.hungergames.listeners.player;

import fr.mrcubee.hungergames.HungerGames;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {
	
	private HungerGames survivalGames;
	
	public PlayerMove(HungerGames survivalGames) {
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
