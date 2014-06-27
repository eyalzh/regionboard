package org.samson.bukkit.plugins.regionboard.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.samson.bukkit.plugins.regionboard.RegionBoardPlugin;

public abstract class CommandExecutorBase implements CommandExecutor {

    protected RegionBoardPlugin plugin;
	private Map<String, Method> subCommandsMap = new HashMap<String, Method>();
    
    public CommandExecutorBase(RegionBoardPlugin plugin) {
        this.plugin = plugin;
        
        registerSubCommands();
    }
    
    private void registerSubCommands() {

    	Method[] methods = this.getClass().getMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(SubCommand.class)) {
				
				SubCommand annotation = method.getAnnotation(SubCommand.class);
				subCommandsMap.put(annotation.subCommand().toLowerCase(), method);
			}
		}
		
	}

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    	if (args.length > 0) {
    		
    		String subCommand = args[0].toLowerCase();
    		
    		if (subCommandsMap.containsKey(subCommand)) {
    			Method method = subCommandsMap.get(subCommand);
    			try {
    				String[] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);
					method.invoke(this, sender, subCommandArgs);
					return true;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
    		}
    		
    	}
    	
        return false;
        
    }    
    
}
