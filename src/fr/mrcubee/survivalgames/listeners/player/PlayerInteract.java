package fr.mrcubee.survivalgames.listeners.player;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;
import fr.mrcubee.survivalgames.kit.KitMenu;
import fr.mrcubee.survivalgames.radar.PlayerRadar;

public class PlayerInteract implements Listener {
	
	private SurvivalGames survivalGames;

	public PlayerInteract(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void playerInteractEvent(PlayerInteractEvent event) {
		GameStats gameStats = survivalGames.getGame().getGameStats();

		if ((!event.getAction().equals(Action.RIGHT_CLICK_AIR)) && (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			return;
		if (event.getItem() == null)
			return;
		if ((gameStats == GameStats.WAITING) || (gameStats == GameStats.STARTING)) {
			Inventory inventory = KitMenu.getInventory(0);
			if (inventory != null)
				event.getPlayer().openInventory(inventory);
			return;
		}
		if ((gameStats != GameStats.DURING) || (!event.getItem().getType().equals(Material.COMPASS)))
			return;
		event.setCancelled(true);
		PlayerRadar.radar(survivalGames.getGame(), event.getPlayer());
	}
}
