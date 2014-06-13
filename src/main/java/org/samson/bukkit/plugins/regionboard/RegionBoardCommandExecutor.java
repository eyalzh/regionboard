package org.samson.bukkit.plugins.regionboard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

public class RegionBoardCommandExecutor implements CommandExecutor {

    private RegionBoardPlugin plugin;

    public RegionBoardCommandExecutor(RegionBoardPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("regionboard")) {
        	
        	final PluginDescriptionFile pluginDescription = plugin.getDescription();
        	sender.sendMessage(pluginDescription.getName() + " version " + pluginDescription.getVersion());
        	
            return true;
            
        }
        
        return false;
        
    }
}
