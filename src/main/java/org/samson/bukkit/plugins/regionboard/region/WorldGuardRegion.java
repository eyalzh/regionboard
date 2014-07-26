package org.samson.bukkit.plugins.regionboard.region;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

public class WorldGuardRegion implements Region {

	private String regionId;
	private String mainStat;
	private String subStat;
	private String objectiveDisplayName;
	
	private int autoResetSecs;
	private int autoResetXpReward;

	public WorldGuardRegion(String wgRegionId, String mainStat, String subStat, String objectiveDisplayName) {
		this.regionId = wgRegionId;
		this.mainStat = mainStat;
		this.subStat = subStat;
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
		return "WorldGuardRegion [ID = " + regionId
				+ ", main statistic = " + mainStat
				+ ", sub statistic = " + subStat
				+ ", auto reset = " + autoResetSecs
				+ ", xp reward = " + autoResetXpReward
				+ ", display name = " + objectiveDisplayName + "]";
	}

	@Override
	public String getMainStatisticName() {
		return mainStat;
	}

	@Override
	public String getSubStatisticName() {
		return subStat;
	}
	
	@Override
	public boolean matchStatistic(Statistic statistic, Material material, EntityType entityType) {
		return 
				matchMainStat(statistic) && 
				matchSubStat(statistic, material, entityType);
	}	
	
	private boolean matchSubStat(Statistic statistic, Material material, EntityType entityType) {
		
		if (subStat == null) {
			// Always match if the sub-stat is not defined
			return true;
		}
		
		if (statistic.equals(Statistic.MINE_BLOCK) || statistic.equals(Statistic.USE_ITEM)) {
			return (material != null && material.name().equalsIgnoreCase(subStat));
		} 
		
		if (statistic.equals(Statistic.KILL_ENTITY) || statistic.equals(Statistic.ENTITY_KILLED_BY)) {
			return (entityType != null && entityType.name().equalsIgnoreCase(subStat));
		} 
		
		return true;
	}

	private boolean matchMainStat(Statistic statistic) {
		return statistic.name().equalsIgnoreCase(mainStat);
	}

	@Override
	public void setAutoResetTime(int timeSecs) {
		autoResetSecs = timeSecs;
	}

	@Override
	public void setAutoResetXpReward(int xpPoints) {
		autoResetXpReward = xpPoints;
	}

	@Override
	public int getAutoResetTime() {
		return autoResetSecs;
	}

	@Override
	public int getXpPoints() {
		return autoResetXpReward;
	}
	
}
