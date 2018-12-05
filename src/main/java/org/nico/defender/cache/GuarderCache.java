package org.nico.defender.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nico.defender.guarder.Guarder;

public class GuarderCache implements Cache<String, List<Guarder>>{

	private static Map<String, List<Guarder>> cacheMap = new HashMap<>();

	@Override
	public void set(String key, List<Guarder> value) {
		cacheMap.putIfAbsent(key, value);
	}

	@Override
	public void clear() {
		cacheMap.clear();
	}

	@Override
	public List<Guarder> get(String key) {
		return cacheMap.get(key);
	}
	
}
