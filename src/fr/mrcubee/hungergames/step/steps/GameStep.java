package fr.mrcubee.hungergames.step.steps;

import fr.mrcubee.annotation.spigot.config.Config;
import fr.mrcubee.plugin.util.spigot.annotations.PluginAnnotations;
import fr.mrcubee.hungergames.Game;
import fr.mrcubee.hungergames.step.Step;
import org.bukkit.entity.Player;

public class GameStep extends Step {

    @Config(path = "game.step.feast.beforeTime")
    private long beforeTime = 1200;

    private GameStep(Game game) {
        super(game, "before_feast");
        PluginAnnotations.load(game.getPlugin(), this);
        setSecondDuring(this.beforeTime);
    }

    @Override
    public void start() {
        super.start();
        getGame().setPvpEnable(true);
    }

    @Override
    public void update() {

    }

    @Override
    public String scoreBoardGameStatus(Player player) {
        return null;
    }

    public static GameStep create(Game game) {
        if (game == null)
            return null;
        return new GameStep(game);
    }

}
