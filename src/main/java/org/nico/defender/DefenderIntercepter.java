package org.nico.defender;

import java.util.List;

import org.nico.defender.cache.Cache;
import org.nico.defender.guarder.Caller;
import org.nico.defender.guarder.Guarder;
import org.springframework.util.CollectionUtils;

public class DefenderIntercepter{
	
	private List<Guarder> guarders;
	
	public DefenderIntercepter(List<Guarder> guarders) {
		this.guarders = guarders;
	}

	public Guarder intercept(Caller caller) {
		Guarder intercepter = null;
		if(! CollectionUtils.isEmpty(guarders)) {
			for(Guarder guarder: guarders) {
				if(guarder.isMatches(caller)) {
					boolean access = guarder.detection(caller);
					if(! access) {
						intercepter = guarder;
						break;
					}
				}
			}
		}
		return intercepter;
	}
}
