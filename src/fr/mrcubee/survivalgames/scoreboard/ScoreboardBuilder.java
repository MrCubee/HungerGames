package fr.mrcubee.survivalgames.scoreboard;
import fr.mrcubee.survivalgames.Game;
import fr.mrcubee.survivalgames.kit.Kit;
import fr.mrcubee.survivalgames.listeners.server.PingServer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public final class ScoreboardBuilder {

    private static void getGameStatus(Game game, List<String> result) {
        long seconds = (game.getNextStatTime() - System.currentTimeMillis()) / 1000;

        switch (game.getGameStats()) {
            case WAITING:
                result.add(ChatColor.GREEN + "Waiting for players...");
                break;
            case STARTING:
                result.add(ChatColor.GOLD + "Starts in " + ChatColor.RED + PingServer.getTime(seconds));
                break;
            case DURING:
                if (game.isPvpEnable())
                    result.add(ChatColor.GRAY + "Playing ...");
                else
                    result.add(ChatColor.GRAY + "PvP in " + ChatColor.RED + PingServer.getTime(seconds));
                break;
            case STOPPING:
                result.add(ChatColor.GOLD + "Restart in " + ChatColor.RED + PingServer.getTime(seconds));
                break;
        }
    }

    private static void getPlayers(Game game, List<String> result) {
        if (game.getGameStats().ordinal() < 3)
            result.add(ChatColor.GREEN.toString() + game.getNumberPlayer() + " / " + game.getGameSetting().getMinPlayer());
        else
            result.add(ChatColor.GREEN.toString() + game.getNumberPlayer());
    }

    private static void getPlayerKit(Game game, Player player, List<String> result) {
        Kit kit;

        if (game.isSpectator(player)) {
            result.add(ChatColor.GRAY + "Spectator");
            return;
        }
        kit = game.getKitManager().getKitByPlayer(player);
        if (kit == null)
            result.add(ChatColor.GRAY + "No Kit");
        else
            result.add(ChatColor.GRAY + kit.getName());
    }

    private static void getWorldBorder(Game game, List<String> result) {
        long border = (long) (game.getGameWorld().getWorldBorder().getSize() / 2);

        result.add(ChatColor.BLUE + "+" + border + " -" + border);
    }

    protected static List<String> build(Game game, Player player) {
        List<String> result;

        if (game == null || player == null)
            return null;
        result = new LinkedList<String>();
        result.add(ChatColor.BLACK.toString());
        result.add(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Game Status:    ");
        getGameStatus(game, result);
        result.add(ChatColor.DARK_BLUE.toString());
        result.add(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Players:");
        getPlayers(game, result);
        result.add(ChatColor.DARK_GREEN.toString());
        result.add(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Kit:");
        getPlayerKit(game, player, result);
        result.add(ChatColor.DARK_AQUA.toString());
        result.add(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Border:");
        getWorldBorder(game, result);
        return result;
    }

}
