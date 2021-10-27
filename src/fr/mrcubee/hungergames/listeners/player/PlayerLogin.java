package fr.mrcubee.hungergames.listeners.player;

import fr.mrcubee.hungergames.HungerGames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Map;
import java.util.WeakHashMap;

public class PlayerLogin implements Listener {

	public static final Map<Player, String> PLAYER_HOSTNAMES = new WeakHashMap<Player, String>();

	private final HungerGames survivalGames;

	public PlayerLogin(HungerGames survivalGames) {
		this.survivalGames = survivalGames;
	}

	@EventHandler
	public void playerPreLoginEvent(PlayerLoginEvent event) {
		String[] args = event.getHostname().split(":");

		if (event.getHostname() != null)
			PLAYER_HOSTNAMES.put(event.getPlayer(), args[0]);
	}
}
