package org.samson.bukkit.plugins.regionboard.db;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

public class MapDBServiceTest {

	private static MapDBService mapDB;

	@BeforeClass
	public static void prepareDB() {
		mapDB = new MapDBService(new File("/tmp/temp"));
	}
	
	@Test
	public void SingleSet_Get_ReturnsCorrectValue() {
		
		mapDB.set("key", "value");
		
		String value = mapDB.get("key");
		
		assertEquals(value, "value");
		
	}

	@Test
	public void MultipleSet_Get_ReturnsCorrectValue() {

		mapDB.set("key1", "value1");
		mapDB.set("key2", "value2");
		mapDB.set("key3", "value3");
		
		String value = mapDB.get("key2");
		
		assertEquals(value, "value2");
		
	}
}
