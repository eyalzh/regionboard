package org.samson.bukkit.plugins.regionboard.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryDBServiceTest {

	@Test
	public void SingleSet_Get_ReturnsCorrectValue() {
		
		MemoryDBService memoryDB = new MemoryDBService();
		memoryDB.set("key", "value");
		
		String value = memoryDB.get("key");
		
		assertEquals(value, "value");
		
	}

	@Test
	public void MultipleSet_Get_ReturnsCorrectValue() {
		
		MemoryDBService memoryDB = new MemoryDBService();
		
		memoryDB.set("key1", "value1");
		memoryDB.set("key2", "value2");
		memoryDB.set("key3", "value3");
		
		String value = memoryDB.get("key2");
		
		assertEquals(value, "value2");
		
	}
	
}
