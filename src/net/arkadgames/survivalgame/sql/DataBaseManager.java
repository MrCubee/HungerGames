package net.arkadgames.survivalgame.sql;

import fr.mrcubee.survivalgames.SurvivalGames;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataBaseManager {

    private final SurvivalGames survivalGames;
    private final DataBase dataBase;
    private final Map<UUID, PlayerData> playersData;
    private BukkitTask task;

    public DataBaseManager(SurvivalGames survivalGames) {
        this.survivalGames = survivalGames;
        this.dataBase = new DataBase(survivalGames);
        this.playersData = new HashMap<UUID, PlayerData>();
    }

    public PlayerData getPlayerData(UUID uuid) {
        PlayerData result;

        if (uuid == null)
            return null;
        result = this.playersData.get(uuid);
        if (result == null) {
            result = this.dataBase.getPlayerData(uuid);
            if (result == null)
                result = new PlayerData();
            this.playersData.put(uuid, result);
        }
        return result;
    }

    public boolean sendPlayerData(UUID uuid) {
        if (uuid == null)
            return false;
        return this.dataBase.setPlayerData(uuid, this.playersData.remove(uuid));
    }

    public boolean sendPlayerInfo(UUID uuid) {
        return this.dataBase.setPlayerInfo(uuid);
    }

    public void sendAllPlayerDataAsync() {
        if (this.task != null)
            return;
        this.task = new BukkitRunnable() {

            @Override
            public void run() {
                Map<UUID, PlayerData> playerDataCopy = new HashMap<UUID, PlayerData>(DataBaseManager.this.playersData);

                playerDataCopy.forEach((uuid, data) -> {
                    sendPlayerData(uuid);
                });
                playerDataCopy.clear();
                DataBaseManager.this.task = null;
            }

        }.runTaskAsynchronously(this.survivalGames);
    }

    public boolean isEmpty() {
        return this.playersData.isEmpty();
    }

}
