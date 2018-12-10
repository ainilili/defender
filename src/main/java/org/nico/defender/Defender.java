package org.nico.defender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.nico.defender.guarder.AbstractPreventer;
import org.nico.defender.guarder.Caller;
import org.nico.defender.guarder.Guarder;
import org.nico.defender.guarder.Result;
import org.nico.defender.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.CollectionUtils;

public class Defender {

	private List<Guarder> guarders;

	private BeanDefinitionRegistry registry;

	private DefenderIntercepter intercepter;

	private static Defender instance;

	private static final Logger LOGGER = LoggerFactory.getLogger(Defender.class);

	protected Defender() {
		this.guarders = new ArrayList<>(20);
	}

	public static Defender getInstance() {
		if(instance == null) {
			synchronized (Defender.class) {
				if(instance == null) {
					instance = new Defender();
				}
			}
		}
		return instance;
	}

	public Defender registry(Guarder guarder) {
		guarders.add(guarder);
		return this;
	}

	public Defender ready(){
		{
			if(! CollectionUtils.isEmpty(guarders)){
				guarders.forEach(guarder -> {
					guarder.name(BeanUtils.registerBean(guarder.getPreventer().getClass(), registry));
					LOGGER.debug("Register guarder " + guarder);
				});
			}
			LOGGER.debug("Defender ready to defending.");
		}
		return this;
	}

	public Defender setRegistry(BeanDefinitionRegistry registry) {
		this.registry = registry;
		return this;
	}

	public void initialize() {
		{
			if(! CollectionUtils.isEmpty(guarders)) {
				//Assembly real bean from Spring-Bean-Factory
				guarders.forEach(guarder -> {
					guarder.preventer((AbstractPreventer) DefenderInitialized.getBean(guarder.getName()));
				});

				//Sort guarder list by sort field
				Collections.sort(guarders, new Comparator<Guarder>() {
					@Override
					public int compare(Guarder pre, Guarder next) {
						return pre.getOrder() > next.getOrder() ? 1 : -1;
					}
				});
			}
		}

		//Instance defender intercepter
		intercepter = new DefenderIntercepter(guarders);
	}

	public Result intercept(Caller caller) {
		return intercepter.intercept(caller);
	}

}
