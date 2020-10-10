package fr.mrcubee.survivalgames;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable {

    private final SurvivalGames survivalGames;

    protected Timer(SurvivalGames survivalGames) {
        this.survivalGames = survivalGames;
    }

    private void playersPlaySound(Sound sound, float volume, float pitch) {
        this.survivalGames.getServer().getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), sound, volume, pitch);
        });
    }

    public void waiting() {
        Game game = this.survivalGames.getGame();

        if (game.isForceStart() || game.getNumberPlayer() >= game.getGameSetting().getMinPlayer())
            game.setGameStats(GameStats.STARTING);
    }

    public void starting() {
        Game game = this.survivalGames.getGame();
        long seconds;

        if (!game.isForceStart() && game.getNumberPlayer() < game.getGameSetting().getMinPlayer()) {
            game.setGameStats(GameStats.WAITING);
            return;
        }
        seconds = (game.getNextStatTime() - System.currentTimeMillis()) / 1000;
        if (seconds <= 0) {
            playersPlaySound(Sound.ORB_PICKUP, 100, 2);
            game.setGameStats(GameStats.DURING);
        } else if (seconds == 10 || seconds <= 5) {
            playersPlaySound(Sound.ORB_PICKUP, 100, 1);
            game.broadcastMessage(ChatColor.GOLD + "The game starts in " + ChatColor.RED + seconds + " second" + ((seconds > 1) ? "s" : ""));
        }
    }

    public void during() {
        Game game = this.survivalGames.getGame();
        long seconds;

        if (game.getNumberPlayer() <= 1) {
            game.setGameStats(GameStats.STOPPING);
            return;
        } else if (game.isPvpEnable())
            return;
        else if (game.isForcePvp()) {
            game.setPvpEnable(true);
            return;
        }
        seconds = (game.getNextStatTime() - System.currentTimeMillis()) / 1000;
        if (seconds <= 0) {
            playersPlaySound(Sound.ORB_PICKUP, 100, 2);
            game.setPvpEnable(true);
            game.broadcastMessage(ChatColor.GOLD + "PvP is enabled !");
        } else if (seconds == 10 || seconds <= 5) {
            playersPlaySound(Sound.ORB_PICKUP, 100, 1);
            game.broadcastMessage("PvP will be active in " + ChatColor.RED + seconds + " second" + ((seconds > 1) ? "s" : ""));
        }
    }

    public void stopping() {
        Game game = this.survivalGames.getGame();
        long seconds;

        if (this.survivalGames.getServer().getOnlinePlayers().isEmpty()) {
            this.survivalGames.getGame().setGameStats(GameStats.CLOSING);
            return;
        }
        seconds = (game.getNextStatTime() - System.currentTimeMillis()) / 1000;
        if (seconds <= 0)
            this.survivalGames.getGame().setGameStats(GameStats.CLOSING);
        else if (seconds == 10 || seconds <= 5) {
            playersPlaySound(Sound.ORB_PICKUP, 100, 1);
            game.broadcastMessage(ChatColor.GOLD + "Restart the server in " + ChatColor.RED + seconds + " second" + ((seconds > 1) ? "s" : ""));
        }
    }

    @Override
    public void run() {
        World world;
        GameStats gameStats;

        if (survivalGames.getGame() == null || (world = survivalGames.getGame().getGameWorld()) == null
            || (gameStats = survivalGames.getGame().getGameStats()) == GameStats.CLOSING) {
            survivalGames.getGame().setGameStats(GameStats.CLOSING);
            this.cancel();
            return;
        }
        if (world.isThundering() || world.hasStorm()) {
            world.setThunderDuration(0);
            world.setWeatherDuration(0);
            world.setStorm(false);
        }
        if ((gameStats == GameStats.WAITING || gameStats == GameStats.STARTING) && world.getTime() != 0)
            world.setTime(0);
        switch (survivalGames.getGame().getGameStats()) {
            case WAITING:
                waiting();
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
        }
    }

}
