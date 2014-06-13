package org.samson.bukkit.plugins.regionboard.region;

import org.bukkit.Statistic;

public interface Region {

	String getScoreboardDisplayName();
	String getRegionId();
	Statistic getStatistic();

}
