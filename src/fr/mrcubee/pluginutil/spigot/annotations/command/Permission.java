package fr.mrcubee.pluginutil.spigot.annotations.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface Permission {
	
	String value();
	String errorMessage() default "You do not have permission.";
	boolean color() default false;
	char colorChar() default '&';
	
}
