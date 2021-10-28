package fr.mrcubee.hungergames.world;

import fr.mrcubee.bukkit.Versions;

import java.lang.reflect.Method;

public class BiomeReplacer {

    public static boolean removeOcean() {
        Versions version;
        Class<?> clazz;
        Method method;
        Boolean result = null;

        if ((version = Versions.getCurrent()) == null)
            return false;
        try {
            clazz = Class.forName("fr.mrcubee.hungergames.world." + version.toString() + ".CraftBiomeReplacer");
            method = clazz.getDeclaredMethod("replace");
            method.setAccessible(true);
            result = (Boolean) method.invoke(null);
        } catch (Exception ignored) {}
        return (result != null && result);
    }
}
