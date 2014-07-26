package org.samson.bukkit.plugins.regionboard.region;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.samson.bukkit.plugins.regionboard.db.DBService;

import com.google.gson.Gson;
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
		
		Gson gson = new Gson();		
		
		List<Region> regions = new ArrayList<Region>();
		
		List<String> wgRegions = getWGRegionsIdsByLocation(playerLocation);
		
		for (String wgRegionId : wgRegions) {
			
			Object regionData = dbService.get(wgRegionId);
			
			if (regionData != null) {
				String regionJSON = dbService.get(wgRegionId);
				
				WorldGuardRegion region = gson.fromJson(regionJSON, WorldGuardRegion.class);   
				regions.add(region);
			}

		}
		
		return regions;
	}


	
	@Override
	public void addRegion(Region region) {
		setRegion(region);
	}

	@Override
	public boolean isPlayerInRegion(Player player, Region lastKnownRegion) {
		Location playerLocation = player.getLocation();
		List<String> wgRegions = getWGRegionsIdsByLocation(playerLocation);
		return wgRegions.contains(lastKnownRegion.getRegionId());
	}

	@Override
	public Set<String> getAllRegions() {
		return dbService.getAll();
	}

	@Override
	public void removeAll() {
		dbService.clean();
	}

	@Override
	public Region getRegionById(String id) {
		
		Gson gson = new Gson();	
		
		String regionJSON = dbService.get(id);
		if (regionJSON != null) {
			
			WorldGuardRegion region = gson.fromJson(regionJSON, WorldGuardRegion.class);   

			return region;
			
		} else {
			return null;
		}
	}

	public void removeRegionById(String id) {
		dbService.remove(id);
	}

	@Override
	public void updateRegion(Region region) {
		setRegion(region);
	}
	
	private void setRegion(Region region) {
		Gson gson = new Gson();
		
		dbService.set(region.getRegionId(), gson.toJson((WorldGuardRegion)region));
	}	
	
	private List<String> getWGRegionsIdsByLocation(Location playerLocation) {
		WorldGuardPlugin worldGuardPlugin = WGBukkit.getPlugin();
		
		Vector locVector = BukkitUtil.toVector(playerLocation.toVector());
		
		List<String> wgRegions = worldGuardPlugin.getRegionManager(playerLocation.getWorld()).getApplicableRegionsIDs(locVector);
				
		return wgRegions;
	}	
	
	
}
