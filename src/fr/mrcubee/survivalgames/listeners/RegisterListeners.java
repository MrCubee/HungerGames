package fr.mrcubee.survivalgames.listeners;

import org.bukkit.plugin.PluginManager;

import fr.mrcubee.survivalgames.SurvivalGames;

public class RegisterListeners {
	
	public static void register(SurvivalGames survivalGames) {
		PluginManager pluginManager;

		if (survivalGames == null)
			return;
		pluginManager = survivalGames.getServer().getPluginManager();
		fr.mrcubee.survivalgames.listeners.block.RegisterListeners.register(survivalGames);
		fr.mrcubee.survivalgames.listeners.entity.RegisterListeners.register(survivalGames);
		fr.mrcubee.survivalgames.listeners.packet.RegisterListeners.register(survivalGames);
		fr.mrcubee.survivalgames.listeners.player.RegisterListeners.register(survivalGames);
		fr.mrcubee.survivalgames.listeners.server.RegisterListeners.register(survivalGames);
		fr.mrcubee.survivalgames.listeners.inventory.RegisterListeners.register(survivalGames);
		fr.mrcubee.survivalgames.listeners.game.RegisterListeners.register(survivalGames);
	}

}
