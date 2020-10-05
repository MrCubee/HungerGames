package fr.mrcubee.survivalgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.mrcubee.survivalgames.listeners.server.PingServer;
import fr.mrcubee.survivalgames.worldmanager.BorderManager;

public class Timer extends Thread {

    private SurvivalGames survivalGames;
    private int seconds;
    private long time;

    protected Timer(SurvivalGames survivalGames) {
        this.survivalGames = survivalGames;
        this.seconds = -1;
        this.time = -1;
    }

    @Override
    public void run() {
        World world = null;
        GameStats gameStats;

        while (survivalGames.getGame() != null && (gameStats = survivalGames.getGame().getGameStats()) != GameStats.CLOSING) {
            if (world == null)
                world = survivalGames.getGame().getGameWorld();
            else {
                if (world.isThundering()) {
                    world.setThunderDuration(0);
                    world.setThundering(false);
                }
                if (world.hasStorm()) {
                    world.setWeatherDuration(0);
                    world.setStorm(false);
                }
                if (world.getTime() != 0 && (gameStats == GameStats.WAITING || gameStats == GameStats.STARTING))
                    world.setTime(0);
            }
            switch (survivalGames.getGame().getGameStats()) {
                case WAITING:
                    waitting();
                    break;
                case STARTING:
                    starting();
                    break;
                case DURING:
                    during();
                    break;
                case STOPPING:
                    stopping();
                    break;
                default:
                    break;
            }
        }
    }

    public void waitting() {
        if ((time < 0) || (System.currentTimeMillis() - time >= 1000)) {
            time = System.currentTimeMillis();
            survivalGames.getGame().getPluginScoreBoardManager().putLine(ChatColor.GRAY + "Player: " + ChatColor.RED + survivalGames.getGame().getNumberPlayer()
                    + ChatColor.GRAY + "/" + ChatColor.RED + survivalGames.getGame().getGameSetting().getMinPlayer(), -1);
            survivalGames.getGame().getPluginScoreBoardManager().putLine(ChatColor.GREEN + "Waiting for players...", -2);
        }

        if (survivalGames.getGame().getNumberPlayer() >= survivalGames.getGame().getGameSetting().getMinPlayer() || survivalGames.getGame().isForceStart()) {
            time = -1;
            survivalGames.getGame().setGameStats(GameStats.STARTING);
        }
    }

    public void starting() {
        if (survivalGames.getGame().getNumberPlayer() < survivalGames.getGame().getGameSetting().getMinPlayer() && !survivalGames.getGame().isForceStart()) {
            time = -1;
            survivalGames.getGame().setGameStats(GameStats.WAITING);
            return;
        }

        if (time < 0) {
            time = System.currentTimeMillis();
            seconds = 60;
        }

        if (System.currentTimeMillis() - time >= 1000) {
            time = System.currentTimeMillis();
            survivalGames.getGame().getPluginScoreBoardManager().putLine(ChatColor.GRAY + "Player: " + ChatColor.RED + survivalGames.getGame().getNumberPlayer()
                    + ChatColor.GRAY + "/" + ChatColor.RED + survivalGames.getGame().getGameSetting().getMinPlayer(), -1);
            survivalGames.getGame().getPluginScoreBoardManager().putLine(ChatColor.GOLD + "Starts in " + ChatColor.RED + PingServer.getTime(seconds), -2);
            if (seconds <= 10) {
                survivalGames.getGame().broadcastMessage(ChatColor.GOLD + "The game starts in " + ChatColor.RED + seconds + " second" + ((seconds > 1) ? "s" : ""));
                try {
                    for (Player player : Bukkit.getOnlinePlayers())
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 100, 1);
                } catch (Exception e) {}
            }
            seconds--;
        }
        if (seconds <= 0) {
            time = -1;
            Bukkit.getScheduler().runTask(survivalGames, new Runnable() {
                public void run() {
                    int players = Bukkit.getOnlinePlayers().size();
                    survivalGames.getGame().setTotalPlayers(players);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.teleport(survivalGames.getGame().getSpawn());
                        player.getInventory().clear();
                        player.getInventory().addItem(new ItemStack(Material.COMPASS));
                        player.getInventory().addItem(new ItemStack(Material.APPLE, 10));
                    }
                    survivalGames.getGame().getKitManager().randomKitPlayerWithNotKit();
                    survivalGames.getGame().getKitManager().giveKitToPlayer();
                }
            });
            try {
                for (Player player : Bukkit.getOnlinePlayers())
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 100, 2);
            } catch (Exception e) {}
            survivalGames.getGame().setGameStats(GameStats.DURING);
        }
    }

    public void during() {
        if (time < 0) {
            time = System.currentTimeMillis();
            survivalGames.getGame().setGameDuration(0);
            BorderManager.setWorldBorder(survivalGames.getGame().getGameWorld(), survivalGames.getGame().getGameSetting().getMaxBorder());
            BorderManager.setWorldBorder(survivalGames.getGame().getGameWorld(), survivalGames.getGame().getGameSetting().getMinBorder(), survivalGames.getGame().getGameSetting().getTimeBorder());
        }

        if (System.currentTimeMillis() - time >= 1000) {
            int players = survivalGames.getGame().getNumberPlayer();
            long border = (int) (survivalGames.getGame().getGameWorld().getWorldBorder().getSize() / 2);

            survivalGames.getGame().getPluginScoreBoardManager().putLine(ChatColor.GRAY + "Player: " + ChatColor.RED + players, -1);

            if (!survivalGames.getGame().isPvpEnable()) {
                long time = survivalGames.getGame().getGameSetting().getTimePvp() - survivalGames.getGame().getGameDuration();
                survivalGames.getGame().getPluginScoreBoardManager().putLine(ChatColor.GRAY + "PvP in " + ChatColor.RED + PingServer.getTime(time), -2);
                survivalGames.getGame().getPluginScoreBoardManager().putLine(ChatColor.BLUE + "Border: " + ChatColor.RED + "+" + border + " -" + border, -3);

                if (survivalGames.getGame().isForcePvp())
                    time = 0;

                if (time <= 10) {
                    survivalGames.getGame().broadcastMessage("PvP will be active in " + ChatColor.RED + time + " second" + ((time > 1) ? "s" : ""));
                    try {
                        for (Player player : Bukkit.getOnlinePlayers())
                            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 100, 1);
                    } catch (Exception e) {
                    }
                }

                if (time <= 0) {
                    survivalGames.getGame().setPvpEnable(true);
                    for (int i = -2; i > -4; i--)
                        survivalGames.getGame().getPluginScoreBoardManager().removeLine(i);
                    survivalGames.getGame().getPluginScoreBoardManager().putLine(ChatColor.BLUE + "Border: " + ChatColor.RED + "+" + border + " -" + border, -2);
                    survivalGames.getGame().broadcastMessage(ChatColor.GOLD + "PvP is enabled !");
                    try {
                        for (Player player : Bukkit.getOnlinePlayers())
                            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 100, 1);
                    } catch (Exception e) {
                    }
                }
            } else {
                survivalGames.getGame().getPluginScoreBoardManager().putLine(ChatColor.BLUE + "Border: " + ChatColor.RED + "+" + border + " -" + border, -2);
            }

            time = System.currentTimeMillis();
            survivalGames.getGame().setGameDuration(survivalGames.getGame().getGameDuration() + 1);

            if (players < 2) {
                time = -1;
                survivalGames.getGame().setGameStats(GameStats.STOPPING);
                return;
            }
        }
    }

    public void stopping() {
        if (time < 0) {
            for (int i = -1; i > -4; i--)
                survivalGames.getGame().getPluginScoreBoardManager().removeLine(i);
            time = System.currentTimeMillis();
            seconds = 20;
        }

        if (System.currentTimeMillis() - time >= 1000) {
            time = System.currentTimeMillis();

            survivalGames.getGame().getPluginScoreBoardManager().putLine(ChatColor.GOLD + "Restart in " + ChatColor.RED + PingServer.getTime(seconds), -1);

            if (seconds <= 10) {
                survivalGames.getGame().broadcastMessage(ChatColor.GOLD + "Restart the server in " + ChatColor.RED + seconds + " second" + ((seconds > 1) ? "s" : ""));
                try {
                    for (Player player : Bukkit.getOnlinePlayers())
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 100, 1);
                } catch (Exception e) {
                }
            }

            seconds--;
        }

        if (seconds <= 0) {
            time = -1;
            Bukkit.getScheduler().runTask(survivalGames, new Runnable() {
                public void run() {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.kickPlayer(ChatColor.GOLD + "The server will restart !");
                    }
                    survivalGames.getServer().shutdown();
                }
            });
            survivalGames.getGame().setGameStats(GameStats.CLOSING);
        }
    }

    public int getSeconds() {
        return seconds;
    }

}
