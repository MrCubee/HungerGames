package fr.mrcubee.survivalgames.listeners.player;
import org.bukkit.plugin.PluginManager;

import fr.mrcubee.survivalgames.SurvivalGames;

public class RegisterListeners {
	
	public static void register(SurvivalGames survivalGames) {
		PluginManager pluginManager = survivalGames.getServer().getPluginManager();
		
		pluginManager.registerEvents(new BlockBreak(survivalGames), survivalGames);
		pluginManager.registerEvents(new BlockPlace(survivalGames), survivalGames);
		pluginManager.registerEvents(new FoodLevelChange(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerDeath(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerDropItem(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerInteract(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerJoin(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerMove(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerPickupItem(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerPreLogin(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerQuit(survivalGames), survivalGames);
		pluginManager.registerEvents(new GameStatsEvent(survivalGames), survivalGames);
		pluginManager.registerEvents(new AsyncPlayerChat(), survivalGames);
	}
}
