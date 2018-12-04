package org.nico.defender.annotation;

import org.nico.defender.Defender;
import org.nico.defender.advice.DefenderAdvice;
import org.nico.defender.utils.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

public class EnableDefenderRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware{

	private ResourceLoader resourceLoader;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		Defender.getInstance().setRegistry(registry);
		BeanUtils.registerBean(DefenderAdvice.class, registry);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
