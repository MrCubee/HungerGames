package fr.mrcubee.survivalgames;

import org.bukkit.ChatColor;

import fr.mrcubee.pluginutil.spigot.annotations.config.Config;


public class GameSetting {

    private String menuKitName = ChatColor.YELLOW + "KIT";

    @Config(path = "world.name")
    private String worldName = "world";

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

    @Config(path = "game.pvpTime")
    private long timePvp = 300;

    public String getMenuKitName() {
        return menuKitName;
    }

    public String getWorldName() {
        return worldName;
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

    public long getTimePvp() {
        return timePvp;
    }

}
