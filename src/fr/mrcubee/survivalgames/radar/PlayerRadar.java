package fr.mrcubee.survivalgames.radar;

import fr.mrcubee.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.mrcubee.survivalgames.Game;
import fr.mrcubee.survivalgames.api.event.PlayerRadarEvent;

/**
 * 
 * @author MrCubee
 *
 */
public class PlayerRadar {
	
	/**
	 * Retrieves the {@code Player} closest to Radar who is not a spectator.
	 * And returns to Radar the distance between the 2 players, then sets the direction on his compass.
	 *
	 * @param game the instance of the game to recover the speculators.
	 * @param radar the player who uses the radar.
	 * @since 1.7
	 */
	public static void radar(Game game, Player radar) {
		Location radarLocation;
		Player best = null;
		Location bestLocation = null;
		double bestDistance = 0;
		double distance = 0;
		PlayerRadarEvent playerRadarEvent;
		
		if (game == null || radar == null)
			return;
		radarLocation = radar.getLocation();
		for (Player player : game.getPlayerInGame()) {
			if (player.isOnline() && !player.equals(radar)) {
				playerRadarEvent = new PlayerRadarEvent(radar, player);
				Bukkit.getServer().getPluginManager().callEvent(playerRadarEvent);
				distance = playerRadarEvent.getTargetLocation().distance(radarLocation);
				if (!playerRadarEvent.isCancelled() && (bestLocation == null || distance < bestDistance) && distance > 6) {
					best = player;
					bestLocation = playerRadarEvent.getTargetLocation();
					bestDistance = distance;
				}
			}
		}
		if (best == null || bestLocation == null)
			return;
		radar.setCompassTarget(bestLocation);
		if (PlayerUtil.sendPlayerActionBar(radar, ChatColor.GRAY + best.getName() + " | " +  ChatColor.RED
		+ ((int) bestDistance) + ((bestDistance > 1) ? " blocks" : " block")))
			return;
		radar.sendMessage(ChatColor.GRAY + "The player closest to you is " + ChatColor.GOLD + best.getName()
						+ ChatColor.GRAY + ", he is " + ChatColor.GOLD + ((int) bestDistance) + " block"
						+ ((bestDistance > 1) ? "s" : "") + ChatColor.GRAY + " away.");
	}
}
