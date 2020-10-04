package fr.mrcubee.survivalgames.worldmanager;

import org.bukkit.Location;
import org.bukkit.World;

public class BorderManager {
	
	public static void setCenter(World world, Location location) {
		if (world == null || location == null)
		world.getWorldBorder().setWarningDistance(10);
		world.getWorldBorder().setCenter(location.getX(), location.getZ());
	}
	
	public static void setCenter(World world, double x, double z) {
		if (world == null)
			return;
		world.getWorldBorder().setCenter(x, z);
	}
	
	public static void setWorldBorder(World world, double size) {
		if ((world == null) || (size < 1))
			return;
		world.getWorldBorder().setSize(size);
	}
	
	public static void setWorldBorder(World world, double size, long time) {
		if (world == null || size < 0 || time < 0)
			return;
		world.getWorldBorder().setSize(size, time);
	}

}
