package fr.mrcubee.survivalgames.scoreboard;

import fr.mrcubee.langlib.Lang;
import fr.mrcubee.survivalgames.Game;
import fr.mrcubee.survivalgames.kit.Kit;
import fr.mrcubee.survivalgames.step.Step;
import fr.mrcubee.survivalgames.step.StepUtil;
import net.arkadgames.survivalgame.sql.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public final class ScoreboardBuilder {

    private static void getGameStatus(Game game, Player player, List<String> result) {
        long seconds = (game.getNextStatTime() - System.currentTimeMillis()) / 1000;
        Step step;String value;

        switch (game.getGameStats()) {
            case WAITING:
                 result.add(Lang.getMessage(player, "scoreboard.gameStatus.waiting",
                         ChatColor.GREEN.toString() + "Waiting...", true));
                break;
            case STARTING:
                result.add(Lang.getMessage(player, "scoreboard.gameStatus.start",
                        ChatColor.GOLD.toString() + "Starts in " + ChatColor.RED.toString() + StepUtil.secondToString(seconds),
                        true, StepUtil.secondToString(seconds)));
                break;
            case DURING:
                step = game.getStepManager().getCurrentStep();
                value = (step != null) ? step.scoreBoardGameStatus(player) : null;
                if (value == null)
                    value = ChatColor.GRAY + StepUtil.secondToString((System.currentTimeMillis() - game.getGameStartTime()) / 1000);
                result.add(value);
                break;
            case STOPPING:
                result.add(Lang.getMessage(player, "scoreboard.gameStatus.restart",
                        ChatColor.GOLD.toString() + "Restart in " + ChatColor.RED.toString() + StepUtil.secondToString(seconds),
                        true, StepUtil.secondToString(seconds)));
                break;
        }
    }

    private static void getPlayers(Game game, Player player, List<String> result) {
        String line = ChatColor.WHITE.toString() + "Players: ";

        if (game.getGameStats().ordinal() < 3) {
            if (game.getNumberPlayer() < game.getGameSetting().getMinPlayer()) {
                result.add(Lang.getMessage(player, "scoreboard.players.waiting.notEnough", "&fPlayers: &c%d&7/&a%d", true,
                        game.getNumberPlayer(), game.getGameSetting().getMinPlayer()));
                return;
            }
            result.add(Lang.getMessage(player, "scoreboard.players.waiting.enough", "&fPlayers: &a%d&7/&a%d", true,
                    game.getNumberPlayer(), game.getGameSetting().getMinPlayer()));
            return;
        }
        result.add(Lang.getMessage(player, "scoreboard.players.during", "&fPlayers: &a%d", true, game.getNumberPlayer()));
    }

    private static void getPlayerKit(Game game, Player player, List<String> result) {
        Kit[] kit;
        String kitName;

        if (game.isSpectator(player)) {
            kitName = Lang.getMessage(player, "kit.spectator.name","Spectator", true);
        } else if ((kit = game.getKitManager().getKitByPlayer(player)) == null || kit.length == 0)
            kitName = Lang.getMessage(player, "kit.noKit.name", "No kit", true);
        else
            kitName = kit[0].getDisplayName(player);
        result.add(Lang.getMessage(player, "scoreboard.kit", "&fKit: &7%s", true, kitName));
    }

    private static void getWorldBorder(Game game, Player player, List<String> result) {
        long border = (long) (game.getGameWorld().getWorldBorder().getSize() / 2);

        result.add(Lang.getMessage(player, "scoreboard.worldBorder", "&fBorder: &9&o+%d -%d", true, border, border));
    }

    private static void getServerIP(Player player, List<String> result) {
        result.add(Lang.getMessage(player, "scoreboard.serverIp", "&e%s", true, "mc.arkadgames.net"));
    }

    protected static List<String> build(Game game, Player player, PlayerData playerData) {
        List<String> result;

        if (game == null || player == null)
            return null;
        result = new LinkedList<String>();
        result.add(Lang.getMessage(player, "scoreboard.gameStatus.title" , "&fGame Status:", true));
        getGameStatus(game, player, result);
        result.add(ChatColor.BLACK.toString());
        result.add(Lang.getMessage(player, "scoreboard.rank", "&fRank: &7%d#", true, playerData.getRank()));
        getPlayers(game, player, result);
        getPlayerKit(game, player, result);
        result.add(ChatColor.DARK_BLUE.toString());
        getWorldBorder(game, player, result);
        result.add(ChatColor.DARK_GREEN.toString());
        getServerIP(player, result);
        return result;
    }

}
