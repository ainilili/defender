package org.nico.defender.utils;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class BeanUtils {

	public static String registerBean(Class<?> clazz, BeanDefinitionRegistry registry) {
		        
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);  
		
		String beanName = clazz.getName() + clazz.hashCode();
		registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());  
		return beanName;
	}
	
}
