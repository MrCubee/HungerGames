package fr.mrcubee.survivalgames.scoreboardmanager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class PluginScoreBoardManager {

    private Scoreboard scoreboard;
    private Objective objectiveSideBar;
    private Objective objectiveHealth;
    private Objective objectiveKill;
    private Map<Integer, String> lines;

    public PluginScoreBoardManager(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        this.lines = new HashMap<Integer, String>();
        this.objectiveSideBar = getObjective("SurvivalGames", "dummy");
        this.objectiveSideBar.setDisplayName(ChatColor.GOLD + "SurvivalGames");
        this.objectiveSideBar.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objectiveHealth = getObjective("health", "health");
        this.objectiveHealth.setDisplaySlot(DisplaySlot.BELOW_NAME);
        this.objectiveKill = getObjective("kill", "playerKillCount");
        this.objectiveKill.setDisplaySlot(DisplaySlot.PLAYER_LIST);
    }

    private Objective getObjective(String name, String type) {
        Objective objective = scoreboard.getObjective(name);
        if (objective != null)
            objective.unregister();
        objective = scoreboard.registerNewObjective(name, type);
        return objective;
    }

    public void setDisplayName(String name) {
        if ((name == null) || (name.length() < 1))
            return;
        this.objectiveSideBar.setDisplayName(name);
    }

    public void putLine(String line, int value) {
        if ((line == null) || (line.length() < 1))
            return;
        if (lines.containsKey(value))
            removeLine(value);
        lines.put(value, line);
        this.objectiveSideBar.getScore(line).setScore(value);
    }

    public void removeLine(int value) {
        if (!lines.containsKey(value))
            return;
        String line = lines.get(value);
        scoreboard.resetScores(line);
        lines.remove(value);
    }

    public void clearLine() {
        try {
            for (int key : lines.keySet()) {
                removeLine(key);
            }
        } catch (Exception e) {}
    }

    public void addPlayer(Player player) {
        if (player != null)
            player.setScoreboard(scoreboard);
    }

    public Objective getObjectiveKill() {
        return objectiveKill;
    }

}
