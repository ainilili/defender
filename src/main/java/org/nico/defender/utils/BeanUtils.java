package org.nico.defender.utils;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class BeanUtils {

	public static AtomicInteger at = new AtomicInteger(0);
	
	public static String registerBean(Class<?> clazz, BeanDefinitionRegistry registry) {
		        
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);  
		
		String beanName = clazz.getName() + at.getAndIncrement();
		registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());  
		
		return beanName;
	}
	
}
