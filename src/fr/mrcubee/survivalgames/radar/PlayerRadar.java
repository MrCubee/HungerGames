package fr.mrcubee.survivalgames.radar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
	 * <pre>
	 *     PlayerRadar.radar(survivalgames, radar)
	 * </pre> 
	 * @param game the instance of the game to recover the speculators.
	 * @param radar the player who uses the radar.
	 * @since 1.7
	 */
	public static void radar(Game game, Player radar) {
		Player best = null;
		double bestDistance = 0;
		double distance = 0;
		
		if (game == null || radar == null)
			return;
		for (Player player : Bukkit.getOnlinePlayers()) {
			PlayerRadarEvent playerRadarEvent = new PlayerRadarEvent(radar, player);
			Bukkit.getServer().getPluginManager().callEvent(playerRadarEvent);
			if (player.equals(radar) || game.isSpectator(player) || (!player.isOnline()) || (player.getHealth() < 1) || playerRadarEvent.isCancelled())
				continue;
			distance = player.getLocation().distance(radar.getLocation());
			if ((best == null && distance >= 4) || distance < bestDistance) {
				best = player;
				bestDistance = distance;
			}
		}
		if (best == null)
			return;
		radar.setCompassTarget(best.getLocation());
		radar.sendMessage(ChatColor.GRAY + "The player closest to you is " + ChatColor.GOLD + best.getName()
						+ ChatColor.GRAY + ", he is " + ChatColor.GOLD + ((int) bestDistance) + " block"
						+ ((bestDistance > 1) ? "s" : "") + ChatColor.GRAY + " away.");
	}

}
