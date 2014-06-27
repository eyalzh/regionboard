package org.samson.bukkit.plugins.regionboard.region;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

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
	public boolean matchPlayerStatisticIncrementEvent(PlayerStatisticIncrementEvent event) {
		
		Statistic statistic = event.getStatistic();
		
		if (! statistic.name().equalsIgnoreCase(mainStat)) {
			return false;
		}

		if (statistic.equals(Statistic.MINE_BLOCK) && subStat != null) {
			Material material = event.getMaterial();
			if (material != null && material.name().equalsIgnoreCase(subStat)) {
				return true;
			} else {
				return false;
			}
		} 
		
		return true;
	}

	@Override
	public String getMainStatisticName() {
		return mainStat;
	}

	@Override
	public String getSubStatisticName() {
		return subStat;
	}
	
	
	private static String toSingleStatString(String mainStat, String subStat) {
		return mainStat + ":" + subStat;
	}
	
}
