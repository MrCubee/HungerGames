package fr.mrcubee.survivalgames.listeners.player;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;
import fr.mrcubee.survivalgames.kit.Kit;

public class PlayerDeath implements Listener {

    private SurvivalGames survivalGames;

    public PlayerDeath(SurvivalGames survivalGames) {
        this.survivalGames = survivalGames;
    }

    private void sendDeathMessage(Player deathPlayer) {
        Kit kit = survivalGames.getGame().getKitManager().getKitByPlayer(deathPlayer);
        String kitName = "No Kit";
        int players = survivalGames.getServer().getOnlinePlayers().size() - survivalGames.getGame().getNumberSpectator();
        Set<Player> playerInGame = survivalGames.getGame().getPlayerInGame();

        if (kit != null)
            kitName = kit.getName();
        if (players > 1) {
            survivalGames.getServer().broadcastMessage(ChatColor.RED + deathPlayer.getName() + " (" + ChatColor.GRAY + kitName + ChatColor.RED + ")" + ChatColor.GOLD + " is Dead ! There are " + ChatColor.RED + players + " players left.");
            for (Player player : Bukkit.getOnlinePlayers())
                player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 100, 1);
        } else {
            for (Player player : Bukkit.getOnlinePlayers())
                player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 100, 1);
            survivalGames.getServer().broadcastMessage(ChatColor.RED + deathPlayer.getName() + " (" + ChatColor.GRAY + kitName + ChatColor.RED + ")" + ChatColor.GOLD + " is Dead ! ");
            for (Player player : playerInGame) {
                //survivalGames.getDataBase().updatefinishPlayerData(player, true);
                survivalGames.getServer().broadcastMessage(ChatColor.GOLD + "---> " + ChatColor.RED + player.getName() + ChatColor.GOLD + " win the game. <---");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerDeathEvent(PlayerDeathEvent event) {
        if (survivalGames.getGame().getGameStats() != GameStats.DURING)
            return;
        //survivalGames.getDataBase().updatefinishPlayerData(event.getEntity(), false);
        event.setDeathMessage(null);
        event.getEntity().setMaxHealth(20);
        event.getEntity().setHealth(event.getEntity().getMaxHealth());
        survivalGames.getGame().addSpectator(event.getEntity());
        sendDeathMessage(event.getEntity());
        survivalGames.getGame().getKitManager().removeKit(event.getEntity());
    }
}
