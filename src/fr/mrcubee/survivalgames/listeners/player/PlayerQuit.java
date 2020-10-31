package fr.mrcubee.survivalgames.listeners.player;

import fr.mrcubee.scoreboard.Score;
import fr.mrcubee.survivalgames.Game;
import net.arkadgames.survivalgame.sql.DataBaseManager;
import net.arkadgames.survivalgame.sql.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.mrcubee.survivalgames.SurvivalGames;

public class PlayerQuit implements Listener {

    private final SurvivalGames survivalGames;

    public PlayerQuit(SurvivalGames survivalGames) {
        this.survivalGames = survivalGames;
    }

    private void updatePlayerData(Game game, Player player) {
        PlayerData playerData;
        Score killScore;

        if (game == null || player == null)
            return;
        killScore = game.getPluginScoreBoardManager().getKillObjective().getScore(player.getName());
        playerData = game.getDataBaseManager().getPlayerData(player.getUniqueId());
        if (playerData != null)
            playerData.setKill(playerData.getKill() + ((killScore == null) ? 0 : killScore.getScore()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerQuitEvent(PlayerQuitEvent event) {
        Game game = this.survivalGames.getGame();
        DataBaseManager dataBaseManager = game.getDataBaseManager();

        if (game.getGameStats().ordinal() > 2) {
            if (!game.isSpectator(event.getPlayer()) && event.getPlayer().getHealth() > 0)
                event.getPlayer().setHealth(0);
            game.addSpectator(event.getPlayer());
            game.getPluginScoreBoardManager().removePlayerSideBar(event.getPlayer());
            updatePlayerData(game, event.getPlayer());
            if (game.getGameStats().ordinal() < 4)
                dataBaseManager.sendPlayerData(event.getPlayer().getUniqueId());
        } else {
            event.setQuitMessage(ChatColor.RED + "[-] " + event.getPlayer().getName());
            dataBaseManager.sendPlayerInfo(event.getPlayer().getUniqueId());
        }
    }
}
