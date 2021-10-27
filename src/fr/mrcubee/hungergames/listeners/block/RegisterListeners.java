package fr.mrcubee.hungergames.listeners.block;

import fr.mrcubee.hungergames.HungerGames;
import org.bukkit.plugin.PluginManager;

public class RegisterListeners {
	
	public static void register(HungerGames survivalGames) {
		PluginManager pluginManager;

		if (survivalGames == null)
			return;
		pluginManager = survivalGames.getServer().getPluginManager();
		pluginManager.registerEvents(new BlockPhysics(survivalGames), survivalGames);

	}
}
