package fr.mrcubee.survivalgames.listeners.player;

import java.util.ArrayList;

import fr.mrcubee.bukkit.packet.GenericListener;
import fr.mrcubee.bukkit.packet.GenericListenerManager;
import fr.mrcubee.bukkit.player.PlayerUtils;
import fr.mrcubee.survivalgames.Game;
import fr.mrcubee.util.LangUtil;
import fr.mrcubee.util.PlayerUtil;
import fr.mrcubee.util.Reflection;
import net.arkadgames.survivalgame.sql.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
	
	private final SurvivalGames survivalGames;
	private final ItemStack itemKit;
	
	public PlayerJoin(SurvivalGames survivalGames) {
		ItemMeta itemKitMeta;

		this.survivalGames = survivalGames;
		this.itemKit = new ItemStack(Material.NETHER_STAR);
		itemKitMeta = this.itemKit.getItemMeta();
		itemKitMeta.setDisplayName(ChatColor.YELLOW + "Kit");
		itemKitMeta.setLore(new ArrayList<String>());
		this.itemKit.setItemMeta(itemKitMeta);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerJoinEvent(PlayerJoinEvent event) {
 		Game game = this.survivalGames.getGame();
		PlayerData playerData = game.getDataBaseManager().getPlayerData(event.getPlayer().getUniqueId());
		Player player = event.getPlayer();
		GenericListenerManager genericListenerManager = this.survivalGames.getGenericListenerManager();

		LangUtil.updatePlayerLocale(event.getPlayer(), PlayerUtils.getLocale(event.getPlayer()));
		if (genericListenerManager != null)
			genericListenerManager.addPlayer(event.getPlayer());
		game.getPluginScoreBoardManager().getPlayerSideBar(event.getPlayer());
		if (game.getGameStats() == GameStats.DURING) {
			event.setJoinMessage(null);
			player.getInventory().clear();
			game.addSpectator(player);
		} else {
			player.getInventory().clear();
			player.getInventory().addItem(this.itemKit);
			game.addPlayer(event.getPlayer());
			event.setJoinMessage(ChatColor.GREEN + "[+] " + event.getPlayer().getName());
		}
		event.getPlayer().teleport(survivalGames.getGame().getSpawn());
	}
}
