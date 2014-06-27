package org.samson.bukkit.plugins.regionboard.region;

import static org.junit.Assert.*;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.junit.Test;

public class WorldGuardRegionTest {

	@Test
	public void StatisticsMainPart_Get_ReturnedCorrectly() {
		
		WorldGuardRegion region = new WorldGuardRegion("test-region", "MINE_BLOCK", "DIAMOND_ORE", "Display");
		
		String mainStat = region.getMainStatisticName();
		
		assertEquals("MINE_BLOCK", mainStat);

	}
	
	@Test
	public void StatisticsSubPart_Get_ReturnedCorrectly() {
		
		WorldGuardRegion region = new WorldGuardRegion("test-region", "MINE_BLOCK", "DIAMOND_ORE", "Display");
		
		String subPart = region.getSubStatisticName();
		
		assertEquals("DIAMOND_ORE", subPart);

	}

	@Test
	public void StatisticsSubPartNull_Get_ReturnedCorrectly() {
		
		WorldGuardRegion region = new WorldGuardRegion("test-region", "TREASURE_FISHED", null, "Display");
		
		String subPart = region.getSubStatisticName();
		
		assertEquals(null, subPart);

	}
	
	@Test
	public void StatisticsRegionId_MatchEventWithoutSub_ReturnTrue() {
		
		WorldGuardRegion region = new WorldGuardRegion("test-region", "TREASURE_FISHED", null, "Display");

		PlayerStatisticIncrementEvent event = new PlayerStatisticIncrementEvent(null, Statistic.TREASURE_FISHED, 0, 1);
		boolean isMatched = region.matchPlayerStatisticIncrementEvent(event);

		assertTrue(isMatched);
		
	}

	@Test
	public void StatisticsRegionId_MatchEventWithSub_ReturnTrue() {
		
		WorldGuardRegion region = new WorldGuardRegion("test-region", "MINE_BLOCK", "STONE", "Display");

		PlayerStatisticIncrementEvent event = new PlayerStatisticIncrementEvent(null, Statistic.MINE_BLOCK, 0, 1, Material.STONE);
		
		boolean isMatched = region.matchPlayerStatisticIncrementEvent(event);

		assertTrue(isMatched);
		
	}	
	
	@Test
	public void StatisticsRegionId_Get_ReturnedCorrectly() {
		
		WorldGuardRegion region = new WorldGuardRegion("test-region", "TREASURE_FISHED", null, "Display");
		
		String regionId = region.getRegionId();
		
		assertEquals("test-region", regionId);
		
	}


}
