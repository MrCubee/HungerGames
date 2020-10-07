package fr.mrcubee.survivalgames.listeners.server;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import fr.mrcubee.survivalgames.SurvivalGames;

public class PingServer implements Listener {

	private SurvivalGames survivalGames;

	public PingServer(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
	}

	@EventHandler
	public void ping(ServerListPingEvent event) {
		event.setMaxPlayers(survivalGames.getGame().getGameSetting().getMaxPlayer());
		switch (survivalGames.getGame().getGameStats()) {
		case WAITING:
			waitting(event);
			break;
		case STARTING:
			starting(event);
			break;
		case DURING:
			during(event);
			break;
		case STOPPING:
			stopping(event);
			break;
		default:
			break;
		}
	}

	public void waitting(ServerListPingEvent event) {
		event.setMotd(ChatColor.GREEN + "Waiting for players...");
	}

	public void starting(ServerListPingEvent event) {
		long seconds = (this.survivalGames.getGame().getNextStatTime() - System.currentTimeMillis()) / 1000;

		event.setMotd(ChatColor.GOLD + "Launching game in " + ChatColor.GRAY + getTime(seconds));
	}

	public void during(ServerListPingEvent event) {
		long seconds = (this.survivalGames.getGame().getGameEndTime() - System.currentTimeMillis()) / 1000;

		event.setMotd(ChatColor.RED + "Game is currently launched.\n" + ChatColor.GOLD + "End of the game in about: " + ChatColor.GRAY + getTime(seconds));
	}

	public void stopping(ServerListPingEvent event) {
		event.setMotd(ChatColor.RED + "The party is finished restarting the server...");
	}
	
	public static String longToString(long number) {
		if (number > -1 && number < 10)
			return "0" + number;
		return Long.toString(number);
	}

	public static String getTime(long seconds) {
		long s = seconds;
		long m = 0;
		long h = 0;
		
		if (seconds <= 0)
			return "00:00:00";
		if (s >= 60) {
			m = s / 60;
			s = s - (m * 60);
		}
		if (m >= 60) {
			h = m / 60;
			m = m - (h * 60);
		}
		return (longToString(h) + ":" + longToString(m) + ":" + longToString(s));
	}

}
