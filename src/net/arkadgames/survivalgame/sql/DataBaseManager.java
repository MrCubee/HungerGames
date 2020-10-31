package net.arkadgames.survivalgame.sql;

import fr.mrcubee.survivalgames.SurvivalGames;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataBaseManager {

    private final DataBase dataBase;
    private final Map<UUID, PlayerData> playersData;

    public DataBaseManager(SurvivalGames survivalGames) {
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

}
