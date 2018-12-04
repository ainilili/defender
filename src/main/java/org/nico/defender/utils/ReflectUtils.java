package org.nico.defender.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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
}
