package fr.mrcubee.util;

import org.bukkit.Bukkit;

public class ServerUtil {

    public static String getNMSVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();

        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

}
