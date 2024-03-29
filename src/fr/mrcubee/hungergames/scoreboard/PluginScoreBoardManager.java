package fr.mrcubee.hungergames.scoreboard;

import java.util.*;
import java.util.stream.Stream;

import fr.mrcubee.bukkit.scoreboard.ObjectiveLocation;
import fr.mrcubee.scoreboard.CustomSideBar;
import fr.mrcubee.scoreboard.Objective;
import fr.mrcubee.hungergames.Game;
import net.arkadgames.hungergames.sql.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PluginScoreBoardManager extends BukkitRunnable {

    private final Game game;
    private final Map<UUID, CustomSideBar> playerObjective;
    private final Objective killObjective;
    private final Objective rankObjective;

    public PluginScoreBoardManager(Game game) {
        this.game = game;
        this.playerObjective = new HashMap<UUID, CustomSideBar>();
        this.killObjective = Objective.create("kill", "kill");
        this.rankObjective = Objective.create("rank","#");
        this.killObjective.setLocation(ObjectiveLocation.LIST);
        this.rankObjective.setLocation(ObjectiveLocation.BELOW_NAME);
    }

    public CustomSideBar getPlayerSideBar(Player player) {
        final String scoreBoardDisplayName = ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + game.getPlugin().getDescription().getName();
        CustomSideBar objective;

        if (player == null)
            return null;
        objective = this.playerObjective.get(player.getUniqueId());
        if (objective == null) {
            objective = CustomSideBar.create("sidebar", scoreBoardDisplayName);
            if (objective != null)
                this.playerObjective.put(player.getUniqueId(), objective);
            objective.getReceivers().add(player);
            this.killObjective.getReceivers().add(player);
            this.rankObjective.getReceivers().add(player);
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
        this.rankObjective.getReceivers().remove(offlinePlayer);
        return true;
    }

    public Stream<CustomSideBar> getSideBarStreams() {
        return this.playerObjective.values().stream();
    }

    public Objective getKillObjective() {
        return this.killObjective;
    }

    public Objective getRankObjective() {
        return this.rankObjective;
    }

    @Override
    public void run() {
        this.playerObjective.values().stream().forEach(sidebar -> {
            Iterator<OfflinePlayer> offlinePlayerIterator = sidebar.getReceivers().iterator();
            OfflinePlayer offlinePlayer;
            Player player;
            PlayerData playerData;

            if (!offlinePlayerIterator.hasNext())
                return;
            offlinePlayer = offlinePlayerIterator.next();
            if (offlinePlayer == null || (player = offlinePlayer.getPlayer()) == null)
                return;
            playerData = this.game.getDataBaseManager().getPlayerData(player.getUniqueId());
            if (playerData != null)
                getRankObjective().setScore(player.getName(), playerData.getRank());
            sidebar.setLines(ScoreboardBuilder.build(this.game, player, playerData));
        });
    }
}
