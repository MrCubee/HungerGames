package fr.mrcubee.pluginutil.spigot.annotations;

import org.bukkit.plugin.Plugin;

public interface PluginAnnotationsLoader {
	
	public void loadClass(Plugin plugin, Object... objects);

}
