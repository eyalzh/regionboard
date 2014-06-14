package org.samson.bukkit.plugins.regionboard.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandExecutorSpec {

	String command();

}
