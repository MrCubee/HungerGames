package fr.mrcubee.survivalgames.listeners.game;

import fr.mrcubee.survivalgames.Game;
import fr.mrcubee.survivalgames.SurvivalGames;
import fr.mrcubee.survivalgames.api.event.GameStatsChangeEvent;
import fr.mrcubee.survivalgames.world.BorderManager;
import net.arkadgames.survivalgame.sql.DataBaseManager;
import net.arkadgames.survivalgame.sql.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class GameStatsChange implements Listener {

    private SurvivalGames survivalGames;

    public GameStatsChange(SurvivalGames survivalGames) {
        this.survivalGames = survivalGames;
    }

    private void waiting() {
        Game game = this.survivalGames.getGame();

        BorderManager.setCenter(game.getGameWorld(), 0.0D, 0.0D);
        BorderManager.setWorldBorder(game.getGameWorld(), game.getGameSetting().getMaxBorder());
    }

    private void during() {
        Game game = this.survivalGames.getGame();

        this.survivalGames.getServer().getOnlinePlayers().forEach(player -> {
            PlayerData playerData = this.survivalGames.getGame().getDataBaseManager().getPlayerData(player.getUniqueId());

            if (playerData != null)
                playerData.setPlay(playerData.getPlay() + 1);
            player.getInventory().clear();
            player.setVelocity(new Vector(0, 0, 0));
            player.setFireTicks(0);
            player.teleport(this.survivalGames.getGame().getSpawn());
            player.setHealth(player.getMaxHealth());
            player.getInventory().addItem(new ItemStack(Material.APPLE, 10));
        });
        game.getKitManager().giveKitToPlayer();
        BorderManager.setWorldBorder(game.getGameWorld(), game.getGameSetting().getMinBorder(), game.getGameSetting().getTimeBorder());
    }

    private void closing() {
        Server server = this.survivalGames.getServer();
        DataBaseManager dataBaseManager = this.survivalGames.getGame().getDataBaseManager();

        server.getOnlinePlayers().forEach(player -> player.kickPlayer(ChatColor.GOLD + "The server will restart !"));
        dataBaseManager.sendAllPLayerDataAsync();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void gameStatsChange(GameStatsChangeEvent event) {
        switch (event.getGameStats()) {
            case WAITING:
                waiting();
                break;
            case DURING:
                during();
                break;
            case CLOSING:
                closing();
                break;
        }
    }

}
