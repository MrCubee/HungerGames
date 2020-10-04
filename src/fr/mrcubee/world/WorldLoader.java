package fr.mrcubee.world;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import fr.mrcubee.survivalgames.world.selection.Selection;

/**
 * 
 * @author MrCubee
 *
 */
public class WorldLoader {
	
	/**
	 * Load the desired chunk.
	 * <pre>
	 *     WorldLoader.loadChunk(chunk)
	 * </pre>
	 * @param chunk the chunk that will have to be loaded.
	 * @return Returns {@code true} if the chunk loaded or returns {@code false} if an error occurred.
	 * @since 1.7
	 */
	private static boolean loadChunk(final Chunk chunk) {
		if (chunk == null)
			return false;
		String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
		try {
			Class<?> classCraftWorld = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
			Object craftWorld = classCraftWorld.cast(chunk.getWorld());
			Method methodHandler = classCraftWorld.getMethod("getHandle", new Class[0]);
			Object worldServer = methodHandler.invoke(craftWorld, new Object[0]);
			Field fieldWorldServer = worldServer.getClass().getField("chunkProviderServer");
			Object chunkProviderServer = fieldWorldServer.get(worldServer);
			Method method = chunkProviderServer.getClass().getMethod("getChunkAt", new Class[] { Integer.TYPE, Integer.TYPE });
			method.invoke(chunkProviderServer, new Object[] { Integer.valueOf(chunk.getX()), Integer.valueOf(chunk.getZ()) });
		} catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException
				| IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Load the desired chunks.
	 * <pre>
	 *     WorldLoader.loadChunks(chunks)
	 * </pre>
	 * @param chunks The chunks that will have to be loaded
	 * @return Returns {@code true} if the chunks loaded or returns {@code false} if an error occurred.
	 * @since 1.7
	 */
	public static boolean loadChunks(final Chunk... chunks) {
		boolean result = true;
		
		if (chunks == null)
			return false;
		for (Chunk chunk : chunks)
			if (!loadChunk(chunk))
				result = false;
		return result;
	}

	/**
	 * Load the desired selections.
	 * <pre>
	 *     WorldLoader.loadRegions(selections)
	 * </pre>
	 * @param selections The selections that will need to be loaded.
	 * @return Returns {@code true} if the selections loaded or returns {@code false} if an error occurred.
	 */
	public static boolean loadRegions(final Selection... selections) {
		Chunk[] chunks;

		if (selections == null)
			return false;
		for (Selection selection : selections) {
			chunks = selection.getChunks();
			if (chunks == null)
				continue;
			for (Chunk chunk : chunks)
				loadChunk(chunk);
		}
		return true;
	}
}
