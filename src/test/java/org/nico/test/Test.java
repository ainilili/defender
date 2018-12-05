package org.nico.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.aspectj.lang.annotation.Pointcut;
import org.nico.defender.advice.DefenderAdvice;
import org.nico.defender.annotation.Access;


public class Test {
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		
		Class<?> clazz = DefenderAdvice.class;
		
		Method m = clazz.getDeclaredMethod("defend", Access.class);
		
		Pointcut componentScan = m.getAnnotation(Pointcut.class);
		
		
		InvocationHandler invocationHandler = Proxy.getInvocationHandler(componentScan);
		Field value = invocationHandler.getClass().getDeclaredField("memberValues");
		value.setAccessible(true);
		Map<String, Object> memberValues = (Map<String, Object>) value.get(invocationHandler);
		memberValues.put("value", "hello");
		
		System.out.println(componentScan.value());
		
		m = clazz.getDeclaredMethod("defend", Access.class);
		componentScan = m.getAnnotation(Pointcut.class);
		System.out.println(componentScan.value());
	}
}
