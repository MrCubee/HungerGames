package fr.mrcubee.pluginutil.spigot.annotations;

import org.bukkit.plugin.Plugin;

import fr.mrcubee.pluginutil.spigot.annotations.config.ConfigAnnotation;

public class PluginAnnotations {

	public static void load(Plugin plugin, Object... objects) {
		ConfigAnnotation.loadClass(plugin.getConfig(), objects);
	}

}
