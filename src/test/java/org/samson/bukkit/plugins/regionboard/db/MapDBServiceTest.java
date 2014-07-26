package org.samson.bukkit.plugins.regionboard.db;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MapDBServiceTest {

	private static MapDBService mapDB;

	@Before
	public void prepareDB() {
		mapDB = new MapDBService(new File("/tmp/temp"));
	}
	
	@After
	public void cleanDB() {
		mapDB.clean();
		mapDB.close();
		mapDB = null;
	}
	
	@Test
	public void SingleSet_Get_ReturnsCorrectValue() {
		
		mapDB.set("key", "value");
		
		String value = mapDB.get("key");
		
		assertEquals("value", value);
		
	}

	@Test
	public void MultipleSet_Get_ReturnsCorrectValue() {

		mapDB.set("key1", "value1");
		mapDB.set("key2", "value2");
		mapDB.set("key3", "value3");
		
		String value = mapDB.get("key2");
		
		assertEquals("value2", value);
		
	}
	
	@Test
	public void MultipleSet_GetAll_ReturnsAllValues() {

		mapDB.set("key1", "value1");
		mapDB.set("key2", "value2");
		mapDB.set("key3", "value3");
		
		Set<String> values = mapDB.getAll();
		
		assertTrue(values.contains("key1"));
		assertTrue(values.contains("key2"));
		assertTrue(values.contains("key3"));
		
		assertEquals(3, values.size());
		
	}	
	
}
