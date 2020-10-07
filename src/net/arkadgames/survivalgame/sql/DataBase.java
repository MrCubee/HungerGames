package net.arkadgames.survivalgame.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;

import fr.mrcubee.scoreboard.Score;
import org.bukkit.entity.Player;

import fr.mrcubee.survivalgames.SurvivalGames;

public class DataBase {

	private SurvivalGames survivalGames;
	private Connection connection;

	public DataBase(SurvivalGames survivalGames) throws SQLException {
		connection = DriverManager.getConnection("jdbc:mysql://localhost", "arkadgames", "password");
		this.survivalGames = survivalGames;
	}

	public boolean hasPlayerData(UUID uuid) {
		PreparedStatement statement;
		ResultSet resultSet;

		if (uuid == null || connection == null)
			return false;
		try {
			statement = connection.prepareStatement("SELECT uuid FROM survivalgames WHERE uuid = ?");
			statement.setString(1, uuid.toString());
			resultSet = statement.executeQuery();

			return resultSet.next();
		} catch (SQLException e) {}
		return false;
	}

	public PlayerData getPlayerData(UUID uuid) {
		PlayerData playerData;
		PreparedStatement statement;
		ResultSet resultSet;

		if (uuid == null || connection == null)
			return null;
		try {
			statement = connection.prepareStatement("SELECT last_win, win, play, total_time, player_kill, last_connection FROM survivalgames WHERE uuid = ?");
			statement.setString(1, uuid.toString());
			resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			playerData = new PlayerData();
			playerData.lastWin = resultSet.getString("last_win").equals("YES");
			playerData.win = resultSet.getLong("win");
			playerData.play = resultSet.getLong("play");
			playerData.totalTime = resultSet.getLong("total_time");
			playerData.playerKill = resultSet.getLong("player_kill");
			playerData.lastConnection = resultSet.getDate("last_connection");
			return (playerData);
		} catch (SQLException e) {}
		return null;
	}

	private boolean registerPlayerData(UUID uuid, PlayerData playerData) {
		PreparedStatement statement;

		if (uuid == null || playerData == null || connection == null)
			return false;
		try {
			statement = connection.prepareStatement("INSERT survivalgames VALUES (?, ?, ?, ?, ?, ?, ?)");
			statement.setString(1, uuid.toString());
			statement.setString(2, (playerData.lastWin) ? "YES" : "NO");
			statement.setLong(3, playerData.win);
			statement.setLong(4, playerData.play);
			statement.setLong(5, playerData.totalTime);
			statement.setLong(6, playerData.playerKill);
			statement.setDate(7, playerData.lastConnection);
			statement.execute();
			return true;
		} catch (SQLException e) {}
		return false;
	}

	private boolean updatePlayerData(UUID uuid, PlayerData playerData) {
		PreparedStatement statement;

		if (uuid == null || playerData == null || connection == null)
			return false;
		try {
			statement = connection.prepareStatement("UPDATE survivalgames SET last_win = ?, win = ?, play = ?, total_time = ?, player_kill = ?, last_connection = ? WHERE uuid = ?");
			statement.setString(1, (playerData.lastWin) ? "YES" : "NO");
			statement.setLong(2, playerData.win);
			statement.setLong(3, playerData.play);
			statement.setLong(4, playerData.totalTime);
			statement.setLong(5, playerData.playerKill);
			statement.setDate(6, playerData.lastConnection);
			statement.setString(7, uuid.toString());
			statement.execute();
			return true;
		} catch (SQLException e) {}
		return false;
	}

	public boolean setPlayerData(UUID uuid, PlayerData playerData) {
		if (hasPlayerData(uuid))
			return updatePlayerData(uuid, playerData);
		return registerPlayerData(uuid, playerData);
	}

	public void updatefinishPlayerData(Player player, boolean win) {
		Score score;
		PlayerData playerData;

		if (player == null)
			return;
		playerData = survivalGames.getDataBase().getPlayerData(player.getUniqueId());
		if (playerData == null)
			playerData = new PlayerData();
		playerData.lastWin = win;
		playerData.totalTime += (this.survivalGames.getGame().getGameEndTime() - System.currentTimeMillis()) / 60000;
		score = survivalGames.getGame().getPluginScoreBoardManager().getKillObjective().getScore(player.getName());
		playerData.playerKill += (score != null) ? score.getScore() : 0;
		if (win)
			playerData.win++;
		playerData.lastConnection = new Date(Calendar.getInstance().getTime().getTime());
		survivalGames.getDataBase().setPlayerData(player.getUniqueId(), playerData);
	}

	public void close() throws SQLException {
		if (connection != null)
			connection.close();
	}

}
