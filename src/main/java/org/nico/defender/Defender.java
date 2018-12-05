package org.nico.defender;

import java.util.ArrayList;
import java.util.List;

import org.nico.defender.cache.Cache;
import org.nico.defender.cache.GuarderCache;
import org.nico.defender.entity.Guarder;
import org.nico.defender.guarder.AbstractPreventer;
import org.nico.defender.utils.BeanUtils;
import org.nico.defender.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.CollectionUtils;

public class Defender {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Defender.class);
	
	private static Defender instance;
	
	private List<Guarder> guarders;
	
	private Cache<String, List<Guarder>> cache;
	
	private boolean initialized;
	
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
	
	public Defender registry(Guarder guarder) {
		guarders.add(guarder);
		return this;
	}
	
	public Defender ready(){
		if(! CollectionUtils.isEmpty(guarders)){
			guarders.forEach(g -> {
				String beanName = BeanUtils.registerBean(g.getPreventer().getClass(), registry);
				g.name(beanName);
			});
			LOGGER.debug("Defender ready to defending !!");
		}
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
	
	public void setRegistry(BeanDefinitionRegistry registry) {
		this.registry = registry;
	}
	
	public void initialize() {
		if(! initialized) {
			initialized = ! initialized;
			if(! CollectionUtils.isEmpty(guarders)) {
				for(Guarder guarder: guarders) {
					guarder.preventer((AbstractPreventer) SpringUtils.getBean(guarder.getName()));
				}
			}
		}
	}
	
}
