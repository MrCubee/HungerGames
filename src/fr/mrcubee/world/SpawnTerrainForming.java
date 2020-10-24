package fr.mrcubee.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Logger;


public class SpawnTerrainForming extends BukkitRunnable {

    private BukkitTask task;

    private final Logger logger;
    private final World world;
    private final int size;
    private final int currentSpawnY;
    private final int maxCurrent;
    private int current;
    private int currentY;
    private boolean grass;
    private boolean ground;
    private final Vector[] relativeLocations;

    private SpawnTerrainForming(World world, int size, Logger logger) {
        LinkedList<Vector> tmpList = new LinkedList<Vector>();
        int max = size * 2;

        this.maxCurrent = 3;
        this.logger = logger;
        this.world = world;
        this.size = size;
        this.currentSpawnY = world.getHighestBlockYAt(0, 0);
        this.current = 0;
        this.currentY = this.currentSpawnY + 30;
        this.grass = false;
        this.ground = false;
        for (int z = -max; z <= max; z++)
            for (int x = -max; x <= max; x++)
                if (Math.abs(x) + Math.abs(z) <= size)
                    tmpList.add(new Vector(x, 0, z));
        Collections.shuffle(tmpList);
        this.relativeLocations = tmpList.toArray(new Vector[0]);
        tmpList.clear();
    }

    private boolean isGround(Material material) {
        if (material == null)
            return false;
        switch (material) {
            case AIR:
            case WOOD:
            case LEAVES:
            case LEAVES_2:
            case LONG_GRASS:
                return false;
        }
        return true;
    }

    @Override
    public void run() {
        Location current = new Location(this.world,0, this.currentY, 0);
        Block block;

        if (!this.grass && isGround(this.world.getBlockAt(0, this.currentY, 0).getType())) {
            this.grass = true;
            this.world.setSpawnLocation(0, this.currentY + 1, 0);
        } else if (this.current == 0 && !this.ground && this.grass)
            this.ground = true;
        for (int i = this.current; i < this.relativeLocations.length; i += this.maxCurrent) {
            current.add(this.relativeLocations[i]);
            block = current.getBlock();
            if (this.ground)
                block.setType(Material.DIRT);
            else if (this.grass)
                block.setType(Material.GRASS);
            else
                block.setType(Material.AIR);
            current.subtract(this.relativeLocations[i]);
        }
        if (this.current < this.maxCurrent)
            this.current = (++this.current >= this.maxCurrent) ? 0 : this.current;
        if (this.current == 0 && --this.currentY == 0)
            this.cancel();
    }

    public Location getSpawn() {
        if (this.grass)
            return new Location(this.world, 0, this.world.getHighestBlockYAt(0, 0), 0);
        return new Location(this.world, 0, this.currentY, 0);
    }

    public static SpawnTerrainForming create(Plugin plugin, World world, int size, Logger logger) {
        if (plugin == null || world == null || size <= 0)
            return null;
        return new SpawnTerrainForming(world, size, logger);
    }
}
