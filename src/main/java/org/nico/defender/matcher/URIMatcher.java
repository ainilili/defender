package org.nico.defender.matcher;

import javax.servlet.http.HttpServletRequest;

import org.nico.defender.guarder.Caller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class URIMatcher extends AbstractMatcher{

	private PathMatcher antMatcher = new AntPathMatcher();

	@Override
	public boolean match(Caller caller, String pattern) {
		HttpServletRequest request = caller.getRequest();
		String location = request.getMethod() + " " + request.getRequestURI();
		if(pattern.startsWith("/")) {
			pattern = "* " + pattern;
		}
		return antMatcher.match(pattern, location);
	}

}
