package org.nico.defender.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class ReflectUtils {

	public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
		return clazz.isAnnotationPresent(annotationClass);
	}
	
	public static <T  extends Annotation > T getAnnotation(Class<?> clazz, Class<T> annotationClass) {
		return clazz.getDeclaredAnnotation(annotationClass);
	}
	
	public static boolean hasAnnotation(Method method, Class<? extends Annotation> annotationClass) {
		return method.isAnnotationPresent(annotationClass);
	}
	
	public static <T  extends Annotation > T getAnnotation(Method method, Class<T> annotationClass) {
		return method.getDeclaredAnnotation(annotationClass);
	}
	
	@SuppressWarnings("unchecked")
	public static void modifyAnnotationProperties(Annotation annotation, String name, Object obj) {
		InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
		Field field = null;
		try {
			field = invocationHandler.getClass().getDeclaredField("memberValues");
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		}
		if(field != null) {
			field.setAccessible(true);
			Map<String, Object> memberValues = null;
			try {
				memberValues = (Map<String, Object>) field.get(invocationHandler);
				memberValues.put(name, obj);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
		}
	}
}
