package org.samson.bukkit.plugins.regionboard.db;

import java.util.Set;

public interface DBService {

	public String[] get(String key);
	public void set(String key, String[] value);
	
	public void close();
	public Set<String> getAll();
	public void clean();
	
}
