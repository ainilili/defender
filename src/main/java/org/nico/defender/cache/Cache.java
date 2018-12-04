package org.nico.defender.cache;

public interface Cache<K, V> {

	public void set(K key, V value);
	
	public void clear();
	
	public V get(K key);
}
