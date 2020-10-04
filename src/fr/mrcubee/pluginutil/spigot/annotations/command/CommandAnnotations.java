package fr.mrcubee.pluginutil.spigot.annotations.command;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import fr.mrcubee.pluginutil.spigot.annotations.PluginAnnotationsLoader;

public class CommandAnnotations implements PluginAnnotationsLoader {

	@Override
	public void loadClass(Plugin plugin, Object... objects) {
		if (plugin == null || objects == null)
			return;
		for (Object object : objects)
			registerCommand(plugin, object);
	}
	
	private boolean isCommandAnnoted(PluginCommand pluginCommand) {
		if (pluginCommand.getExecutor() == null || !(pluginCommand.getExecutor() instanceof AnnotedCommand))
			return false;
		return true;
	}
	
	private void registerCommand(Plugin plugin, Object object) {
		Set<Method> completionMethodes = new HashSet<Method>();
		Command command;
		CommandCompletion commandCompletion;
		PluginCommand pluginCommand;
		Permission permission = null;
		Method commandCompletionMethod = null;
		AnnotedCommand result;
		
		for (Method method : object.getClass().getDeclaredMethods())
			if (method.getAnnotation(CommandCompletion.class) != null)
				completionMethodes.add(method);
		for (Method method : object.getClass().getDeclaredMethods()) {
			if ((command = method.getAnnotation(Command.class)) != null) {
				pluginCommand = Bukkit.getPluginCommand(command.command());
				if (pluginCommand != null && pluginCommand.getPlugin().equals(plugin) && !isCommandAnnoted(pluginCommand)) {
					permission = method.getAnnotation(Permission.class);
					for (Method completionMethod : completionMethodes)
						if ((commandCompletion = completionMethod.getAnnotation(CommandCompletion.class)) != null && commandCompletion.command().equals(command.command()))
							commandCompletionMethod = completionMethod;
					result = new AnnotedCommand(object, command, permission, method, commandCompletionMethod);
					pluginCommand.setExecutor(result);
					pluginCommand.setTabCompleter(result);
				}
			}
		}
	}
}
