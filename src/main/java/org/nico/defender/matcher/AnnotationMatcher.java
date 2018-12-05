package org.nico.defender.matcher;

import org.nico.defender.guarder.Caller;

public class AnnotationMatcher extends AbstractMatcher{

	@Override
	public boolean match(Caller caller, String pattern) {
		if(caller.getAccess() != null) {
			String location = caller.getTargetClass().getName();
			return location.startsWith(pattern);
		}else {
			return false;
		}
	}

}
