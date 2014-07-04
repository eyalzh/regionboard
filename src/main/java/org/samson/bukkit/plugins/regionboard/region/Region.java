package org.samson.bukkit.plugins.regionboard.region;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

public interface Region {

	public String getScoreboardDisplayName();
	public String getRegionId();
	
	public String getMainStatisticName();
	public String getSubStatisticName();
	
	public boolean matchStatistic(Statistic statistic, Material material, EntityType entityType);

}
