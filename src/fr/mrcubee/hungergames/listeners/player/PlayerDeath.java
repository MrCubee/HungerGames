package fr.mrcubee.hungergames.listeners.player;

import java.util.Set;

import fr.mrcubee.hungergames.HungerGames;
import fr.mrcubee.langlib.Lang;
import fr.mrcubee.scoreboard.Score;
import fr.mrcubee.hungergames.Game;
import fr.mrcubee.hungergames.kit.KitManager;
import net.arkadgames.hungergames.sql.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.mrcubee.hungergames.GameStats;
import fr.mrcubee.hungergames.kit.Kit;

public class PlayerDeath implements Listener {

    private final HungerGames survivalGames;

    public PlayerDeath(HungerGames survivalGames) {
        this.survivalGames = survivalGames;
    }

    private String getKitsName(KitManager kitManager, Player player, Player displayPlayer) {
        Kit[] kits;
        StringBuilder stringBuilder;

        if (kitManager == null || player == null)
            return null;
        if (displayPlayer == null)
            displayPlayer = player;
        kits = kitManager.getKitByPlayer(player);
        if (kits == null || kits.length <= 0)
            return Lang.getMessage(displayPlayer, "kit.noKit.name", "No kit", true);
        stringBuilder = new StringBuilder();
        stringBuilder.append(kits[0].getDisplayName(displayPlayer));
        for (int i = 1; i < kits.length; i++) {
            stringBuilder.append(ChatColor.GRAY.toString());
            stringBuilder.append(", ");
            stringBuilder.append(kits[i].getDisplayName(displayPlayer));
        }
        return stringBuilder.toString();
    }

    private void victory(Game game) {
        Set<Player> players;

        if (game == null)
            return;
        players = game.getPlayerInGame();
        if (players == null || players.isEmpty())
            return;
        players.forEach(player -> {
            PlayerData playerData = game.getDataBaseManager().getPlayerData(player.getUniqueId());

            if (playerData != null) {
                playerData.setLastWin(true);
                playerData.setWin(playerData.getWin() + 1);
                playerData.setPlayTime(playerData.getPlayTime() + ((System.currentTimeMillis() - game.getGameStartTime()) / 1000));
            }
            game.broadcastMessage("broadcast.player.win", "&c%s &6WIN THE GAME !!!", true, player.getName());
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerDeathEvent(PlayerDeathEvent event) {
        Game game = this.survivalGames.getGame();
        KitManager kitManager;
        PlayerData playerData;
        Score score;

        if (game.getGameStats() != GameStats.DURING)
            return;
        if (event.getEntity().getKiller() != null) {
            score = game.getPluginScoreBoardManager().getKillObjective().getScore(event.getEntity().getKiller().getName());
            game.getPluginScoreBoardManager().getKillObjective().setScore(event.getEntity().getKiller().getName(),
            (score == null) ? 1 : score.getScore() + 1);
        }
        kitManager = game.getKitManager();
        playerData = game.getDataBaseManager().getPlayerData(event.getEntity().getUniqueId());
        if (playerData != null) {
            playerData.setLastWin(false);
            playerData.setPlayTime(playerData.getPlayTime() + ((System.currentTimeMillis() - game.getGameStartTime()) / 1000));
        }
        event.getDrops().removeIf(itemStack -> !kitManager.canLostItem(event.getEntity(), itemStack));
        event.setDeathMessage(null);
        event.getEntity().setMaxHealth(20);
        event.getEntity().setHealth(event.getEntity().getMaxHealth());
        game.addSpectator(event.getEntity());
        if (game.getNumberPlayer() > 1) {
            event.setDeathMessage(null);
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 100, 1);
                player.sendMessage(Lang.getMessage(player, "broadcast.player.death", "&c%s(&7%s&c) &6is Dead ! There are &c%d players left.", true,
                        event.getEntity().getName(), getKitsName(kitManager, event.getEntity(), player), game.getNumberPlayer()));
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 100, 1);
                player.sendMessage(Lang.getMessage(player, "broadcast.player.lastDeath", "&c%s(&7%s&c) &6is Dead !", true,
                        event.getEntity().getName(), getKitsName(kitManager, event.getEntity(), player)));
            }
            victory(game);
        }
        game.getKitManager().removeKit(event.getEntity());
    }
}
