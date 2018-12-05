package org.nico.defender.annotation;

import java.lang.reflect.Method;

import org.aspectj.lang.annotation.Pointcut;
import org.nico.defender.Defender;
import org.nico.defender.DefenderInitialized;
import org.nico.defender.advice.DefenderAdvice;
import org.nico.defender.utils.BeanUtils;
import org.nico.defender.utils.ReflectUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

public class EnableDefenderRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware{


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		Defender.getInstance().setRegistry(registry);
		
		setExpression(getExpression(importingClassMetadata));
		
		BeanUtils.registerBean(DefenderInitialized.class, registry);
		BeanUtils.registerBean(DefenderAdvice.class, registry);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {}
	
	public String getExpression(AnnotationMetadata importingClassMetadata) {
		String expression = (String) importingClassMetadata.getAnnotationAttributes(EnableDefender.class.getName()).get("value");
		return "execution(" + expression + ")";
	}
	
	public void setExpression(String expression) {
		Method[] methods = DefenderAdvice.class.getDeclaredMethods();
		for (Method method : methods) {
			Pointcut pc = ReflectUtils.getAnnotation(method, Pointcut.class);
			if (pc != null) {
				ReflectUtils.modifyAnnotationProperties(pc, "value", expression);
			}
		}
	}
}
