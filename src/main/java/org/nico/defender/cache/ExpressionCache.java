package org.nico.defender.cache;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.weaver.tools.PointcutExpression;

public class ExpressionCache implements Cache<String, PointcutExpression>{

	private static Map<String, PointcutExpression> cacheMap = new HashMap<>();

	@Override
	public void set(String key, PointcutExpression value) {
		cacheMap.putIfAbsent(key, value);
	}

	@Override
	public void clear() {
		cacheMap.clear();
	}

	@Override
	public PointcutExpression get(String key) {
		return cacheMap.get(key);
	}
	
}
