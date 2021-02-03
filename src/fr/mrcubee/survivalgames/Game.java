package fr.mrcubee.survivalgames;

import fr.mrcubee.langlib.Lang;
import fr.mrcubee.plugin.util.spigot.annotations.PluginAnnotations;
import fr.mrcubee.survivalgames.api.event.GameStatsChangeEvent;
import fr.mrcubee.survivalgames.kit.KitManager;
import fr.mrcubee.survivalgames.scoreboard.PluginScoreBoardManager;

import java.util.*;

import fr.mrcubee.survivalgames.step.StepManager;
import fr.mrcubee.world.SpawnTerrainForming;
import net.arkadgames.survivalgame.sql.DataBaseManager;
import org.bukkit.*;
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
    private final GameSetting gameSetting;
    private final DataBaseManager dataBaseManager;
    private final KitManager kitManager;
    private final StepManager stepManager;
    private PluginScoreBoardManager pluginScoreBoardManager;
    private World gameWorld;
    private SpawnTerrainForming spawnTerrainForming;
    private GameStats gameStats;
    private long nextStatTime;
    private boolean forceStart;
    private boolean forcePvp;
    private int totalPlayers;
    private boolean pvpEnable;
    private long gameStartTime;
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
        this.gameSetting = new GameSetting();
        PluginAnnotations.load(survivalGames, this.gameSetting);
        this.dataBaseManager = new DataBaseManager(survivalGames);
        this.kitManager = new KitManager(survivalGames);
        this.stepManager = new StepManager(this);
        this.pluginScoreBoardManager = new PluginScoreBoardManager(this);
        this.gameStats = GameStats.OPENING;
        this.players = new HashSet<Player>();
        this.forceStart = false;
        this.pvpEnable = false;
        SurvivalGamesAPI.setGame(this);
    }

    /**
     * This method is called after loading the server and services.
     * It allows you to retrieve the party world, and to prepare the ScoreBoard and the kits manager.
     */
    protected void init() {
        this.gameWorld = survivalGames.getServer().getWorld(this.gameSetting.getWorldName());
        this.kitManager.createKitUpdater();
        this.spawnTerrainForming = SpawnTerrainForming.create(this.survivalGames, this.gameWorld,
                this.gameSetting.getTerrainFormingSize(), this.survivalGames.getLogger());
        this.pluginScoreBoardManager.runTaskTimerAsynchronously(this.survivalGames, 0L, 10L);
        this.survivalGames.getCommand("kit").setExecutor(this.kitManager);
    }

    public void broadcastMessage(String messageId, String rescueMessage, boolean color, Object... objects) {
        String prefix;

        if (messageId == null || rescueMessage == null)
            return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            prefix = Lang.getMessage(player, "broadcast.prefix", "&6[&cSurvivalGames&6]", true);
            player.sendMessage(prefix + " " + Lang.getMessage(messageId, rescueMessage, color, objects));
        }
    }

    public DataBaseManager getDataBaseManager() {
        return this.dataBaseManager;
    }

    public KitManager getKitManager() {
        return this.kitManager;
    }

    public StepManager getStepManager() {
        return this.stepManager;
    }

    public PluginScoreBoardManager getPluginScoreBoardManager() {
        return this.pluginScoreBoardManager;
    }

    protected void setGameStats(GameStats newStats) {
        if (newStats == null || this.gameStats == newStats
                || (this.gameStats.ordinal() >= 2 && this.gameStats.ordinal() > newStats.ordinal()))
            return;
        this.survivalGames.getServer().getPluginManager().callEvent(new GameStatsChangeEvent(newStats));
        this.gameStats = newStats;
        switch (this.gameStats) {
            case STARTING:
                this.nextStatTime = System.currentTimeMillis() + (this.getGameSetting().getStartTime() * 1000);
                break;
            case DURING:
                this.gameStartTime = System.currentTimeMillis();
                this.gameEndTime = this.gameStartTime + ((this.gameSetting.getTimeBorder() + 20) * 1000);
                this.totalPlayers = this.players.size();
                this.getStepManager().nextStep();
                break;
            case STOPPING:
                this.nextStatTime = System.currentTimeMillis() + (this.getGameSetting().getRestartTime() * 1000);
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

    public SpawnTerrainForming getSpawnTerrainForming() {
        return this.spawnTerrainForming;
    }

    public Location getSpawn() {
        if (this.spawnTerrainForming == null)
            return this.gameWorld.getSpawnLocation();
        return this.spawnTerrainForming.getSpawn();
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

    public void setPvpEnable(boolean pvpEnable) {
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
        this.players.add(player);
        player.setGameMode(GameMode.SURVIVAL);
    }

    public void addSpectator(Player player) {
        if (player == null)
            return;
        this.players.remove(player);
        player.setGameMode(GameMode.SPECTATOR);
    }

    public boolean isSpectator(Player player) {
        return player != null && !this.players.contains(player);
    }

    public int getNumberSpectator() {
        return this.survivalGames.getServer().getOnlinePlayers().size() - this.players.size();
    }

    public long getGameStartTime() {
        return this.gameStartTime;
    }

    public long getGameEndTime() {
        return this.gameEndTime;
    }

    public SurvivalGames getPlugin() {
        return this.survivalGames;
    }
}
