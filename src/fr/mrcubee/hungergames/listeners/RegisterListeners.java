package fr.mrcubee.hungergames.listeners;

import fr.mrcubee.hungergames.HungerGames;
import org.bukkit.plugin.PluginManager;

public class RegisterListeners {
	
	public static void register(HungerGames survivalGames) {
		PluginManager pluginManager;

		if (survivalGames == null)
			return;
		pluginManager = survivalGames.getServer().getPluginManager();
		fr.mrcubee.hungergames.listeners.block.RegisterListeners.register(survivalGames);
		fr.mrcubee.hungergames.listeners.entity.RegisterListeners.register(survivalGames);
		fr.mrcubee.hungergames.listeners.packet.RegisterListeners.register(survivalGames);
		fr.mrcubee.hungergames.listeners.player.RegisterListeners.register(survivalGames);
		fr.mrcubee.hungergames.listeners.server.RegisterListeners.register(survivalGames);
		fr.mrcubee.hungergames.listeners.inventory.RegisterListeners.register(survivalGames);
		fr.mrcubee.hungergames.listeners.game.RegisterListeners.register(survivalGames);
	}

}
