package org.samson.bukkit.plugins.regionboard.region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.samson.bukkit.plugins.regionboard.db.DBService;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WorldGuardRegionMap implements RegionMap {

	private DBService dbService;

	public WorldGuardRegionMap(DBService dbService) {
		this.dbService = dbService;
	}

	@Override
	public List<Region> getRegionsByLocation(Location playerLocation) throws MissingDBService {
		
		if (dbService == null) {
			throw new MissingDBService();
		}
		
		List<Region> regions = new ArrayList<Region>();
		
		List<String> wgRegions = getWGRegionsIdsByLocation(playerLocation);
		
		for (String wgRegionId : wgRegions) {
			String statType = dbService.get(wgRegionId);
			
			if (statType != null) {
				
				Statistic stat = Statistic.valueOf(statType);
				if (stat != null) {
					regions.add(new WorldGuardRegion(wgRegionId, stat, "TBD"));
				}
			}
		}
		
		return regions;
	}

	private List<String> getWGRegionsIdsByLocation(Location playerLocation) {
		WorldGuardPlugin worldGuardPlugin = WGBukkit.getPlugin();
		
		Vector locVector = BukkitUtil.toVector(playerLocation.toVector());
		
		List<String> wgRegions = worldGuardPlugin.getRegionManager(playerLocation.getWorld()).getApplicableRegionsIDs(locVector);
				
		return wgRegions;
	}
	
	@Override
	public void addRegion(WorldGuardRegion region) {
		dbService.set(region.getRegionId(), region.getStatistic().name());
	}

	@Override
	public boolean isPlayerInRegion(Player player, Region lastKnownRegion) {
		Location playerLocation = player.getLocation();
		List<String> wgRegions = getWGRegionsIdsByLocation(playerLocation);
		return wgRegions.contains(lastKnownRegion.getRegionId());
	}

}
