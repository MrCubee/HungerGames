package fr.mrcubee.hungergames.command;

import fr.mrcubee.hungergames.Game;
import fr.mrcubee.hungergames.GameStats;
import fr.mrcubee.hungergames.HungerGames;
import fr.mrcubee.hungergames.HungerGamesAPI;
import fr.mrcubee.hungergames.kit.Kit;
import fr.mrcubee.hungergames.kit.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitCommand implements CommandExecutor, TabCompleter {

    private final Game game;
    private final KitManager kitManager;

    public KitCommand(HungerGames hungerGames) {
        this.game = hungerGames.getGame();
        this.kitManager = this.game.getKitManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean inGame = this.game.getGameStats() == GameStats.DURING;
        Player player;
        Kit temp;
        List<Kit> targetedKit;
        Kit[] kits;

        if (args == null || args.length < 1)
            return false;
        player = Bukkit.getPlayerExact(args[0]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + args[0] + " player does not exist or is not connected.");
            return true;
        }
        targetedKit = new ArrayList<Kit>();
        for (int i = 1; i < args.length; i++) {
            temp = this.kitManager.getKitByName(args[i]);
            if (temp == null) {
                sender.sendMessage(ChatColor.RED + "The " + args[i] + " Kit does not exist.");
                return true;
            }
            targetedKit.add(temp);
        }
        kits = this.kitManager.getKitByPlayer(player);
        if (kits != null) {
            for (Kit kit : kits) {
                if (inGame)
                    kit.removePlayerKit(player);
                kit.removePlayer(player);
            }
        }
        for (Kit kit : targetedKit) {
            kit.addPlayer(player);
            if (inGame)
                kit.givePlayerKit(player);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result;

        if (args.length > 1) {
            result = new ArrayList<String>(Arrays.asList(this.kitManager.getKitNames()));
            result.removeIf(str -> !str.startsWith(args[args.length - 1]));
            return result;
        }
        return null;
    }
}
