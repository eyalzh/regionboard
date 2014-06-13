package org.samson.bukkit.plugins.regionboard.db;

import java.util.HashMap;
import java.util.Map;

public class MemoryDBService implements DBService {

	Map<String, String> memory = new HashMap<String, String>();
	
	@Override
	public String get(String key) {
		return memory.get(key);
	}

	@Override
	public void set(String key, String value) {
		memory.put(key, value);
	}

	@Override
	public void close() {
	}

}
