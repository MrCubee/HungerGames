package fr.mrcubee.hungergames.listeners.server;
import org.bukkit.plugin.PluginManager;

import fr.mrcubee.hungergames.HungerGames;

public class RegisterListeners {
	
	public static void register(HungerGames survivalGames) {
		PluginManager pluginManager;

		if (survivalGames == null)
			return;
		pluginManager = survivalGames.getServer().getPluginManager();
		pluginManager.registerEvents(new PingServer(survivalGames), survivalGames);
	}

}
