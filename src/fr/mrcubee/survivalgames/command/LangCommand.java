package fr.mrcubee.survivalgames.command;

import fr.mrcubee.langlib.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LangCommand implements CommandExecutor, TabCompleter {

    private final List<String> langs;

    public LangCommand() {
        this.langs = Arrays.asList("EN_us", "FR_fr", "ZH_zh");
    }

    private String getLang(String langString) {
        if (langString == null)
            return null;
        for (String lang : this.langs) {
            if (lang.equalsIgnoreCase(langString))
                return lang;
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String lang;
        int value;

        if (!(sender instanceof Player) ||  args.length < 1)
            return false;
        if (args[0].equalsIgnoreCase("clean")) {
            if (args.length < 2)
                return true;
            try {
                value = Integer.parseInt(args[1]);
            } catch (Exception exception) {
                sender.sendMessage("Incorrect number");
                return true;
            }
            Lang.clean(value);
            return true;
        }
        lang = getLang(args[0]);
        if (lang == null) {
            sender.sendMessage(ChatColor.RED + "Lang not found.");
            return true;
        }
        Lang.setPlayerLang((Player) sender, lang);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> result = new ArrayList<String>();

        if (args.length == 1) {
            result.addAll(this.langs);
            result.removeIf(value -> !value.toLowerCase().startsWith(args[0].toLowerCase()));
        }
        return result;
    }
}
