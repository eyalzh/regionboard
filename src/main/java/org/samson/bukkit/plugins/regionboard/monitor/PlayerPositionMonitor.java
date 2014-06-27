package org.samson.bukkit.plugins.regionboard.monitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.samson.bukkit.plugins.regionboard.RegionBoardPlugin;
import org.samson.bukkit.plugins.regionboard.event.PlayerEntersRegion;
import org.samson.bukkit.plugins.regionboard.event.PlayerLeavesRegion;
import org.samson.bukkit.plugins.regionboard.region.MissingDBService;
import org.samson.bukkit.plugins.regionboard.region.Region;
import org.samson.bukkit.plugins.regionboard.region.RegionMap;

/**
 * Responsible to starting, stopping, changing the pace of the monitor job - 
 * a Bukkit runnable which emits the PlayerEntersRegion and PlayerLeavesRegion events
 */
public class PlayerPositionMonitor {
	
	private static final int JOB_INTERVAL_TICKS = 20;

	private final RegionBoardPlugin plugin;
	
	private MonitorPlayerPositionJob monitorJob;

	private Map<String, Region> playerRegionCache = new HashMap<String, Region>();
	
	public PlayerPositionMonitor(RegionBoardPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void start() {
		
		plugin.getLogger().info("Starting PlayerPositionMonitor...");
		
		monitorJob = new MonitorPlayerPositionJob(plugin.getRegionMap());
		monitorJob.runTaskTimer(plugin, 0, JOB_INTERVAL_TICKS);
	}
	
	public void stop() {
		
		plugin.getLogger().info("Stopping PlayerPositionMonitor...");
		
		if (monitorJob != null) {
			monitorJob.cancel();
		}
	}

	public void revokeCache() {
		playerRegionCache.clear();
	}
	
	public boolean isPlayerInAnyRegion(Player player) {
		return playerRegionCache.containsKey(player.getName());
	}
	
	public class MonitorPlayerPositionJob extends BukkitRunnable {

		private RegionMap regionMap;
		
		public MonitorPlayerPositionJob(RegionMap regionMap) {
			this.regionMap = regionMap;
		}
		
		@Override
		public void run() {
			
			Player[] onlinePlayers = plugin.getServer().getOnlinePlayers();
			
			for (Player player : onlinePlayers) {
				updatePlayerRegions(player);
			}
			
		}
		
		private void updatePlayerRegions(Player player) {

			Region lastKnownRegion = getLastKnownRegion(player);
			
			if (lastKnownRegion != null) {
				
				if (! isPlayerStillInRegion(player, lastKnownRegion)) {
					playerRegionCache.remove(player.getName());
					PlayerLeavesRegion playerLeavesRegionEvent = new PlayerLeavesRegion(lastKnownRegion, player);
					plugin.getServer().getPluginManager().callEvent(playerLeavesRegionEvent);
				}
				
			} else {
				
				findFirstRegion(player);
			
			}
			
		}

		private void findFirstRegion(Player player) {
			try {

				List<Region> regionList;
				Location playerLocation = player.getLocation();
				regionList = regionMap.getRegionsByLocation(playerLocation);
				
				if (regionList.size() > 0) {
					
					Region region = regionList.get(0);

					PlayerEntersRegion playerEntersRegionEvent = new PlayerEntersRegion(region, player);
					plugin.getServer().getPluginManager().callEvent(playerEntersRegionEvent);
					playerRegionCache.put(player.getName(), region);
						
				}					
				
			} catch (MissingDBService e) {
				e.printStackTrace();
			}
		}

		private Region getLastKnownRegion(Player player) {
			if (playerRegionCache.containsKey(player.getName())) {
				return playerRegionCache.get(player.getName());
			}
			
			return null;
		}
		
		private boolean isPlayerStillInRegion(Player player, Region lastKnownRegion) {
			return regionMap.isPlayerInRegion(player, lastKnownRegion);
		}		
		
		
	}
	
}
