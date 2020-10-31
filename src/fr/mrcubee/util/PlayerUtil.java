package fr.mrcubee.util;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PlayerUtil {

    public static Object getEntityPlayer(Player player) {
        Class<?> clazz;
        Method method;

        if (player == null)
            return null;
        try {
            clazz = Class.forName("org.bukkit.craftbukkit." + ServerUtil.getNMSVersion() + ".entity.CraftPlayer");
            method = clazz.getMethod("getHandle");
            return method.invoke(player);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
        return null;
    }

    public static Object getPlayerConnection(Player player) {
        Class<?> clazz;
        Field field;
        Object entityPlayer = getEntityPlayer(player);

        if (entityPlayer == null)
            return null;
        try {
            clazz = Class.forName("net.minecraft.server." + ServerUtil.getNMSVersion() + ".EntityPlayer");
            field = clazz.getDeclaredField("playerConnection");
            field.setAccessible(true);
            return field.get(entityPlayer);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException ignored) {}
        return null;
    }

    public static boolean sendPacket(Player player, Object packet) {
        Object playerConnection = getPlayerConnection(player);
        Class<?> clazz;
        Class<?> parameterClass;
        Method method;

        if (playerConnection == null)
            return false;
        try {
            clazz = Class.forName("net.minecraft.server." + ServerUtil.getNMSVersion() + ".PlayerConnection");
            parameterClass = Class.forName("net.minecraft.server." + ServerUtil.getNMSVersion() + ".Packet");
            method = clazz.getMethod("sendPacket", parameterClass);
            method.invoke(playerConnection, packet);
            return true;
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
        return false;
    }

    public static Object getChatComponentText(String message) {
        Class<?> clazz;
        Class<?> parameterClass;
        Constructor<?> constructor;

        if (message == null)
            return null;
        try {
            clazz = Class.forName("net.minecraft.server." + ServerUtil.getNMSVersion() + ".ChatComponentText");
            constructor = clazz.getConstructor(String.class);
            constructor.setAccessible(true);
            return constructor.newInstance(message);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {}
        return null;
    }

    public static boolean sendPlayerActionBar(Player player, String message) {
        Class<?> clazz;
        Class<?> parameterClass;
        Constructor<?> constructor;
        Object chatComponentText;

        if (player == null)
            return false;
        chatComponentText = getChatComponentText(message);
        if (chatComponentText == null)
            return false;
        try {
            clazz = Class.forName("net.minecraft.server." + ServerUtil.getNMSVersion() + ".PacketPlayOutChat");
            parameterClass = Class.forName("net.minecraft.server." + ServerUtil.getNMSVersion() + ".IChatBaseComponent");
            constructor = clazz.getConstructor(parameterClass, byte.class);
            constructor.setAccessible(true);
            return sendPacket(player, constructor.newInstance(chatComponentText, (byte) 2));
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {}
        return false;
    }

}
