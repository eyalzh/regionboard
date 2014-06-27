package org.samson.bukkit.plugins.regionboard.region;

import org.bukkit.event.player.PlayerStatisticIncrementEvent;

public interface Region {

	public String getScoreboardDisplayName();
	public String getRegionId();
	
	public String getMainStatisticName();
	public String getSubStatisticName();
	
	public boolean matchPlayerStatisticIncrementEvent(PlayerStatisticIncrementEvent event);

}
