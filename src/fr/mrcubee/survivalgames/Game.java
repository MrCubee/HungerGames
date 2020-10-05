package fr.mrcubee.survivalgames;

import fr.mrcubee.pluginutil.spigot.annotations.PluginAnnotations;
import fr.mrcubee.survivalgames.api.event.GameStatsChangeEvent;
import fr.mrcubee.survivalgames.kit.KitManager;
import fr.mrcubee.survivalgames.scoreboardmanager.PluginScoreBoardManager;

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
    private World gameWorld;
    private Location spawn;
    private boolean forceStart;
    private boolean forcePvp;
    private int totalPlayers;
    private boolean pvpEnable;
    private long gameDuration;
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
        this.gameWorld = survivalGames.getServer().getWorld(gameSetting.getWorldName());
        this.pluginScoreBoardManager = new PluginScoreBoardManager(
                this.survivalGames.getServer().getScoreboardManager().getMainScoreboard());
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
        survivalGames.getServer().getPluginManager().callEvent(new GameStatsChangeEvent(newStats));
        gameStats = newStats;
    }

    public GameStats getGameStats() {
        return gameStats;
    }

    public GameSetting getGameSetting() {
        return gameSetting;
    }

    public World getGameWorld() {
        return gameWorld;
    }

    protected void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getSpawn() {
        return spawn.clone();
    }

    protected void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    protected void forceStart() {
        forceStart = true;
    }

    public boolean isForceStart() {
        return forceStart;
    }

    public void forcePvp() {
        forcePvp = true;
    }

    public boolean isForcePvp() {
        return forcePvp;
    }

    protected void setPvpEnable(boolean pvpEnable) {
        this.pvpEnable = pvpEnable;
    }

    public boolean isPvpEnable() {
        return pvpEnable;
    }

    protected void setGameDuration(long gameDuration) {
        this.gameDuration = gameDuration;
    }

    public long getGameDuration() {
        return gameDuration;
    }

    protected SurvivalGames getSurvivalGames() {
        return survivalGames;
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
}
