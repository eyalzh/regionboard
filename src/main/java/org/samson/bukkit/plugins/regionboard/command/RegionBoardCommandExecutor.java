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
    	sender.sendMessage(ChatColor.DARK_GREEN +
    			pluginDescription.getName() + " version " + pluginDescription.getVersion());
    	sender.sendMessage(ChatColor.GREEN +
    			"For help, please see RegionBoard's Bukkit page.");    	
    	
    }

    @SubCommand(subCommand="help")
    public void helpSubCommand(CommandSender sender, String[] args) {
    	versionSubCommand(sender, args);
    }    
    
    @SubCommand(
    		subCommand="add", 
    		numberOfArgs = 3, 
    		usageMessage = "Usage: add <region-id> <stat> <display-name>"
    )
    public void addSubCommand(CommandSender sender, String[] args) {

    	final String regionId = args[0];
    	
    	Region region = plugin.getRegionMap().getRegionById(regionId);
    	
    	if (region != null) {
    		sender.sendMessage(ChatColor.RED +
    				"Region " + args[0] + " already exists! Remove it first.");
    		return;
    	}
    	
    	String[] statParts = args[1].split(":");
    	WorldGuardRegion newRegion = new WorldGuardRegion(
    			args[0], 
    			statParts[0], 
    			(statParts.length > 1)?statParts[1]:null, 
    			args[2]);
    	
    	try {
    		plugin.addRegionBoard(newRegion);
    		sender.sendMessage(ChatColor.DARK_GREEN + "Region " + regionId + " added succesfully");
    	} catch (IllegalArgumentException e) {
    		// TODO refactor (more intelligible exceptions)
    		sender.sendMessage(ChatColor.RED + "Illegal region data");
    	}
    	
    }
    
    @SubCommand(subCommand="list")
    public void listSubCommand(CommandSender sender, String[] args) {

    	Set<String> regions = plugin.getRegionMap().getAllRegions();
    	
    	sender.sendMessage(ChatColor.DARK_GREEN + "Region list:");
    	sender.sendMessage(StringUtils.join(regions, ", "));
    	
    }
    
    @SubCommand(
    		subCommand="info", 
    		numberOfArgs = 1, 
    		usageMessage = "Usage: info <region-id>"
    )
    public void infoSubCommand(CommandSender sender, String[] args) {

    	Region region = plugin.getRegionMap().getRegionById(args[0]);
    	
    	if (region != null) {
    		sender.sendMessage(ChatColor.DARK_GREEN + region.toString());
    	} else {
    		sender.sendMessage(ChatColor.RED + "No such region: " + args[0]);
    	}
    	
    }    

    @SubCommand(
    		subCommand="reset", 
    		numberOfArgs = 1, 
    		usageMessage = "Usage: reset <region-id>"
    )
    public void resetSubCommand(CommandSender sender, String[] args) {
    	
    	// TODO add validation
    	plugin.getScoreboardController().resetScoreboard(args[0]);
    	
    	sender.sendMessage(ChatColor.DARK_GREEN + "Region board was reset.");
    	
    }   

    @SubCommand(
    		subCommand="autoreset", 
    		numberOfArgs = 2, 
    		usageMessage = "Usage: autoreset <region-id> <time-secs> [xp-points]"
    )
    public void autoresetSubCommand(CommandSender sender, String[] args) {

    	final String regionId = args[0];
    	
    	Region region = plugin.getRegionMap().getRegionById(args[0]);
    	
    	if (region != null) {
    	
    		int timeMins = Integer.parseInt(args[1]);
    		region.setAutoResetTime(timeMins);
    		
    		if (args.length > 2) {
    			int xpPoints = Integer.parseInt(args[2]);
    			region.setAutoResetXpReward(xpPoints);
    		}

    		plugin.getRegionMap().updateRegion(region);
    		
    		plugin.startAutoResetJob(region);
    		
    		sender.sendMessage(ChatColor.DARK_GREEN + "Auto reset started for " + regionId);
    		
    	} else {
    		
    		sender.sendMessage(ChatColor.RED + "Cannot find the region " + regionId);
    		
    	}
    	
    }      
    
    @SubCommand(
    		subCommand="remove", 
    		numberOfArgs = 1, 
    		usageMessage = "Usage: remove <region-id>"
    )
    public void removeSubCommand(CommandSender sender, String[] args) {
    	
    	final String regionId = args[0];
    	
    	if (plugin.getRegionMap().getRegionById(regionId) != null) {
    		
	    	plugin.getRegionMap().removeRegionById(regionId);
	    	plugin.getScoreboardController().removeScoreboardForRegion(regionId);
	    	plugin.getPlayerPositionMonitor().revokeCache();
	    	
	    	sender.sendMessage(ChatColor.DARK_GREEN + "region " + regionId + " removed!");
	    	
    	} else {
    		
    		sender.sendMessage(ChatColor.RED + "Cannot find the region " + regionId);
    		
    	}
    	
    }
    
}
