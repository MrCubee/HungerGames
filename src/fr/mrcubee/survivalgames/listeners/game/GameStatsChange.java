package fr.mrcubee.survivalgames.listeners.game;

import fr.mrcubee.survivalgames.Game;
import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;
import fr.mrcubee.survivalgames.api.event.GameStatsChangeEvent;
import fr.mrcubee.survivalgames.world.BorderManager;
import net.arkadgames.survivalgame.sql.DataBase;
import net.arkadgames.survivalgame.sql.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class GameStatsChange implements Listener {

    private SurvivalGames survivalGames;

    public GameStatsChange(SurvivalGames survivalGames) {
        this.survivalGames = survivalGames;
    }

    private void updatePlayerStats() {
        DataBase dataBase = this.survivalGames.getDataBase();
        PlayerData playerData;

        if (dataBase == null || this.survivalGames.getGame().getGameStats() != GameStats.DURING)
            return;
        for (Player player : survivalGames.getGame().getPlayerInGame()) {
            playerData = dataBase.getPlayerData(player.getUniqueId());
            if (playerData == null)
                playerData = new PlayerData();
            playerData.play++;
            //survivalGames.getDataBase().setPlayerData(player.getUniqueId(), playerData);
        }
    }

    private void waiting() {
        Game game = this.survivalGames.getGame();

        BorderManager.setCenter(game.getGameWorld(), 0.0D, 0.0D);
        BorderManager.setWorldBorder(game.getGameWorld(), game.getGameSetting().getMaxBorder());
    }

    private void during() {
        Game game = this.survivalGames.getGame();

        game.setGameEndTime(System.currentTimeMillis() + (game.getGameSetting().getTimeBorder() + 20) * 1000);
        game.setTotalPlayers(this.survivalGames.getGame().getNumberPlayer());
        this.survivalGames.getServer().getOnlinePlayers().forEach(player -> {
            player.teleport(this.survivalGames.getGame().getSpawn());
            player.getInventory().clear();
            player.getInventory().addItem(new ItemStack(Material.COMPASS));
            player.getInventory().addItem(new ItemStack(Material.APPLE, 10));
        });
        game.getKitManager().randomKitPlayerWithNotKit();
        game.getKitManager().giveKitToPlayer();
        BorderManager.setWorldBorder(game.getGameWorld(), game.getGameSetting().getMinBorder(), game.getGameSetting().getTimeBorder());
    }

    private void closing() {
        Server server = this.survivalGames.getServer();

        server.getOnlinePlayers().forEach(player -> player.kickPlayer(ChatColor.GOLD + "The server will restart !"));
        server.shutdown();
    }

    @EventHandler
    public void gameStatsChange(GameStatsChangeEvent event) {
        updatePlayerStats();
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
