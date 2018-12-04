package org.nico.defender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.nico.defender.cache.Cache;
import org.nico.defender.cache.GuarderCache;
import org.nico.defender.guarder.AbstractVerify;
import org.nico.defender.utils.BeanUtils;
import org.nico.defender.utils.SpringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.CollectionUtils;

public class Defender {
	
	private static Defender instance;
	
	private List<Guarder> guarders;
	
	private Cache<String, List<Guarder>> cache;
	
	private Object notPass;
	
	private boolean hasInit;
	
	private BeanDefinitionRegistry registry;
	
	private Defender() {
		this.guarders = new ArrayList<>(20);
		this.cache = new GuarderCache();
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
	
	public Defender registryGuarder(Guarder guarder) {
		String beanName = BeanUtils.registerBean(guarder.getVerify().getClass(), registry);
		guarder.setBeanName(beanName);
		guarders.add(guarder);
		return this;
	}
	
	public List<Guarder> getGuarders(String location) {
		if(! CollectionUtils.isEmpty(guarders)) {
			List<Guarder> results = cache.get(location);
			if(results == null) {
				results = new ArrayList<>(guarders.size());
				for(Guarder guarder: guarders) {
					if(guarder.isMatches(location)) {
						results.add(guarder);
					}
				}
			}
			return results;
		}else {
			return null;
		}
	}
	
	public Defender noNotPass(Supplier<Object> callback) {
		notPass = callback.get();
		return this;
	}

	public BeanDefinitionRegistry getRegistry() {
		return registry;
	}

	public void setRegistry(BeanDefinitionRegistry registry) {
		this.registry = registry;
	}

	public Object getNotPass() {
		return notPass;
	}

	public List<Guarder> getGuarders() {
		return guarders;
	}

	public void setGuarders(List<Guarder> guarders) {
		this.guarders = guarders;
	}
	
	public void initGuarder() {
		if(! hasInit) {
			hasInit = ! hasInit;
			if(! CollectionUtils.isEmpty(guarders)) {
				for(Guarder guarder: guarders) {
					guarder.setVerify((AbstractVerify) SpringUtils.getBean(guarder.getBeanName()));
				}
			}
		}
	}
	
}
