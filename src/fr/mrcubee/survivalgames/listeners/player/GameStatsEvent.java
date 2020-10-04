package fr.mrcubee.survivalgames.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;
import fr.mrcubee.survivalgames.api.event.GameStatsChangeEvent;
import net.arkadgames.survivalgame.sql.PlayerData;

public class GameStatsEvent implements Listener {
	private SurvivalGames survivalGames;
	
	public GameStatsEvent(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void gameStatsChangeEvent(GameStatsChangeEvent event) {
		PlayerData playerData;
		
		if (event.getGameStats() != GameStats.DURING)
			return;
		for (Player player : survivalGames.getGame().getPlayerInGame()) {
			playerData = survivalGames.getDataBase().getPlayerData(player.getUniqueId());
			if (playerData == null)
				playerData = new PlayerData();
			playerData.play++;
			//survivalGames.getDataBase().setPlayerData(player.getUniqueId(), playerData);
		}
	}
}
