package fr.mrcubee.pluginutil.spigot.annotations.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface Command {
	String command();
	String usage();
	boolean color() default false;
	char colorChar() default '&';
	CommandParameterTypes[] arguments() default {CommandParameterTypes.VOID};
	int min() default 0;
	int max() default -1;
	CommandSenderType[] allowSender() default {CommandSenderType.ALL};
}
