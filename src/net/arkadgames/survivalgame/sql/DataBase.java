package net.arkadgames.survivalgame.sql;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import fr.mrcubee.annotation.spigot.config.Config;
import fr.mrcubee.plugin.util.spigot.annotations.PluginAnnotations;

import fr.mrcubee.survivalgames.SurvivalGames;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;

public class DataBase {

	private final SurvivalGames survivalGames;

	@Config(path = "db.enable")
	private boolean enable;

	@Config(path = "db.host")
	private String host;

	@Config(path = "db.database")
	private String dataBase;

	@Config(path = "db.user")
	private String user;

	@Config(path = "db.password")
	private String password;

	private Connection connection;

	protected DataBase(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
		this.enable = false;
		PluginAnnotations.load(survivalGames, this);
		getConnection();
	}

	protected Connection getConnection() {
		if (!this.enable)
			return null;
		try {
			if (this.connection == null || this.connection.isClosed()) {
				this.survivalGames.getLogger().info("Connecting to " + this.toString() + "...");
				this.connection = null;
				this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + "/" + this.dataBase
						+ "?characterEncoding=utf8", this.user, this.password);
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return this.connection;
	}

	public void disconnect() {
		try {
			if (this.connection == null || this.connection.isClosed()) {
				this.connection.close();
				this.connection = null;
			}
		} catch (SQLException ignored) {}
	}

	private boolean setPlayerHead(UUID uuid) {
		BufferedImage bufferedImage;
		ByteArrayOutputStream baos;
		byte[] imageInByte = null;
		PreparedStatement statement;

		if (uuid == null)
			return false;
		try {
			bufferedImage = ImageIO.read(new URL("https://cravatar.eu/avatar/" + uuid.toString() + "/128"));
			baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", baos);
			imageInByte = baos.toByteArray();
		} catch (IOException ignored) {}
		if (imageInByte == null)
			return false;
		connection = getConnection();
		if (connection == null)
			return !this.enable;
		try {
			statement = connection.prepareStatement("INSERT playerhead VALUES (?, ?) ON DUPLICATE KEY UPDATE data = ?");
			statement.setString(1, uuid.toString().replaceAll("-", ""));
			statement.setBytes(2, imageInByte);
			statement.setBytes(3, imageInByte);
			return statement.execute();
		} catch (SQLException ignored) {
			ignored.printStackTrace();
		}
		return false;
	}

	private boolean setPlayerName(UUID uuid) {
		Player player;
		PreparedStatement statement;

		if (uuid == null)
			return false;
		player = Bukkit.getPlayer(uuid);
		if (player == null)
			return false;
		connection = getConnection();
		if (connection == null)
			return !this.enable;
		try {
			statement = connection.prepareStatement("INSERT playername VALUES (?, ?) ON DUPLICATE KEY UPDATE name = ?");
			statement.setString(1, uuid.toString().replaceAll("-", ""));
			statement.setString(2, player.getName());
			statement.setString(3, player.getName());
			return statement.execute();
		} catch (SQLException ignored) {
			ignored.printStackTrace();
		}
		return false;
	}

	public boolean setPlayerInfo(UUID uuid) {
		return setPlayerName(uuid) && setPlayerHead(uuid);
	}

	public boolean hasPlayerData(UUID uuid) {
		Connection connection;
		PreparedStatement statement;
		ResultSet resultSet;

		if (uuid == null)
			return false;
		connection = getConnection();
		if (connection == null)
			return !this.enable;
		try {
			statement = connection.prepareStatement("SELECT uuid FROM survivalgames WHERE uuid = ?");
			statement.setString(1, uuid.toString().replaceAll("-", ""));
			resultSet = statement.executeQuery();
			return resultSet.next();
		} catch (SQLException ignored) {
			ignored.printStackTrace();
		}
		return false;
	}

	public PlayerData getPlayerData(UUID uuid) {
		PlayerData playerData;
		PreparedStatement statement;
		ResultSet resultSet;

		if (uuid == null)
			return null;
		connection = getConnection();
		if (connection == null)
			return null;
		try {
			statement = connection.prepareStatement("SELECT lastwin, play, win, kills, time, survivalgames_rank(survivalgames_ratio(uuid)) AS 'rank' FROM survivalgames WHERE uuid = ?");
			statement.setString(1, uuid.toString().replaceAll("-", ""));
			resultSet = statement.executeQuery();
			if (!resultSet.next()) {
				playerData = new PlayerData();
				playerData.setCanSaveInDB(true);
				return playerData;
			}
			playerData = new PlayerData();
			playerData.setLastWin(resultSet.getBoolean(1));
			playerData.setPlay(resultSet.getInt(2));
			playerData.setWin(resultSet.getInt(3));
			playerData.setKill(resultSet.getInt(4));
			playerData.setPlayTime(resultSet.getLong(5));
			playerData.setRank(resultSet.getInt(6));
			playerData.setCanSaveInDB(true);
			return playerData;
		} catch (SQLException ignored) {
			ignored.printStackTrace();
		}
		return null;
	}

	public boolean setPlayerData(UUID uuid, PlayerData playerData) {
		Connection connection;
		PreparedStatement statement;

		if (uuid == null || playerData == null || !playerData.canSaveInDB())
			return false;
		setPlayerName(uuid);
		setPlayerHead(uuid);
		connection = getConnection();
		if (connection == null)
			return false;
		try {
			statement = connection.prepareStatement("INSERT survivalgames VALUES (?, ?, ?, ?, ?, ?, DEFAULT) ON DUPLICATE KEY UPDATE lastwin = ?, play = ?, win = ?, kills = ?, time = ?");
			statement.setString(1, uuid.toString().replaceAll("-", ""));
			statement.setBoolean(2, playerData.isLastWin());
			statement.setInt(3, playerData.getPlay());
			statement.setInt(4, playerData.getWin());
			statement.setInt(5, playerData.getKill());
			statement.setLong(6, playerData.getPlayTime());
			statement.setBoolean(7, playerData.isLastWin());
			statement.setInt(8, playerData.getPlay());
			statement.setInt(9, playerData.getWin());
			statement.setInt(10, playerData.getKill());
			statement.setLong(11, playerData.getPlayTime());
			return statement.execute();
		} catch (SQLException ignored) {
			ignored.printStackTrace();
		}
		return false;
	}

	public boolean close() {
		try {
			if (this.connection == null || this.connection.isClosed())
				return true;
			this.connection.close();
			return true;
		} catch (SQLException ignored) {}
		return false;
	}

	@Override
	public String toString() {
		return "DataBase{" +
				"survivalGames=" + this.survivalGames +
				", host='" + this.host + '\'' +
				", dataBase='" + this.dataBase + '\'' +
				", user='" + this.user + '\'' +
				", connection=" + this.connection +
				'}';
	}
}
