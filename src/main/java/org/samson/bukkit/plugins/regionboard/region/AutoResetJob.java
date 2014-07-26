package org.samson.bukkit.plugins.regionboard.region;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.samson.bukkit.plugins.regionboard.RegionBoardPlugin;
import org.samson.bukkit.plugins.regionboard.scoreboard.ScoreboardController;

public class AutoResetJob extends BukkitRunnable {

	final private Region region;
	final private RegionBoardPlugin plugin;

	public AutoResetJob(RegionBoardPlugin plugin, Region region) {
		
		this.plugin = plugin;
		this.region = region;
	}
	
	@Override
	public void run() {
		
		// Get the winner(s):
		ScoreboardController scoreboardController = plugin.getScoreboardController();
		
		int xpPoints = region.getXpPoints();
		List<String> topEntries = scoreboardController.getTopEntries(region.getRegionId());
		
		for (String entry : topEntries) {
			
			@SuppressWarnings("deprecation")
			Player player = plugin.getServer().getPlayer(entry);
			
			if (player != null && player.isOnline()) {
				
				plugin.getServer().broadcastMessage(ChatColor.DARK_BLUE + 
						player.getDisplayName() + " wins " + xpPoints + " XP points (" + region.getRegionId() + ")");
				
				scoreboardController.resetScoreboard(region.getRegionId());
				
				if (xpPoints > 0) {
					player.giveExp(xpPoints);
				}
				
			}
			
		}
		
	}

}
