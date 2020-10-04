package fr.mrcubee.survivalgames.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class AsyncPlayerChat implements Listener {
	
	@EventHandler
	public void PlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player target;
		Player player = event.getPlayer();
		String[] args;
		
		if ((!player.isOp()) && (!player.getGameMode().equals(GameMode.SPECTATOR)))
			return;
		args = event.getMessage().split(" ");
		if (args.length < 1 || (!args[0].equalsIgnoreCase("/tp")))
			return;
		event.setCancelled(true);
		if (args.length < 2)
			return;
		if ((target = Bukkit.getPlayer(args[1])) == null) {
			player.sendMessage(ChatColor.RED + "The player is not connected or does not exist.");
			return;
		}
		player.teleport(target);
	}
	
}
