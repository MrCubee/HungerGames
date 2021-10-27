package fr.mrcubee.hungergames.listeners.player;

import fr.mrcubee.fastgui.inventory.FastInventory;
import fr.mrcubee.hungergames.Game;
import fr.mrcubee.hungergames.HungerGames;
import fr.mrcubee.hungergames.kit.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.mrcubee.hungergames.GameStats;
import fr.mrcubee.hungergames.radar.PlayerRadar;

public class PlayerInteract implements Listener {
	
	private HungerGames survivalGames;

	public PlayerInteract(HungerGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void playerInteractEvent(PlayerInteractEvent event) {
		Game game = this.survivalGames.getGame();
		KitManager kitManager = game.getKitManager();
		GameStats gameStats = game.getGameStats();
		FastInventory[] inventories;

		if ((!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		|| event.getItem() == null)
			return;
		if ((gameStats == GameStats.WAITING) || (gameStats == GameStats.STARTING)) {
			inventories = kitManager.getKitInventories();
			if (inventories != null && inventories.length > 0)
				inventories[0].openInventory(event.getPlayer(), ChatColor.YELLOW + "Kit");
			return;
		}
		if (gameStats != GameStats.DURING || !event.getItem().isSimilar(game.getKitManager().getRadarItem()))
			return;
		event.setCancelled(true);
		PlayerRadar.radar(survivalGames.getGame(), event.getPlayer());
	}
}
