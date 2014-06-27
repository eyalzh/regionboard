package org.samson.bukkit.plugins.regionboard.region;

import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface RegionMap {

	public List<Region> getRegionsByLocation(Location playerLocation) throws MissingDBService;

	public void addRegion(WorldGuardRegion region);

	public boolean isPlayerInRegion(Player player, Region lastKnownRegion);
	
	public Set<String> getAllRegions();
	public Region getRegionByName(String name);
	
	public void removeAll();
	
}
