package fr.mrcubee.world;

import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.logging.Logger;

public class WorldSpawnSetup {

    private static void loadArena(World world, int loadSize, Logger logger) {
        int chunkSize = (loadSize / 16) + 1;
        int mSize = chunkSize / 2;
        int total = chunkSize * chunkSize;
        int current = 0;
        int temp = -1;
        int result;

        if (world == null || loadSize < 0)
            return;
        for (int z = -mSize; z <= mSize; z++) {
            for (int x = -mSize; x <= mSize; x++) {
                WorldLoader.loadChunks(world.getChunkAt(x, z));
                current++;
                result = (current * 100) / total;
                if (logger != null && temp != result && result % 10 == 0) {
                    temp = result;
                    logger.warning("Loading ... (" + result + "%)");
                }
            }
        }
    }

    public static boolean setup(World world, int loadSize, Logger logger) {
        if (world == null || loadSize < 0)
            return false;
        loadArena(world, loadSize, logger);
        return true;
    }

}
