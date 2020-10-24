package fr.mrcubee.survivalgames.scoreboard;

import fr.mrcubee.survivalgames.Game;
import fr.mrcubee.survivalgames.kit.Kit;
import fr.mrcubee.survivalgames.step.Step;
import fr.mrcubee.survivalgames.step.StepUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public final class ScoreboardBuilder {

    private static void getGameStatus(Game game, Player player, List<String> result) {
        long seconds = (game.getNextStatTime() - System.currentTimeMillis()) / 1000;
        Step step;
        String value;

        switch (game.getGameStats()) {
            case WAITING:
                result.add(ChatColor.GREEN.toString() + "Waiting for players...");
                break;
            case STARTING:
                result.add(ChatColor.GOLD.toString() + "Starts in " + ChatColor.RED.toString() + StepUtil.secondToString(seconds));
                break;
            case DURING:
                step = game.getStepManager().getCurrentStep();
                value = (step != null) ? step.scoreBoardGameStatus(player) : null;
                if (value == null)
                    value = ChatColor.GRAY + "Playing...";
                result.add(value);
                break;
            case STOPPING:
                result.add(ChatColor.GOLD.toString() + "Restart in " + ChatColor.RED.toString() + StepUtil.secondToString(seconds));
                break;
        }
    }

    private static void getPlayers(Game game, List<String> result) {
        String line;

        if (game.getGameStats().ordinal() < 3) {
            if (game.getNumberPlayer() < game.getGameSetting().getMinPlayer())
                line = ChatColor.RED.toString() + game.getNumberPlayer();
            else
                line = ChatColor.GREEN.toString() + game.getNumberPlayer();
            line = line + ChatColor.GRAY.toString() + " / " + ChatColor.GREEN.toString() + game.getGameSetting().getMinPlayer();
        } else
            line = ChatColor.GREEN.toString() + game.getNumberPlayer();
        result.add(line);
    }

    private static void getPlayerKit(Game game, Player player, List<String> result) {
        Kit kit;

        if (game.isSpectator(player)) {
            result.add(ChatColor.GRAY.toString() + "Spectator");
            return;
        }
        kit = game.getKitManager().getKitByPlayer(player);
        if (kit == null)
            result.add(ChatColor.GRAY.toString() + "No Kit");
        else
            result.add(ChatColor.GRAY.toString() + kit.getName());
    }

    private static void getWorldBorder(Game game, List<String> result) {
        long border = (long) (game.getGameWorld().getWorldBorder().getSize() / 2);

        result.add(ChatColor.BLUE.toString() + "+" + border + " -" + border);
    }

    private static void getServerIP(Player player, List<String> result) {
        result.add(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "mc.arkadgames.net");
    }

    protected static List<String> build(Game game, Player player) {
        List<String> result;

        if (game == null || player == null)
            return null;
        result = new LinkedList<String>();
        result.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "                           ");
        result.add(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Game Status:");
        getGameStatus(game, player, result);
        result.add(ChatColor.DARK_BLUE.toString());
        result.add(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Players:");
        getPlayers(game, result);
        result.add(ChatColor.DARK_GREEN.toString());
        result.add(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Kit:");
        getPlayerKit(game, player, result);
        result.add(ChatColor.DARK_AQUA.toString());
        result.add(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Border:");
        getWorldBorder(game, result);
        result.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "                           "
                + ChatColor.WHITE.toString());
        getServerIP(player, result);
        return result;
    }

}
