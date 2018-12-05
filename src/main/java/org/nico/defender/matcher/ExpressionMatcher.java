package org.nico.defender.matcher;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;
import org.nico.defender.cache.Cache;
import org.nico.defender.cache.ExpressionCache;
import org.nico.defender.guarder.Caller;

public class ExpressionMatcher extends AbstractMatcher{

	private Cache<String, PointcutExpression> cache = new ExpressionCache();
	
	
	@Override
	public boolean match(Caller caller, String pattern) {
		if(! pattern.startsWith("execution")) {
			pattern = "execution(" + pattern + ")";
		}
		
		PointcutExpression pe = cache.get(pattern);
		if(pe == null) {
			PointcutParser pp = PointcutParser.getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution();
			pe = pp.parsePointcutExpression(pattern);
			cache.set(pattern, pe);
		}
		ShadowMatch sm = pe.matchesMethodExecution(caller.getTargetMethod());
		return sm.alwaysMatches();
	}

}
