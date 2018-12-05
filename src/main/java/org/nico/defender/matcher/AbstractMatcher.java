package org.nico.defender.matcher;

import org.nico.defender.guarder.Caller;

public abstract class AbstractMatcher {

	public abstract boolean match(Caller caller, String pattern); 
}
