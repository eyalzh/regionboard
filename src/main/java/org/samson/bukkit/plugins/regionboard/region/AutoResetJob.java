package org.samson.bukkit.plugins.regionboard.region;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.samson.bukkit.plugins.regionboard.RegionBoardPlugin;
import org.samson.bukkit.plugins.regionboard.event.RegionAutoResetEvent;
import org.samson.bukkit.plugins.regionboard.scoreboard.ScoreboardController;

public class AutoResetJob extends BukkitRunnable {

	final private Region region;
	final private RegionBoardPlugin plugin;
	
	private int remainingTicks;

	public AutoResetJob(RegionBoardPlugin plugin, Region region) {
		
		this.plugin = plugin;
		this.region = region;
		
		this.remainingTicks = region.getAutoResetTime();
		
	}
	
	@Override
	public void run() {
		
		if (remainingTicks == 0) {
			
			RegionAutoResetEvent resetEvent = new RegionAutoResetEvent(region);
			Bukkit.getServer().getPluginManager().callEvent(resetEvent);
			
			if (! resetEvent.isCancelled()) {
				fireAutoResetEvent();
			}
			
			remainingTicks = region.getAutoResetTime();			
			
		} else {
			
			updateScoreboardTimer();
			remainingTicks--;
			
		}
		
	}

	private void updateScoreboardTimer() {
		ScoreboardController scoreboardController = plugin.getScoreboardController();
		scoreboardController.updateScoreboardTimer(region.getRegionId(), remainingTicks);
		
	}

	@SuppressWarnings("deprecation")
	private void fireAutoResetEvent() {
		// Get the winner(s):
		ScoreboardController scoreboardController = plugin.getScoreboardController();
		
		int xpPoints = region.getXpPoints();
		
		if (xpPoints == 0) {
			return;
		}
		
		List<String> topEntries = scoreboardController.getTopEntries(region.getRegionId());
		
		for (String entry : topEntries) {

			Player player = plugin.getServer().getPlayer(entry);

			scoreboardController.resetScoreboard(region.getRegionId());
			
			if (player != null && player.isOnline()) {
				player.giveExp(xpPoints);					
				announceAutoResetWinner(xpPoints, player);
			}
			
		}		
		
	}

	private void announceAutoResetWinner(int xpPoints, Player player) {
		
		String winMsg = String.format("%s &5wins %d XP points (%s&5)!",
				player.getDisplayName(),
				xpPoints,
				ScoreboardController.getFormattedScoreboardDisplayName(region.getScoreboardDisplayName()));

		winMsg = ChatColor.translateAlternateColorCodes('&', winMsg);
		
		plugin.getServer().broadcast(winMsg, "regionboard.seewinmsg");
		
	}

}
