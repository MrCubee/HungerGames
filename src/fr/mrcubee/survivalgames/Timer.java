package fr.mrcubee.survivalgames;

import fr.mrcubee.langlib.Lang;
import fr.mrcubee.survivalgames.step.Step;
import fr.mrcubee.survivalgames.step.StepManager;
import net.arkadgames.survivalgame.sql.DataBaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable {

    private final SurvivalGames survivalGames;
    private final Game game;
    private final StepManager stepManager;
    private final DataBaseManager dataBaseManager;

    protected Timer(SurvivalGames survivalGames) {
        this.survivalGames = survivalGames;
        this.game = this.survivalGames.getGame();
        this.stepManager = this.game.getStepManager();
        this.dataBaseManager = this.game.getDataBaseManager();
    }

    private void playersPlaySound(Sound sound, float volume, float pitch) {
        if (sound == null)
            return;
        this.survivalGames.getServer().getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), sound, volume, pitch);
        });
    }

    private void broadcastMessage(String messageId, String rescueMessage, boolean color, Object... objects) {
        if (messageId == null || rescueMessage == null)
            return;
        for (Player player : Bukkit.getOnlinePlayers())
            player.sendMessage(Lang.getMessage(player, messageId, rescueMessage, color, objects));
    }

    private void waiting() {
        if (this.game.isForceStart() || this.game.getNumberPlayer() >= this.game.getGameSetting().getMinPlayer())
            this.game.setGameStats(GameStats.STARTING);
    }

    private void starting() {
        long seconds;

        if (!this.game.isForceStart() && this.game.getNumberPlayer() < this.game.getGameSetting().getMinPlayer()) {
            this.game.setGameStats(GameStats.WAITING);
            return;
        }
        seconds = (this.game.getNextStatTime() - System.currentTimeMillis()) / 1000;
        if (seconds <= 0) {
            playersPlaySound(Sound.ORB_PICKUP, 100, 2);
            this.game.setGameStats(GameStats.DURING);
        } else if (seconds == 10 || seconds <= 5) {
            playersPlaySound(Sound.ORB_PICKUP, 100, 1);
            game.broadcastMessage("broadcast.starting", "&6The game starts in &c%s second%s", true,
                    seconds, (seconds > 1) ? "s" : "");
        }
    }

    private void during() {
        Step step = this.stepManager.getCurrentStep();

        if (step != null) {
            step.update();
            if (step.getEndSeconds() == 0)
                this.stepManager.nextStep();
        }
        if (this.game.getNumberPlayer() <= 1)
            this.game.setGameStats(GameStats.STOPPING);
    }

    private void stopping() {
        long seconds;

        if (this.survivalGames.getServer().getOnlinePlayers().isEmpty()) {
            this.game.setGameStats(GameStats.CLOSING);
            return;
        }
        seconds = (this.game.getNextStatTime() - System.currentTimeMillis()) / 1000;
        if (seconds <= 0)
            this.game.setGameStats(GameStats.CLOSING);
        else if (seconds == 10 || seconds <= 5) {
            playersPlaySound(Sound.ORB_PICKUP, 100, 1);
            game.broadcastMessage("broadcast.restarting", "&6Restart the server in &c%s second%s", true,
                    seconds, (seconds > 1) ? "s" : "");
        }
    }

    @Override
    public void run() {
        World world = this.game.getGameWorld();
        GameStats gameStats;

        if ((gameStats = this.game.getGameStats()) == GameStats.CLOSING) {
            this.game.setGameStats(GameStats.CLOSING);
            if (this.dataBaseManager.isEmpty()) {
                this.survivalGames.getServer().shutdown();
                cancel();
            }
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
