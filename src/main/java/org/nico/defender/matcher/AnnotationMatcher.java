package org.nico.defender.matcher;

import org.nico.defender.guarder.Caller;

public class AnnotationMatcher extends AbstractMatcher{

	@Override
	public boolean match(String pattern, String location) {
		if(location != null) {
			return location.startsWith(pattern);
		}else {
			return false;
		}
	}

	@Override
	public String parseLocation(Caller caller) {
		if(caller.getAccess() != null) {
			return caller.getTargetClass().getName();
		}else {
			return null;
		}
	}

}
