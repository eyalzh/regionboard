package org.samson.bukkit.plugins.regionboard;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Statistic;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.samson.bukkit.plugins.regionboard.command.RegionBoardCommandExecutor;
import org.samson.bukkit.plugins.regionboard.db.DBService;
import org.samson.bukkit.plugins.regionboard.db.MapDBService;
import org.samson.bukkit.plugins.regionboard.event.RegionBoardEventListener;
import org.samson.bukkit.plugins.regionboard.monitor.PlayerPositionMonitor;
import org.samson.bukkit.plugins.regionboard.region.AutoResetJob;
import org.samson.bukkit.plugins.regionboard.region.Region;
import org.samson.bukkit.plugins.regionboard.region.RegionMap;
import org.samson.bukkit.plugins.regionboard.region.WorldGuardRegionMap;
import org.samson.bukkit.plugins.regionboard.scoreboard.ScoreboardController;
import org.samson.bukkit.plugins.regionboard.util.BukkitCommandLoader;

public class RegionBoardPlugin extends JavaPlugin {
	
	private RegionMap regionMap;
	private DBService regionsDB;
	
	private static final Set<Statistic> STATS_TRACKED;
	
	static {
		Set<Statistic> statsTrackedInit = new HashSet<Statistic>();
		
		statsTrackedInit.add(Statistic.DEATHS);
		statsTrackedInit.add(Statistic.MOB_KILLS);
		statsTrackedInit.add(Statistic.PLAYER_KILLS);
		statsTrackedInit.add(Statistic.FISH_CAUGHT);
		statsTrackedInit.add(Statistic.ANIMALS_BRED);
		statsTrackedInit.add(Statistic.TREASURE_FISHED);
		statsTrackedInit.add(Statistic.JUNK_FISHED);
		statsTrackedInit.add(Statistic.MINE_BLOCK);
		statsTrackedInit.add(Statistic.USE_ITEM);
		statsTrackedInit.add(Statistic.KILL_ENTITY);
		statsTrackedInit.add(Statistic.ENTITY_KILLED_BY);
		
		STATS_TRACKED = Collections.unmodifiableSet(statsTrackedInit);
	}	
	
	private final RegionBoardEventListener eventListener = new RegionBoardEventListener(this);
	private final ScoreboardController scoreboardController = new ScoreboardController();
	private final PlayerPositionMonitor playerPositionMonitor = new PlayerPositionMonitor(this);
	
	private Map<String, AutoResetJob> regionJobs = new HashMap<String, AutoResetJob>();
	
	public void onDisable() {
		
		playerPositionMonitor.stop();
		
		stopRegionJobs();
		
		if (regionsDB != null) {
			regionsDB.close();
		}
		
	}

	public void onEnable() { 
		
		getDataFolder().mkdirs();
		regionsDB = new MapDBService(new File(getDataFolder(), "regions.db"));
		regionMap = new WorldGuardRegionMap(regionsDB);
		
		startRegionJobs();
		
		BukkitCommandLoader.loadCommands(this, RegionBoardCommandExecutor.class);
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(eventListener, this);
		
		playerPositionMonitor.start();
		
	}


	public ScoreboardController getScoreboardController() {
		return scoreboardController;
	}

	public RegionMap getRegionMap() {
		return regionMap;
	}
	
	public static boolean isStatTracked(Statistic stat) {
		return STATS_TRACKED.contains(stat);
	}
	
	public void addRegionBoard(Region newRegion) {
		
		Statistic statistic = Statistic.valueOf(newRegion.getMainStatisticName());
		
		if (isStatTracked(statistic)) {
			
			regionMap.addRegion(newRegion);
			
		} else {
			
			throw new IllegalArgumentException();
			
		}
		
	}
	
	public void removeAllRegions() {
    	
    	regionMap.removeAll();
    	playerPositionMonitor.revokeCache();
    	
	}
	
	public PlayerPositionMonitor getPlayerPositionMonitor() {
		return playerPositionMonitor;
	}

	public void startAutoResetJob(Region region) {
		
		if (region.getAutoResetTime() > 0) {
			
			if (regionJobs.containsKey(region.getRegionId())) {
				AutoResetJob previousJob = regionJobs.get(region.getRegionId());
				previousJob.cancel();
			}
		
			AutoResetJob autoResetJob = new AutoResetJob(this, region);
			final int tickingPeriod = 20; // 20 ticks
			autoResetJob.runTaskTimer(this, 0, tickingPeriod);
			
			regionJobs.put(region.getRegionId(), autoResetJob);
			
		}
		
	}
	
	private void stopRegionJobs() {
		
		Set<Entry<String, AutoResetJob>> entrySet = regionJobs.entrySet();
		
		for (Entry<String, AutoResetJob> entry : entrySet) {
			entry.getValue().cancel();
		}
		
	}
	
	private void startRegionJobs() {
		
		Set<String> allRegions = regionMap.getAllRegions();
		
		for (String regionId : allRegions) {
			Region region = regionMap.getRegionById(regionId);
			startAutoResetJob(region);
		}
		
	}	
	
}
