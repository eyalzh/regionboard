package org.samson.bukkit.plugins.regionboard.command;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.samson.bukkit.plugins.regionboard.RegionBoardPlugin;
import org.samson.bukkit.plugins.regionboard.region.Region;
import org.samson.bukkit.plugins.regionboard.region.WorldGuardRegion;
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
    	sender.sendMessage(ChatColor.DARK_GREEN + pluginDescription.getName() + " version " + pluginDescription.getVersion());
    	
    }

    @SubCommand(subCommand="add")
    public void addSubCommand(CommandSender sender, String[] args) {

    	if (args.length != 3) {
    		sender.sendMessage(ChatColor.RED + "Missing arguments");
    		return;
    	} 

    	WorldGuardRegion newRegion = WorldGuardRegion.fromStringValues(args);
    	
    	plugin.addRegionBoard(newRegion);
    	
    	sender.sendMessage(ChatColor.DARK_GREEN + "Region added succesfully");
    	
    }
    
    @SubCommand(subCommand="list")
    public void listSubCommand(CommandSender sender, String[] args) {

    	Set<String> regions = plugin.getRegionMap().getAllRegions();
    	
    	sender.sendMessage(ChatColor.DARK_GREEN + "Region list:");
    	sender.sendMessage(StringUtils.join(regions, ", "));
    	
    }
    
    @SubCommand(subCommand="info")
    public void infoSubCommand(CommandSender sender, String[] args) {

    	if (args.length != 1) {
    		sender.sendMessage(ChatColor.RED + "Expected single argument");
    		return;
    	} 
    	
    	Region region = plugin.getRegionMap().getRegionByName(args[0]);
    	
    	if (region != null) {
    		sender.sendMessage(ChatColor.DARK_GREEN + region.toString());
    	} else {
    		sender.sendMessage(ChatColor.RED + "No such region " + args[0]);
    	}
    	
    }    
    
    @SubCommand(subCommand="removeall")
    public void removeAllSubCommand(CommandSender sender, String[] args) {

    	plugin.removeAllRegions();
    	
    	sender.sendMessage(ChatColor.DARK_GREEN + "All regions removed!");
    	
    }    
    
    
}
