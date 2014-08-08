package org.samson.bukkit.plugins.regionboard.event;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import org.samson.bukkit.plugins.regionboard.RegionBoardPlugin;
import org.samson.bukkit.plugins.regionboard.region.MissingDBService;
import org.samson.bukkit.plugins.regionboard.region.Region;
import org.samson.bukkit.plugins.regionboard.scoreboard.ScoreboardController;

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
		
		Region region = event.getRegion();
		Player player = event.getPlayer();
		
		ScoreboardController scoreboardController = plugin.getScoreboardController();
		scoreboardController.displayObjectiveForPlayer(player, region.getRegionId(), region.getScoreboardDisplayName());
		
	}
	
	@EventHandler
	public void onPlayerJoinsIntoRegion(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		
		Location location = player.getLocation();
		
		List<Region> regions;
		try {
			
			regions = plugin.getRegionMap().getRegionsByLocation(location);
			
			if (regions.size() > 0) {
				Region firstRegion = regions.get(0);
				
				ScoreboardController scoreboardController = plugin.getScoreboardController();
				scoreboardController.displayObjectiveForPlayer(player, firstRegion.getRegionId(), firstRegion.getScoreboardDisplayName());
			}			
			
		} catch (MissingDBService e) {
			e.printStackTrace();
		}
		
	}	
	
	@EventHandler
	public void onPlayerLeavesRegion(PlayerLeavesRegion event) {

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
		
		if (! plugin.getPlayerPositionMonitor().isPlayerInAnyRegion(player)) {
			return;
		}
		
		List<Region> regions;
		try {
			regions = plugin.getRegionMap().getRegionsByLocation(player.getLocation());
			
			if (! regions.isEmpty()) {
			
				Region firstRegion = regions.get(0);
				
				boolean isMatched = firstRegion.matchStatistic(stat, event.getMaterial(), event.getEntityType()); 
				
				if (isMatched) {
					
					ScoreboardController scoreboardController = plugin.getScoreboardController();
					scoreboardController.updateScoreForPlayer(player, firstRegion, event.getNewValue() - event.getPreviousValue());					
					
				}
				
			}
			
		} catch (MissingDBService e) {
			e.printStackTrace();
		}
		
	}
	
}
