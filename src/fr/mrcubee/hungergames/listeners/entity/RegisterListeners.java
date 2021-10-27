package fr.mrcubee.hungergames.listeners.entity;
import fr.mrcubee.hungergames.HungerGames;
import org.bukkit.plugin.PluginManager;

public class RegisterListeners {
	
	public static void register(HungerGames survivalGames) {
		PluginManager pluginManager;

		if (survivalGames == null)
			return;
		pluginManager = survivalGames.getServer().getPluginManager();
		pluginManager.registerEvents(new EntityDamage(survivalGames), survivalGames);
		pluginManager.registerEvents(new EntityDamageByEntity(survivalGames), survivalGames);
		pluginManager.registerEvents(new EntityTarget(survivalGames), survivalGames);
	}
}
