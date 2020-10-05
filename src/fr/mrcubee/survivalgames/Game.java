package fr.mrcubee.survivalgames;

import fr.mrcubee.pluginutil.spigot.annotations.PluginAnnotations;
import fr.mrcubee.survivalgames.api.event.GameStatsChangeEvent;
import fr.mrcubee.survivalgames.kit.KitManager;
import fr.mrcubee.survivalgames.scoreboardmanager.PluginScoreBoardManager;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Game {
    private SurvivalGames survivalGames;
    private SurvivalGamesAPI survivalGamesAPI;
    private KitManager kitManager;
    private PluginScoreBoardManager pluginScoreBoardManager;
    private GameSetting gameSetting;
    private GameStats gameStats;
    private World gameWorld;
    private Location spawn;
    private boolean forcestart;
    private boolean forcepvp;
    private int totalPlayers;
    private boolean pvpEnable;
    private long gameDuration;
    private HashSet<Player> players;

    protected Game(SurvivalGames survivalGames) {
        this.survivalGames = survivalGames;
        this.kitManager = new KitManager(survivalGames);
        this.survivalGamesAPI = new SurvivalGamesAPI(this);
        this.gameStats = GameStats.OPENING;
        this.gameSetting = new GameSetting();
        this.players = new HashSet<Player>();
        this.forcestart = false;
        this.pvpEnable = false;

        PluginAnnotations.load(survivalGames, gameSetting);
    }

    protected void init() {
        this.gameWorld = survivalGames.getServer().getWorld(gameSetting.getWorldName());
        this.pluginScoreBoardManager = new PluginScoreBoardManager(
                this.survivalGames.getServer().getScoreboardManager().getMainScoreboard());
        this.survivalGames.getCommand("kit").setExecutor(this.kitManager);
    }

    public void broadcastMessage(String message) {
        if ((message == null) || (message.length() < 1)) {
            return;
        }
        this.survivalGames.getServer().broadcastMessage(
                ChatColor.GOLD + "[" + ChatColor.RED + this.survivalGames.getName() + ChatColor.GOLD + "] " + message);
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
    }

    public GameStats getGameStats() {
        return this.gameStats;
    }

    public GameSetting getGameSetting() {
        return this.gameSetting;
    }

    public World getGameWorld() {
        return gameWorld;
    }

    protected void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getSpawn() {
        return this.spawn.clone();
    }

    protected void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public int getTotalPlayers() {
        return this.totalPlayers;
    }

    protected void forcestart() {
        this.forcestart = true;
    }

    public boolean isForcestart() {
        return this.forcestart;
    }

    public void forcepvp() {
        this.forcepvp = true;
    }

    public boolean isForcepvp() {
        return this.forcepvp;
    }

    protected void setPvpEnable(boolean pvpEnable) {
        this.pvpEnable = pvpEnable;
    }

    public boolean isPvpEnable() {
        return this.pvpEnable;
    }

    protected void setGameDuration(long gameDuration) {
        this.gameDuration = gameDuration;
    }

    public long getGameDuration() {
        return this.gameDuration;
    }

    protected SurvivalGames getSurvivalGames() {
        return this.survivalGames;
    }

    public Set<Player> getPlayerInGame() {
        return (Set<Player>) this.players.clone();
    }

    public int getNumberPlayer() {
        return this.players.size();
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
}
