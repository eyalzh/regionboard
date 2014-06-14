package org.samson.bukkit.plugins.regionboard.util;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitCommandLoader {

	public static void loadCommands(JavaPlugin plugin, Class<? extends CommandExecutorBase> commandExecutorClass) {

		Class<? extends JavaPlugin> pluginClass = plugin.getClass();
		
		if (! commandExecutorClass.isAnnotationPresent(CommandExecutorSpec.class)) {
			plugin.getLogger().warning("Cannot load command executor!");
			return;
		}
		
		String command = commandExecutorClass.getAnnotation(CommandExecutorSpec.class).command();
				
		try {
			
			CommandExecutor executorInstance = commandExecutorClass.getDeclaredConstructor(pluginClass).newInstance(plugin);
			
			// Register the command executor with annotated command
			plugin.getCommand(command).setExecutor(executorInstance);
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

	}

	
}
