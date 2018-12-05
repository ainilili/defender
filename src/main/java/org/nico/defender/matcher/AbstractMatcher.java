package org.nico.defender.matcher;

import org.nico.defender.guarder.Caller;

public abstract class AbstractMatcher {

	public abstract String parseLocation(Caller caller);
	
	public abstract boolean match(String pattern, String location); 
}
