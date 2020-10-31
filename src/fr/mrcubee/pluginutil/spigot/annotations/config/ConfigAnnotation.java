package fr.mrcubee.pluginutil.spigot.annotations.config;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigAnnotation {

	public enum ConfigLogType {
		LOADED,
		NO_FOUND,
		ERROR,
	};

	private static void configLog(Field field, Config configValue, Object value, ConfigLogType type) {
		String message;

		if (field == null || configValue == null || type == null)
			return;
		message = "[ANNOTATION CONFIG]";
		switch (type) {
			case LOADED:
				message += "[LOADED] ";
				break;
			case NO_FOUND:
				message +=  "[NO FOUND] ";
				break;
			case ERROR:
				message += "[ERROR] ";
				break;
		}
		message += "CLASS: " + field.getDeclaringClass().getName() + " FIELD: " + field.getName()
				+ " CONFIG_PATH: " + configValue.path() + " VALUE: " + ((value == null) ? "NULL" : value.toString());
		switch (type) {
			case LOADED:
				Bukkit.getLogger().info(message);
				break;
			case NO_FOUND:
				Bukkit.getLogger().warning(message);
				break;
			case ERROR:
				Bukkit.getLogger().severe(message);
				break;
		}
	}

	private static void loadField(FileConfiguration config, Object object, Field field, Config configValue) {
		Object value;

		if (config == null  || object == null || field == null || configValue == null)
			return;
		if (!config.contains(configValue.path())) {
			configLog(field, configValue, null, ConfigLogType.NO_FOUND);
			return;
		}
		field.setAccessible(true);
		value = config.get(configValue.path());
		if (configValue.color() && (value instanceof String))
			value = ChatColor.translateAlternateColorCodes(configValue.colorChar(), (String) value);
		try {
			if (value != null)
				field.set(object, value);
			configLog(field, configValue, value, ConfigLogType.LOADED);
		} catch (Exception ignored) {
			configLog(field, configValue, value, ConfigLogType.ERROR);
		}
	}

    public static void loadClass(FileConfiguration fileConfiguration, Object... objects) {
        if (fileConfiguration == null || objects == null)
            return;
        for (Object object : objects)
			for (Field field : object.getClass().getDeclaredFields())
				loadField(fileConfiguration, object, field, field.getAnnotation(Config.class));
    }
}
