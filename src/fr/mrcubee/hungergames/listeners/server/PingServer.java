package fr.mrcubee.hungergames.listeners.server;

import fr.mrcubee.hungergames.step.StepUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import fr.mrcubee.hungergames.HungerGames;

public class PingServer implements Listener {

	private HungerGames survivalGames;

	public PingServer(HungerGames survivalGames) {
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

		event.setMotd(ChatColor.GOLD + "Launching game in " + ChatColor.GRAY + StepUtil.secondToString(seconds));
	}

	public void during(ServerListPingEvent event) {
		long seconds = (this.survivalGames.getGame().getGameEndTime() - System.currentTimeMillis()) / 1000;

		event.setMotd(ChatColor.RED + "Game is currently launched.\n" + ChatColor.GOLD + "End of the game in about: " + ChatColor.GRAY +  StepUtil.secondToString(seconds));
	}

	public void stopping(ServerListPingEvent event) {
		event.setMotd(ChatColor.RED + "The party is finished restarting the server...");
	}

}
