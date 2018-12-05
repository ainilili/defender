package org.nico.defender.matcher;

import org.nico.defender.guarder.Caller;

public class ExpressionMatcher extends AbstractMatcher{

	@Override
	public boolean match(String pattern, String location) {
		
		return false;
	}

	@Override
	public String parseLocation(Caller caller) {
		return null;
	}

}
