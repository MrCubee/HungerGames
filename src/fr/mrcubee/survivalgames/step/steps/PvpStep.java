package fr.mrcubee.survivalgames.step.steps;

import fr.mrcubee.pluginutil.spigot.annotations.PluginAnnotations;
import fr.mrcubee.pluginutil.spigot.annotations.config.Config;
import fr.mrcubee.survivalgames.Game;
import fr.mrcubee.survivalgames.step.Step;
import fr.mrcubee.survivalgames.step.StepUtil;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PvpStep extends Step {

    @Config(path = "game.step.pvpTime")
    private long timePvp = 300;

    private PvpStep(Game game) {
        super(game, "pvp");
        PluginAnnotations.load(game.getPlugin(), this);
        setSecondDuring(this.timePvp);
    }

    private void playersPlaySound(Sound sound, float volume, float pitch) {
        getGame().getPlugin().getServer().getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), sound, volume, pitch);
        });
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void complete() {
        super.complete();
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    public void update() {
        Game game = getGame();
        long seconds = getEndSeconds();

        if (seconds <= 0) {
            playersPlaySound(Sound.ORB_PICKUP, 100, 2);
            game.setPvpEnable(true);
            game.broadcastMessage(ChatColor.GOLD + "PvP is enabled !");
        } else if (seconds == 10 || seconds <= 5) {
            playersPlaySound(Sound.ORB_PICKUP, 100, 1);
            game.broadcastMessage("PvP will be active in " + ChatColor.RED + seconds + " second" + ((seconds > 1) ? "s" : ""));
        }
    }

    @Override
    public String scoreBoardGameStatus(Player player) {
        return ChatColor.GRAY.toString() + "PvP in " + ChatColor.RED.toString() +  StepUtil.secondToString(getEndSeconds());
    }

    public static PvpStep create(Game game) {
        if (game == null)
            return null;
        return new PvpStep(game);
    }

}
