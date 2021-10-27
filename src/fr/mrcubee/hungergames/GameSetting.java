package fr.mrcubee.hungergames;

import fr.mrcubee.annotation.spigot.config.Config;
import org.bukkit.ChatColor;

public class GameSetting {

    private String menuKitName = ChatColor.YELLOW + "KIT";

    @Config(path = "world.name")
    private String worldName = "world";

    @Config(path = "world.loadSize")
    private int loadSize = 320;

    @Config(path = "world.terrainFormingSize")
    private int terrainFormingSize = 60;

    @Config(path = "player.min")
    private int minPlayer = 10;

    @Config(path = "player.max")
    private int maxPlayer = 100;

    @Config(path = "world.border.min")
    private int minBorder = 1;

    @Config(path = "world.border.max")
    private int maxBorder = 2000;

    @Config(path = "world.border.time")
    private long timeBorder = 5400;

    @Config(path = "game.startTime")
    private long startTime = 60;

    @Config(path = "game.step.pvpTime")
    private long timePvp = 300;

    @Config(path = "game.restartTime")
    private long restartTime = 20;

    public String getMenuKitName() {
        return menuKitName;
    }

    public String getWorldName() {
        return worldName;
    }

    public int getLoadSize() {
        return loadSize;
    }

    public int getTerrainFormingSize() {
        return terrainFormingSize;
    }

    public int getMinPlayer() {
        return minPlayer;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public int getMinBorder() {
        return minBorder;
    }

    public int getMaxBorder() {
        return maxBorder;
    }

    public long getTimeBorder() {
        return timeBorder;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getTimePvp() {
        return timePvp;
    }

    public long getRestartTime() {
        return restartTime;
    }
}
