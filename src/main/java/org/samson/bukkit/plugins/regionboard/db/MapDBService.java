package org.samson.bukkit.plugins.regionboard.db;

import java.io.File;
import java.util.Set;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

public class MapDBService implements DBService {
	
	private File datafile;
	private DB db;
	private HTreeMap<String, String> hashMap;

	public MapDBService(File datafile) {
		this.datafile = datafile;
		initDB();
	}

	@Override
	public String get(String key) {
		return hashMap.get(key);
	}

	@Override
	public void set(String key, String value) {
		hashMap.put(key, value);
		db.commit();
	}

	private void initDB() {
		db = DBMaker
				.newFileDB(datafile)
				.make();
		
		hashMap = db.getHashMap("main");
	}

	@Override
	public void close() {
		db.close();
	}

	@Override
	public Set<String> getAll() {
		return hashMap.keySet();
	}

	@Override
	public void clean() {
		hashMap.clear();
		db.commit();
	}

	@Override
	public void remove(String key) {
		hashMap.remove(key);
		db.commit();
	}	
	
}
