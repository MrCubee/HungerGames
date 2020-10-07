package fr.mrcubee.survivalgames;

import fr.mrcubee.pluginutil.spigot.annotations.PluginAnnotations;
import fr.mrcubee.survivalgames.api.event.GameStatsChangeEvent;
import fr.mrcubee.survivalgames.kit.KitManager;
import fr.mrcubee.survivalgames.scoreboard.PluginScoreBoardManager;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * This class contains all the elements of the party.
 * <p>
 * It can only be instantiated by the plugin. Because it is only useful for him.
 *
 * @author MrCubee
 */
public class Game {
    private final SurvivalGames survivalGames;
    private final KitManager kitManager;
    private final GameSetting gameSetting;
    private PluginScoreBoardManager pluginScoreBoardManager;
    private GameStats gameStats;
    private long nextStatTime;
    private World gameWorld;
    private Location spawn;
    private boolean forceStart;
    private boolean forcePvp;
    private int totalPlayers;
    private boolean pvpEnable;
    private long gameEndTime;
    private HashSet<Player> players;

    /**
     * The constructor makes it possible to instantiate the classes necessary for the party
     * and to define default values in the variables.
     * <p>
     * This constructor is called before the server and services are loaded.
     * It can only be instantiated by the plugin. Because it is only useful for him.
     *
     * @param survivalGames The plugin's main class instance.
     */
    protected Game(SurvivalGames survivalGames) {
        this.survivalGames = survivalGames;
        this.kitManager = new KitManager(survivalGames);
        this.gameStats = GameStats.OPENING;
        this.gameSetting = new GameSetting();
        this.players = new HashSet<Player>();
        this.forceStart = false;
        this.pvpEnable = false;

        PluginAnnotations.load(survivalGames, gameSetting);
        SurvivalGamesAPI.setGame(this);
    }

    /**
     * This method is called after loading the server and services.
     * It allows you to retrieve the party world, and to prepare the ScoreBoard and the kits manager.
     */
    protected void init() {
        this.gameWorld = survivalGames.getServer().getWorld(this.gameSetting.getWorldName());
        this.pluginScoreBoardManager = new PluginScoreBoardManager(this);
        this.pluginScoreBoardManager.runTaskTimerAsynchronously(this.survivalGames, 0L, 20L);
        this.survivalGames.getCommand("kit").setExecutor(this.kitManager);
    }

    /**
     * This method allows you to send a message to all players in the standard SurvivalGames format.
     *
     * @param message The message to send to the players.
     */
    public void broadcastMessage(String message) {
        if (message == null || message.isEmpty() || StringUtils.isWhitespace(message))
            return;
        this.survivalGames.getServer().broadcastMessage(ChatColor.GOLD + "[" + ChatColor.RED + this.survivalGames.getName() + ChatColor.GOLD + "] " + message);
    }

    public KitManager getKitManager() {
        return this.kitManager;
    }

    public PluginScoreBoardManager getPluginScoreBoardManager() {
        return this.pluginScoreBoardManager;
    }

    protected void setGameStats(GameStats newStats) {
        if (newStats == null)
            return;
        this.survivalGames.getServer().getPluginManager().callEvent(new GameStatsChangeEvent(newStats));
        this.gameStats = newStats;
        switch (this.gameStats) {
            case STARTING:
                this.nextStatTime = System.currentTimeMillis() + 60000;
                break;
            case DURING:
                this.nextStatTime = System.currentTimeMillis() + (this.getGameSetting().getTimePvp() * 1000);
                break;
            case STOPPING:
                this.nextStatTime = System.currentTimeMillis() + 20000;
        }
    }

    public GameStats getGameStats() {
        return this.gameStats;
    }

    public long getNextStatTime() {
        return this.nextStatTime;
    }

    public GameSetting getGameSetting() {
        return this.gameSetting;
    }

    public World getGameWorld() {
        return this.gameWorld;
    }

    protected void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getSpawn() {
        return this.spawn.clone();
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public int getTotalPlayers() {
        return this.totalPlayers;
    }

    protected void forceStart() {
        this.forceStart = true;
    }

    public boolean isForceStart() {
        return this.forceStart;
    }

    public void forcePvp() {
        this.forcePvp = true;
    }

    public boolean isForcePvp() {
        return this.forcePvp;
    }

    protected void setPvpEnable(boolean pvpEnable) {
        this.pvpEnable = pvpEnable;
    }

    public boolean isPvpEnable() {
        return this.pvpEnable;
    }

    public Set<Player> getPlayerInGame() {
        return (Set<Player>) players.clone();
    }

    public int getNumberPlayer() {
        return players.size();
    }

    public void addPlayer(Player player) {
        if (player == null || !player.isOnline())
            return;
        players.add(player);
        player.setGameMode(GameMode.SURVIVAL);
    }

    public void addSpectator(Player player) {
        if (player == null)
            return;
        players.remove(player);
        player.setGameMode(GameMode.SPECTATOR);
    }

    public boolean isSpectator(Player player) {
        return player != null && !this.players.contains(player);
    }

    public int getNumberSpectator() {
        return this.survivalGames.getServer().getOnlinePlayers().size() - this.players.size();
    }

    public void setGameEndTime(long gameEndTime) {
        this.gameEndTime = gameEndTime;
    }

    public long getGameEndTime() {
        return gameEndTime;
    }
}
