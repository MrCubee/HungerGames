package fr.mrcubee.survivalgames.listeners.inventory;
import org.bukkit.plugin.PluginManager;

import fr.mrcubee.survivalgames.SurvivalGames;

public class RegisterListeners {
	
	public static void register(SurvivalGames survivalGames) {
		PluginManager pluginManager = survivalGames.getServer().getPluginManager();
		
		pluginManager.registerEvents(new InventoryClick(survivalGames), survivalGames);
	}

}
