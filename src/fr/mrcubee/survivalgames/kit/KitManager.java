package fr.mrcubee.survivalgames.kit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;

public class KitManager implements CommandExecutor {
	
	private SurvivalGames survivalGames;
	private List<Kit>     kits;
	
	public KitManager(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
		this.kits = new ArrayList<Kit>();
	}
	
	public void registerKit(Kit kit) {
		if ((kit == null) || (kit.getName() == null) || (kit.getName().length() < 1) || (getKitByName(kit.getName()) != null) || (kit.getItemStack() == null) || kits.contains(kit))
			return;
		kits.add(kit);
		survivalGames.getServer().getPluginManager().registerEvents(kit, survivalGames);
	}
	
	public boolean hasKit(Player player) {
		if (player == null)
			return false;
		for (Kit kit : kits)
			if (kit.containsPlayer(player))
				return true;
		return false;
	}
	
	public void removeKit(Player player) {
		if (player == null)
			return;
		for (Kit kit : kits)
			kit.removePlayer(player);
	}
	
	public String[] getKitsNames() {
		String[] names = new String[kits.size()];
		for (int i = 0; i < kits.size(); i++)
			names[i] = kits.get(i).getName();
		return names;
	}
	
	public Kit getKitByName(String name) {
		if ((name == null) || (name.length() < 1))
			return null;
		for (Kit kit : kits)
			if (kit.getName().equals(name))
				return kit;
		return null;
	}
	
	public Kit getKitByPlayer(Player player) {
		if (player == null)
			return null;
		for (Kit kit : kits)
			if (kit.containsPlayer(player))
				return kit;
		return null;
	}
	
	public void giveKitToPlayer() {
		for (Kit kit : kits)
			kit.givePlayersKit();
	}
	
	public void removeKitToPlayer() {
		for (Kit kit : kits)
			kit.removePlayersKit();
	}
	
	public Kit randomKit(Player player) {
		Kit result = null;
		
		if (kits.size() < 0)
			return null;
		while (result == null || (!result.canPlayerTakeKit(player)))
			result = kits.get(new Random().nextInt(kits.size()));
		return result;
	}
	
	public void randomKitPlayerWithNotKit() {
		Kit kit;

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (getKitByPlayer(player) == null) {
				if ((kit = randomKit(player)) != null) {
					kit.addPlayer(player);
					player.sendMessage(ChatColor.GOLD + "You force took the "+ ChatColor.RED + kit.getName() + ChatColor.GOLD + " kit.");
				}
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Kit kit;
		Player target;
		
		if (sender.isOp() && args != null && args.length > 1 &&  args[0].equalsIgnoreCase("view")) {
			target = Bukkit.getPlayer(args[1]);
			if (target == null)
				sender.sendMessage(ChatColor.RED + "Player do not exist !");
			else
				sender.sendMessage(ChatColor.GOLD + "Kit: " + ChatColor.RESET + (((kit = getKitByPlayer(target)) == null) ? "No Kit" : kit.getName()));
		}
		return true;
	}

}
