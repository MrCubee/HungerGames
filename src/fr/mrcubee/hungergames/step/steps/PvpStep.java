package fr.mrcubee.hungergames.step.steps;

import fr.mrcubee.annotation.spigot.config.Config;
import fr.mrcubee.langlib.Lang;
import fr.mrcubee.plugin.util.spigot.annotations.PluginAnnotations;
import fr.mrcubee.hungergames.Game;
import fr.mrcubee.hungergames.step.Step;
import fr.mrcubee.hungergames.step.StepUtil;
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
            game.broadcastMessage("step.pvpStep.done.broadcast", "PvP is enabled !", true);
        } else if (seconds == 10 || seconds <= 5) {
            playersPlaySound(Sound.ORB_PICKUP, 100, 1);
            game.broadcastMessage("step.pvpStep.broadcast", "PvP will be active in &c%s second%s", true,
                    seconds, (seconds > 1) ? "s" : "");
        }
    }

    @Override
    public String scoreBoardGameStatus(Player player) {
        return Lang.getMessage(player, "step.pvpStep.scoreboard", "&7PvP in &c%s", true,
                StepUtil.secondToString(getEndSeconds()));
    }

    public static PvpStep create(Game game) {
        if (game == null)
            return null;
        return new PvpStep(game);
    }

}
