package fr.mrcubee.survivalgames.listeners.entity;
import org.bukkit.plugin.PluginManager;

import fr.mrcubee.survivalgames.SurvivalGames;

public class RegisterListeners {
	
	public static void register(SurvivalGames survivalGames) {
		PluginManager pluginManager;

		if (survivalGames == null)
			return;
		pluginManager = survivalGames.getServer().getPluginManager();
		pluginManager.registerEvents(new EntityDamage(survivalGames), survivalGames);
		pluginManager.registerEvents(new EntityDamageByEntity(survivalGames), survivalGames);
		pluginManager.registerEvents(new EntityTarget(survivalGames), survivalGames);
	}
}
