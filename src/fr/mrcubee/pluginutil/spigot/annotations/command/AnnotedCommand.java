package fr.mrcubee.pluginutil.spigot.annotations.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AnnotedCommand implements CommandExecutor, TabCompleter {
	
	private Object commandObject;
	private Command command;
	private Permission permission;
	private Method commandMethod;
	private Method completionMethod;
	
	public AnnotedCommand(Object commandObject, Command command, Permission permission, Method commandMethod, Method completionMethod) {
		this.commandObject = commandObject;
		this.command = command;
		this.permission = permission;
		this.commandMethod = commandMethod;
		this.completionMethod = completionMethod;
	}
	
	private boolean isAllowSender(CommandSender sender) {
		for (CommandSenderType commandSenderType : CommandSenderType.values()) {
			switch (commandSenderType) {
			case ALL :
				return true;
			case CONSOL :
				
			case PLAYER :
				if (sender instanceof Player)
					return true;
				break;
			case COMMAND_BLOCK :
				if (sender instanceof BlockCommandSender)
					return true;
				break;
			case ENTITY :
				if ((sender instanceof Entity) && !(sender instanceof Player))
					return true;
			default:
				break;
			}
		}
		return false;
	}
	
	private String getArguments(String[] args, int index) {
		String result = null;
		
		for (int i = index; i < args.length; i++) {
			if (result == null)
				result = args[i];
			else
				result += " " + args[i];
		}
		if (result == null)
			return "";
		return result;
	}
	
	private Object[] getArguments(Object[] startArguements, String[] args, int min, int max) {
		List<Object> arguments = new ArrayList<Object>();
		Object value;
		CommandParameterTypes commandParameterType;
		
		arguments.addAll(Arrays.asList(startArguements));
		for (int i = 0; i < args.length; i++) {
			commandParameterType = (command.arguments() == null || i >= command.arguments().length) ? CommandParameterTypes.VOID : command.arguments()[i];
			if (commandParameterType == CommandParameterTypes.VOID) {
				if (i > 0 && command.arguments()[i - 1] == CommandParameterTypes.STRING)
					arguments.set(arguments.size() - 1, arguments.get(arguments.size() - 1) + getArguments(args, i));
				break;
			}
			arguments.add(commandParameterType.getValue(args[i]));
		}
		return arguments.toArray();
	}
	
	private void sendUsage(CommandSender sender, String usage) {
		if (usage == null || usage.isEmpty())
			return;
		if (command.color())
			usage = ChatColor.translateAlternateColorCodes(command.colorChar(), usage);
		sender.sendMessage(usage);
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command bukkitCommand, String label, String[] args) {
		int min = (command.min() < command.max() || command.max() < 0) ? command.min() : command.max();
		int max = (command.min() > command.max() && command.max() < 0) ? command.min() : command.max();
		Object[] arguments;
		String usage = command.usage();
		String errorMessage;
		
		if (!isAllowSender(sender))
			return false;
		else if (permission != null && permission.value() != null && !permission.value().isEmpty() && !sender.hasPermission(permission.value())) {
			errorMessage = permission.errorMessage();
			if (errorMessage != null && !errorMessage.isEmpty()) {
				if (permission.color())
					errorMessage = ChatColor.translateAlternateColorCodes(permission.colorChar(), errorMessage);
				sender.sendMessage(errorMessage);
			}
			return false;
		}
		if (min < 0)
			return false;
		if (args.length < min || (args.length > max && max > -1)) {
			sendUsage(sender, usage);
			return false;
		}
		try {
			commandMethod.setAccessible(true);
			commandMethod.invoke(commandObject, getArguments(new Object[] {sender}, args, min, max));
			return true;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			if (e instanceof IllegalAccessException)
				e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command bukkitCommand, String label, String[] args) {
		List<String> completTab = new ArrayList<String>();
		
		if (completionMethod != null) {
			try {
				completionMethod.setAccessible(true);
				completionMethod.invoke(commandObject, getArguments(new Object[] {completTab, sender}, args, 0, -1));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e instanceof IllegalAccessException)
					e.printStackTrace();
			}
		}
		return (completTab);
	}

}
