package org.nico.defender.matcher;

import javax.servlet.http.HttpServletRequest;

import org.nico.defender.guarder.Caller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class URIMatcher extends AbstractMatcher{

	private PathMatcher antMatcher = new AntPathMatcher();
	
	@Override
	public boolean match(String pattern, String location) {
		if(pattern.startsWith("/")) {
			pattern = "* " + pattern;
		}
		return antMatcher.match(pattern, location);
	}

	@Override
	public String parseLocation(Caller caller) {
		HttpServletRequest request = caller.getRequest();
		return request.getMethod() + " " + request.getRequestURI();
	}

}
