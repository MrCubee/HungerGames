package fr.mrcubee.hungergames.listeners.player;

import fr.mrcubee.hungergames.HungerGames;
import org.bukkit.plugin.PluginManager;

public final class RegisterListeners {
	
	public static void register(HungerGames survivalGames) {
		PluginManager pluginManager;

		if (survivalGames == null)
			return;
		pluginManager = survivalGames.getServer().getPluginManager();
		pluginManager.registerEvents(new BlockBreak(survivalGames), survivalGames);
		pluginManager.registerEvents(new BlockPlace(survivalGames), survivalGames);
		pluginManager.registerEvents(new FoodLevelChange(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerDeath(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerDropItem(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerInteract(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerJoin(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerLogin(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerMove(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerPickupItem(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerPreLogin(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerQuit(survivalGames), survivalGames);
		pluginManager.registerEvents(new PlayerCommandPreprocess(survivalGames), survivalGames);
		pluginManager.registerEvents(new AsyncPlayerChat(survivalGames), survivalGames);
	}
}
