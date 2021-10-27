package fr.mrcubee.survivalgames.listeners.player;

import fr.mrcubee.langlib.Lang;
import fr.mrcubee.survivalgames.Game;
import fr.mrcubee.survivalgames.SurvivalGames;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat implements Listener {

	private final SurvivalGames survivalGames;

	public AsyncPlayerChat(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void asyncPlayerChat(AsyncPlayerChatEvent event) {
		Game game;

		game = this.survivalGames.getGame();
		if (!game.isSpectator(event.getPlayer()))
			return;
		event.setFormat(Lang.getMessage(event.getPlayer(), "chat.spectator.prefix", "&7[Spectator]&r", true) + event.getFormat());
		event.getRecipients().removeIf((player) -> !game.isSpectator(player));
	}
	
}
