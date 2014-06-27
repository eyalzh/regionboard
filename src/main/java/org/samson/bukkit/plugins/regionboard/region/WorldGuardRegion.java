package org.samson.bukkit.plugins.regionboard.region;

public class WorldGuardRegion implements Region {

	private String regionId;
	private String stat;
	private String objectiveDisplayName;

	public WorldGuardRegion(String wgRegionId, String stat, String objectiveDisplayName) {
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
				+ ", Statistic =" + stat
				+ ", objectiveDisplayName=" + objectiveDisplayName + "]";
	}

	@Override
	public String getStatistic() {
		return stat;
	}

	public String[] toStringValues() {

		String values[] = new String[] {
				regionId,
				stat,
				objectiveDisplayName 
		};
				
		return values;
	}

	public static WorldGuardRegion fromStringValues(String[] regionValues) {

		if (regionValues != null && regionValues.length == 3) {
			
			return new WorldGuardRegion(regionValues[0], regionValues[1], regionValues[2]);
			
		} else {
			throw new IllegalArgumentException("regionValues should contain 3 strings");
		}
		
	}
	
}
