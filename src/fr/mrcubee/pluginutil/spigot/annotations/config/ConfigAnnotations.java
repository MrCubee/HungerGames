package fr.mrcubee.pluginutil.spigot.annotations.config;

import java.lang.reflect.Field;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import fr.mrcubee.pluginutil.spigot.annotations.PluginAnnotationsLoader;

public class ConfigAnnotations implements PluginAnnotationsLoader {

	@Override
	public void loadClass(Plugin plugin, Object... objects) {
		if (plugin == null || objects == null)
			return;
		for (Object object : objects)
			loadObject(plugin, object);
	}

	private void loadObject(Plugin plugin, Object object) {
		Class<?> objectClass = object.getClass();
		FileConfiguration config = plugin.getConfig();
		Config configValue;
		Object value;
		
		for (Field field :  objectClass.getDeclaredFields()) {
			configValue = field.getAnnotation(Config.class);
			if (configValue != null && configValue.path() != null && !configValue.path().isEmpty() && config.contains(configValue.path())) {
				field.setAccessible(true);
				value = config.get(configValue.path());
				if (value != null && (value instanceof String) && configValue.color())
					value = ChatColor.translateAlternateColorCodes(configValue.colorChar(), (String) value);
				try {
					if (value != null)
						field.set(object, value);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
