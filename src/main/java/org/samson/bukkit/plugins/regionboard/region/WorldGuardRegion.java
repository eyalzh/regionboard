package org.samson.bukkit.plugins.regionboard.region;

import org.bukkit.Statistic;

public class WorldGuardRegion implements Region {
	
	private String regionId;
	private Statistic stat;
	private String objectiveDisplayName;

	public WorldGuardRegion(String wgRegionId, Statistic stat, String objectiveDisplayName) {
		this.regionId = wgRegionId;
		this.stat = stat;
		this.objectiveDisplayName = objectiveDisplayName;
	}

	@Override
	public String getRegionId() {
		return regionId;
	}

	@Override
	public String getScoreboardDisplayName() {
		return objectiveDisplayName;
	}
	
	@Override
	public String toString() {
		return "WorldGuardRegion [regionId=" + regionId
				+ ", Statistic =" + stat.toString()
				+ ", objectiveDisplayName=" + objectiveDisplayName + "]";
	}

	@Override
	public Statistic getStatistic() {
		return stat;
	}
	
}
