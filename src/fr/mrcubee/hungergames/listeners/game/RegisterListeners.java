package fr.mrcubee.hungergames.listeners.game;

import fr.mrcubee.hungergames.HungerGames;
import org.bukkit.plugin.PluginManager;

public final class RegisterListeners {
	
	public static void register(HungerGames survivalGames) {
		PluginManager pluginManager;

		if (survivalGames == null)
			return;
		pluginManager = survivalGames.getServer().getPluginManager();
		pluginManager.registerEvents(new GameStatsChange(survivalGames), survivalGames);
	}

}
