package org.samson.bukkit.plugins.regionboard.region;

import static org.junit.Assert.*;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.junit.Test;

public class WorldGuardRegionTest {

	@Test
	public void GetMainStatisticName_ShouldReturnRightValue() {
		
		WorldGuardRegion region = new WorldGuardRegion("test-region", "MINE_BLOCK", "DIAMOND_ORE", "Display");
		
		String mainStat = region.getMainStatisticName();
		
		assertEquals("MINE_BLOCK", mainStat);

	}
	
	@Test
	public void GetSubStatisticName_ShouldReturnRightValue() {
		
		WorldGuardRegion region = new WorldGuardRegion("test-region", "MINE_BLOCK", "DIAMOND_ORE", "Display");
		
		String subPart = region.getSubStatisticName();
		
		assertEquals("DIAMOND_ORE", subPart);

	}

	@Test
	public void GetSubStatisticName_ShouldReturnNull_WhenEmpty() {
		
		WorldGuardRegion region = new WorldGuardRegion("test-region", "TREASURE_FISHED", null, "Display");
		
		String subPart = region.getSubStatisticName();
		
		assertEquals(null, subPart);

	}
	
	@Test
	public void MatchStatistic_ShouldReturnTrue_WhenMainStatsMatch() {
		
		WorldGuardRegion region = new WorldGuardRegion("test-region", "TREASURE_FISHED", null, "Display");

		boolean isMatched = region.matchStatistic(Statistic.TREASURE_FISHED, null, null);

		assertTrue(isMatched);
		
	}

	@Test
	public void MatchStatistic_ShouldReturnTrue_WhenMainAndSubStatsMatch() {
		
		WorldGuardRegion region = new WorldGuardRegion("test-region", "MINE_BLOCK", "STONE", "Display");
		
		boolean isMatched = region.matchStatistic(Statistic.MINE_BLOCK, Material.STONE, null);

		assertTrue(isMatched);
		
	}	
	
	@Test
	public void MatchStatistic_ShouldReturnFalse_WhenSubStatsDoNotMatch() {
		
		WorldGuardRegion region = new WorldGuardRegion("test-region", "MINE_BLOCK", "STONE", "Display");
		
		boolean isMatched = region.matchStatistic(Statistic.MINE_BLOCK, Material.ANVIL, null);

		assertFalse(isMatched);
		
	}		
	
	@Test
	public void GetRegionId_ShouldReturnRightValue() {
		
		WorldGuardRegion region = new WorldGuardRegion("test-region", "TREASURE_FISHED", null, "Display");
		
		String regionId = region.getRegionId();
		
		assertEquals("test-region", regionId);
		
	}


}
