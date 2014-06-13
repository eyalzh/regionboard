package org.samson.bukkit.plugins.regionboard;

import java.util.List;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import org.samson.bukkit.plugins.regionboard.event.PlayerEntersRegion;
import org.samson.bukkit.plugins.regionboard.event.PlayerLeavesRegion;
import org.samson.bukkit.plugins.regionboard.region.MissingDBService;
import org.samson.bukkit.plugins.regionboard.region.Region;

/**
 * The plugin's main event handler/controller
 */
public class RegionBoardEventListener implements Listener {

	private final RegionBoardPlugin plugin;


	
	public RegionBoardEventListener(RegionBoardPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerEntersRegion(PlayerEntersRegion event) {
	
		plugin.getLogger().info("Debug: called onPlayerEntersRegion with " + event.getRegion());
		
		Region region = event.getRegion();
		Player player = event.getPlayer();
		
		ScoreboardController scoreboardController = plugin.getScoreboardController();
		scoreboardController.displayObjectiveForPlayer(player, region.getRegionId(), region.getScoreboardDisplayName());
		
	}
	
	@EventHandler
	public void onPlayerLeavesRegion(PlayerLeavesRegion event) {
	
		plugin.getLogger().info("Debug: called onPlayerLeavesRegion with " + event.getRegion());

		Player player = event.getPlayer();
		
		ScoreboardController scoreboardController = plugin.getScoreboardController();
		scoreboardController.displayMainScoreboard(player);

		// TODO - display previous, cached board instead of main scoreboard
		
	}	
	
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerStatisticIncrementEvent(PlayerStatisticIncrementEvent event) {

		Statistic stat = event.getStatistic();
		
		if (! RegionBoardPlugin.isStatTracked(stat)) {
			return;
		}
		
		Player player = event.getPlayer();
		
		List<Region> regions;
		try {
			regions = plugin.getRegionMap().getRegionsByLocation(player.getLocation());
			
			if (! regions.isEmpty()) {
			
				Region firstRegion = regions.get(0);
				
				Statistic regionStat = firstRegion.getStatistic();
				
				if (regionStat.equals(stat)) {
					
					ScoreboardController scoreboardController = plugin.getScoreboardController();
					scoreboardController.updateScoreForPlayer(player, firstRegion, event.getNewValue() - event.getPreviousValue());					
					
				}
				
			}
			
		} catch (MissingDBService e) {
			e.printStackTrace();
		}
		
	}
	
	
}
