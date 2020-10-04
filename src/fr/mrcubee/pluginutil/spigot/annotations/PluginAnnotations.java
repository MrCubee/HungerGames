package fr.mrcubee.pluginutil.spigot.annotations;

import org.bukkit.plugin.Plugin;

import fr.mrcubee.pluginutil.spigot.annotations.command.CommandAnnotations;
import fr.mrcubee.pluginutil.spigot.annotations.config.ConfigAnnotations;

public class PluginAnnotations {
	
	private static final ConfigAnnotations CONFIG_LOADER = new ConfigAnnotations();
	private static final CommandAnnotations COMMAND_LOADER = new CommandAnnotations();
	private static final PluginAnnotationsLoader[] INTERFACES_LOADER = {CONFIG_LOADER, COMMAND_LOADER};
	
	public static void load(Plugin plugin, Object... objects) {
		for (PluginAnnotationsLoader interfaceLoader : INTERFACES_LOADER)
			interfaceLoader.loadClass(plugin, objects);
	}

}
