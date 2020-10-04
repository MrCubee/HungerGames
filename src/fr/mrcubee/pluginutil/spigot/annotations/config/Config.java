package fr.mrcubee.pluginutil.spigot.annotations.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface Config {
	String path();
	boolean color() default false;
	char colorChar() default '&';
}
