package fr.mrcubee.survivalgames.listeners.game;

import fr.mrcubee.survivalgames.SurvivalGames;
import org.bukkit.plugin.PluginManager;

public final class RegisterListeners {
	
	public static void register(SurvivalGames survivalGames) {
		PluginManager pluginManager = survivalGames.getServer().getPluginManager();
		
		pluginManager.registerEvents(new GameStatsChange(survivalGames), survivalGames);
	}

}
