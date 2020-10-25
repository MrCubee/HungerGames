package fr.mrcubee.world;

import org.bukkit.Bukkit;
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

    private BukkitTask bukkitTask;

    private final Logger logger;
    private final World world;
    private final int maxStep;
    private int currentStep;
    private int currentY;
    private boolean grass;
    private boolean ground;
    private final Vector[] relativeLocations;

    private SpawnTerrainForming(World world, int size, Logger logger) {
        int sizeSquared = size * size;
        LinkedList<Vector> tmpList = new LinkedList<Vector>();

        this.maxStep = 3;
        this.logger = logger;
        this.world = world;
        int currentSpawnY = world.getHighestBlockYAt(0, 0);
        this.currentStep = 0;
        this.currentY = currentSpawnY + 30;
        this.grass = false;
        this.ground = false;
        for (int z = -size; z <= size; z++)
            for (int x = -size; x <= size; x++)
                if ((x * x) + (z * z) <= sizeSquared)
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
            case LOG:
            case LOG_2:
            case LEAVES:
            case LEAVES_2:
            case LONG_GRASS:
                return false;
        }
        return true;
    }

    @Override
    public void run() {
        Location last = new Location(this.world, 0, this.currentY - 1, 0);
        Location current = new Location(this.world,0, this.currentY, 0);
        Block block;

        if (!this.grass && isGround(this.world.getBlockAt(0, this.currentY, 0).getType())) {
            this.grass = true;
            this.world.setSpawnLocation(0, this.currentY + 1, 0);
        } else if (this.currentStep == 0 && !this.ground && this.grass)
            this.ground = true;
        for (int i = this.currentStep; i < this.relativeLocations.length; i += this.maxStep) {
            current.add(this.relativeLocations[i]);
            last.add(this.relativeLocations[i]);
            block = current.getBlock();
            if (this.ground)
                block.setType(Material.DIRT);
            else if (this.grass)
                block.setType(Material.GRASS);
            else
                block.setType(Material.AIR);
            current.subtract(this.relativeLocations[i]);
        }
        if (this.currentStep < this.maxStep)
            this.currentStep = (++this.currentStep >= this.maxStep) ? 0 : this.currentStep;
        if (this.currentStep == 0 && --this.currentY == 0)
            this.cancel();
    }

    public Location getSpawn() {
        return new Location(this.world, 0, this.world.getHighestBlockYAt(0, 0), 0);
    }

    public boolean isCurrentlyRunning() {
        return Bukkit.getScheduler().isCurrentlyRunning(getTaskId());
    }

    public boolean isGrass() {
        return this.grass;
    }

    public static SpawnTerrainForming create(Plugin plugin, World world, int size, Logger logger) {
        if (plugin == null || world == null || size <= 0)
            return null;
        return new SpawnTerrainForming(world, size, logger);
    }
}
