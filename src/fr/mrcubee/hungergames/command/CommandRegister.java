package fr.mrcubee.hungergames.command;

import fr.mrcubee.hungergames.HungerGames;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public class CommandRegister {

    private static void registerCommand(HungerGames hungerGames, String commandName, CommandExecutor commandExecutor, TabCompleter tabCompleter) {
        PluginCommand pluginCommand;

        if (hungerGames == null || commandName == null)
            return;
        pluginCommand = hungerGames.getCommand(commandName);
        if (pluginCommand == null)
            return;
        pluginCommand.setExecutor(commandExecutor);
        pluginCommand.setTabCompleter(tabCompleter);
    }

    private static void registerCommand(HungerGames hungerGames, String commandName, Object both) {
        if (!(both instanceof CommandExecutor) || !(both instanceof TabCompleter))
            return;
        registerCommand(hungerGames, commandName, (CommandExecutor) both, (TabCompleter) both);
    }

    public static void registerCommand(HungerGames hungerGames) {
        if (hungerGames == null)
            return;
        registerCommand(hungerGames, "kit", new KitCommand(hungerGames));
        registerCommand(hungerGames, "lang", new LangCommand());
    }

}
