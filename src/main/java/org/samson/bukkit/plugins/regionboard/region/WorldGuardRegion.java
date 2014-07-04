package org.samson.bukkit.plugins.regionboard.region;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

public class WorldGuardRegion implements Region {

	private String regionId;
	private String mainStat;
	private String subStat;
	private String objectiveDisplayName;

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
		return "WorldGuardRegion [regionId=" + regionId
				+ ", Main statistic =" + mainStat
				+ ", Sub statistic =" + subStat
				+ ", objectiveDisplayName=" + objectiveDisplayName + "]";
	}

	public String[] toStringValues() {

		String values[] = new String[] {
				regionId,
				toSingleStatString(mainStat, subStat),
				objectiveDisplayName 
		};

		return values;
	}

	public static WorldGuardRegion fromStringValues(String[] regionValues) {

		if (regionValues != null && regionValues.length == 3) {
			
			String[] statParts = regionValues[1].split(":");
			
			String subStat = (statParts.length > 1) ? statParts[1] : null;			
			
			return new WorldGuardRegion(regionValues[0], statParts[0], subStat, regionValues[2]);
			
		} else {
			throw new IllegalArgumentException("regionValues should contain 3 strings");
		}
		
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

	private static String toSingleStatString(String mainStat, String subStat) {
		return mainStat + ":" + subStat;
	}


	
}
