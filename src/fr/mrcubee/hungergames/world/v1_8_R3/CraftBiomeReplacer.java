package fr.mrcubee.hungergames.world.v1_8_R3;

import net.minecraft.server.v1_8_R3.BiomeBase;

public class CraftBiomeReplacer {

    public static boolean replace() {
        BiomeBase[] biomeBases = BiomeBase.getBiomes();

        biomeBases[BiomeBase.OCEAN.id] = BiomeBase.PLAINS;
        biomeBases[BiomeBase.DEEP_OCEAN.id] = BiomeBase.PLAINS;
        biomeBases[BiomeBase.FROZEN_OCEAN.id] = BiomeBase.PLAINS;

        biomeBases[BiomeBase.BEACH.id] = BiomeBase.PLAINS;
        biomeBases[BiomeBase.COLD_BEACH.id] = BiomeBase.PLAINS;
        biomeBases[BiomeBase.STONE_BEACH.id] = BiomeBase.PLAINS;
        return true;
    }

}
