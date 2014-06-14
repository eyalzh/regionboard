package org.samson.bukkit.plugins.regionboard.command;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.samson.bukkit.plugins.regionboard.RegionBoardPlugin;
import org.samson.bukkit.plugins.regionboard.util.CommandExecutorBase;
import org.samson.bukkit.plugins.regionboard.util.CommandExecutorSpec;
import org.samson.bukkit.plugins.regionboard.util.SubCommand;

@CommandExecutorSpec(command="regionboard")
public class RegionBoardCommandExecutor extends CommandExecutorBase {

    public RegionBoardCommandExecutor(RegionBoardPlugin plugin) {
        super(plugin);
    }
    
    @SubCommand(subCommand="version")
    public void versionSubCommand(CommandSender sender, String[] args) {
    	
    	final PluginDescriptionFile pluginDescription = plugin.getDescription();
    	sender.sendMessage(pluginDescription.getName() + " version " + pluginDescription.getVersion());
    	
    }

    @SubCommand(subCommand="add")
    public void addSubCommand(CommandSender sender, String[] args) {

    	sender.sendMessage("TBD - add");
    	
    }
    
    @SubCommand(subCommand="list")
    public void listSubCommand(CommandSender sender, String[] args) {

    	sender.sendMessage("TBD - list");
    	
    }
    
}
