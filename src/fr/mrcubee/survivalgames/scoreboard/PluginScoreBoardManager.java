package fr.mrcubee.survivalgames.scoreboard;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Stream;

import fr.mrcubee.scoreboard.CustomSideBar;
import fr.mrcubee.scoreboard.Objective;
import fr.mrcubee.scoreboard.ObjectiveLocation;
import fr.mrcubee.survivalgames.Game;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PluginScoreBoardManager extends BukkitRunnable {

    private final Game game;
    private ScheduledExecutorService scheduler;
    private Map<UUID, CustomSideBar> playerObjective;
    private Objective killObjective;
    private int oldCount;

    public PluginScoreBoardManager(Game game) {
        this.game = game;
        this.playerObjective = new HashMap<UUID, CustomSideBar>();
        this.killObjective = Objective.create("kill", "kill");
        this.killObjective.setLocation(ObjectiveLocation.LIST);
        this.oldCount = 0;
    }

    public CustomSideBar getPlayerSideBar(Player player) {
        final String scoreBoardDisplayName = ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "SurvivalGames";
        CustomSideBar objective = this.playerObjective.get(player.getUniqueId());

        if (player == null)
            return null;
        if (objective == null) {
            objective = CustomSideBar.create("sidebar", scoreBoardDisplayName);
            if (objective != null)
                this.playerObjective.put(player.getUniqueId(), objective);
            objective.getReceivers().add(player);
            this.killObjective.getReceivers().add(player);
        }
        return objective;
    }

    public boolean removePlayerSideBar(OfflinePlayer offlinePlayer) {
        CustomSideBar customSideBar;

        if (offlinePlayer == null)
            return false;
        customSideBar = this.playerObjective.remove(offlinePlayer.getUniqueId());
        if (customSideBar != null)
            customSideBar.getReceivers().remove(offlinePlayer);
        this.killObjective.getReceivers().remove(offlinePlayer);
        return true;
    }

    public Stream<CustomSideBar> updateSideBar() {
        return this.playerObjective.values().stream();
    }

    public Objective getKillObjective() {
        return killObjective;
    }

    @Override
    public void run() {
        this.playerObjective.values().stream().forEach(sidebar -> {
            Iterator<OfflinePlayer> offlinePlayerIterator = sidebar.getReceivers().iterator();
            OfflinePlayer offlinePlayer;
            Player player;
            List<String> lines;
            int count;

            if (!offlinePlayerIterator.hasNext())
                return;
            offlinePlayer = offlinePlayerIterator.next();
            if (offlinePlayer == null || (player = offlinePlayer.getPlayer()) == null)
                return;
            lines = ScoreboardBuilder.build(this.game, player);
            if (lines == null || lines.isEmpty()) {
                this.oldCount = 0;
            }
            count = lines.size();
            for (String line : lines)
                sidebar.setLine(count--, line);
            for (int i = count + 1; i <= this.oldCount; i++)
                sidebar.removeLine(i);
        });
    }
}
