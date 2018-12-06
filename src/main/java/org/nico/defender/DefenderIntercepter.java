package org.nico.defender;

import java.util.List;

import org.nico.defender.guarder.Caller;
import org.nico.defender.guarder.Guarder;
import org.nico.defender.guarder.Result;
import org.springframework.util.CollectionUtils;

public class DefenderIntercepter{
	
	private List<Guarder> guarders;
	
	public DefenderIntercepter(List<Guarder> guarders) {
		this.guarders = guarders;
	}

	public Result intercept(Caller caller) {
		Result intercepter = null;
		if(! CollectionUtils.isEmpty(guarders)) {
			for(Guarder guarder: guarders) {
				if(guarder.isMatches(caller)) {
					intercepter = guarder.detection(caller);
					if(! intercepter.isAccess()) {
						break;
					}
				}
			}
		}
		return intercepter == null ? Result.pass() : intercepter;
	}
}
