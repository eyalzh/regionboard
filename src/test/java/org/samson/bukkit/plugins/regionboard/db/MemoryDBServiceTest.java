package org.samson.bukkit.plugins.regionboard.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryDBServiceTest {

	@Test
	public void SingleSet_Get_ReturnsCorrectValue() {
		
		MemoryDBService memoryDB = new MemoryDBService();
		memoryDB.set("key", new String[]{"value"});
		
		String[] value = memoryDB.get("key");
		
		assertEquals("value", value[0]);
		
	}

	@Test
	public void MultipleSet_Get_ReturnsCorrectValue() {
		
		MemoryDBService memoryDB = new MemoryDBService();
		
		memoryDB.set("key1", new String[]{"value1"});
		memoryDB.set("key2", new String[]{"value2"});
		memoryDB.set("key3", new String[]{"value3"});
		
		String[] value = memoryDB.get("key2");
		
		assertEquals("value2", value[0]);
		
	}
	
}
