package fr.mrcubee.plugin.util.spigot.annotations;

import fr.mrcubee.annotation.spigot.config.ConfigAnnotation;
import org.bukkit.plugin.Plugin;

public class PluginAnnotations {

	public static void load(Plugin plugin, Object... objects) {
		ConfigAnnotation.loadClass(plugin.getConfig(), objects);
	}

}
