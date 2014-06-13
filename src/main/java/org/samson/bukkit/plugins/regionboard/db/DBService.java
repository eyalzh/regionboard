package org.samson.bukkit.plugins.regionboard.db;

public interface DBService {

	public String get(String key);
	public void set(String key, String value);
	
	public void close();
	
}
