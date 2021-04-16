package fr.mrcubee.survivalgames.listeners.packet;

import fr.mrcubee.survivalgames.SurvivalGames;
import fr.mrcubee.survivalgames.listeners.inventory.InventoryClick;
import org.bukkit.plugin.PluginManager;

public class RegisterListeners {
	
	public static void register(SurvivalGames survivalGames) {
		PluginManager pluginManager;

		if (survivalGames == null)
			return;
		pluginManager = survivalGames.getServer().getPluginManager();
		pluginManager.registerEvents(new PacketReceived(), survivalGames);
	}

}
