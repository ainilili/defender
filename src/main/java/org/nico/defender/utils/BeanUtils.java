package org.nico.defender.utils;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class BeanUtils {

	public static AtomicInteger at = new AtomicInteger(0);
	
	public static String registerBean(Class<?> clazz, BeanDefinitionRegistry registry) {
		        
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);  
		
		String beanName = clazz.getName() + at.getAndIncrement();
		registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());  
		
		return beanName;
	}
	
	public static Object getBean(String beanName) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		return wac.getBean(beanName);
	}
}
