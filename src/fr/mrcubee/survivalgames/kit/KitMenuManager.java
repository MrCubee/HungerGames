package fr.mrcubee.survivalgames.kit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;

public class KitMenuManager implements Listener {
	
	private SurvivalGames survivalGames;
	
	public KitMenuManager(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler
	public void inventoryEvent(InventoryClickEvent event) {
		ItemStack itemStack;
		ItemMeta  itemMeta;
		Kit       kit;
		Player    player;
		
		if (!event.getInventory().getName().equals(survivalGames.getGame().getGameSetting().getMenuKitName()))
			return;
		event.setCancelled(true);
		if ((survivalGames.getGame().getGameStats() != GameStats.WAITING) && (survivalGames.getGame().getGameStats() != GameStats.STARTING)) {
			event.getWhoClicked().closeInventory();
			return;
		}
		if (event.getSlot() < 0 || event.getSlot() >= event.getInventory().getSize())
			return;
		itemStack = event.getInventory().getItem(event.getSlot());
		if (itemStack == null)
			return;
		itemMeta = itemStack.getItemMeta();
		kit = survivalGames.getGame().getKitManager().getKitByName(itemMeta.getDisplayName());
		event.getWhoClicked().closeInventory();
		if (kit == null)
			return;
		player = (Player) event.getWhoClicked();
		survivalGames.getGame().getKitManager().removeKit(player);
		kit.addPlayer(player);
		event.getWhoClicked().sendMessage(ChatColor.GOLD + "You took the "+ ChatColor.RED + kit.getName() + ChatColor.GOLD + " kit.");
	}

}
