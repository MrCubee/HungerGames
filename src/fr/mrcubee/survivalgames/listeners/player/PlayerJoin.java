package fr.mrcubee.survivalgames.listeners.player;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;

public class PlayerJoin implements Listener {
	
	private SurvivalGames survivalGames;
	
	public PlayerJoin(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	private ItemStack createKitItem() {
		ItemStack itemStackKit = new ItemStack(Material.NETHER_STAR);
		ItemMeta  itemMetaKit = itemStackKit.getItemMeta();
		
		itemMetaKit.setDisplayName(ChatColor.YELLOW + "Kit");
		itemMetaKit.setLore(new ArrayList<String>());
		itemStackKit.setItemMeta(itemMetaKit);
		return itemStackKit;
	}
	
	private void setupSpectator(Player player) {
		survivalGames.getGame().addSpectator(player);
	}
	
	private void setupWaitingPlayer(Player player) {
		player.getInventory().clear();
		player.getInventory().addItem(createKitItem());
		this.survivalGames.getGame().addPlayer(player);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerJoinEvent(PlayerJoinEvent event) {
		GameStats gameStats = survivalGames.getGame().getGameStats();
		
		survivalGames.getGame().getPluginScoreBoardManager().addPlayer(event.getPlayer());
		if (gameStats == GameStats.DURING) {
			event.setJoinMessage(null);
			setupSpectator(event.getPlayer());
		} else {
			setupWaitingPlayer(event.getPlayer());
			event.setJoinMessage(ChatColor.GREEN + "[+] " + event.getPlayer().getName());
		}
		event.getPlayer().teleport(survivalGames.getGame().getSpawn());
	}
}
