package fr.mrcubee.survivalgames.step;

import fr.mrcubee.pluginutil.spigot.annotations.PluginAnnotations;
import fr.mrcubee.survivalgames.Game;
import org.bukkit.entity.Player;

public abstract class Step {

    private final Game game;
    private final String name;
    private long startTime;
    private long duringTime;

    protected Step(Game game, String name) {
        this.game = game;
        this.name = name;
        this.startTime = 0;
        this.duringTime = -1;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public void complete() {
        this.startTime = 0;
    }

    public void remove() {
        this.startTime = 0;
    }

    public abstract void update();

    public abstract String scoreBoardGameStatus(Player player);

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public boolean equals(Object obj) {
        return (obj instanceof Step) && obj.hashCode() == hashCode();
    }

    public long getStartTime() {
        return startTime;
    }

    protected void setSecondDuring(long seconds) {
        if (seconds < 0) {
            this.duringTime = -1;
            return;
        }
        this.duringTime = seconds * 1000;
    }

    public long getDuringTime() {
        return this.duringTime / 1000;
    }

    public long getEndSeconds() {
        long time;

        if (this.startTime == 0 || this.duringTime < 0)
            return -1;
        time = System.currentTimeMillis() - this.startTime;
        if (time > this.duringTime)
            return 0;
        return (this.duringTime - time) / 1000;
    }

    public Game getGame() {
        return game;
    }
}
