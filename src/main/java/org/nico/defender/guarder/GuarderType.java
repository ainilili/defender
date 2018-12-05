package org.nico.defender.guarder;

import org.nico.defender.matcher.*;

public enum GuarderType {
	
	EXPRESSION(new ExpressionMatcher()),
	
	URI(new URIMatcher()),
	
	ANNOTATION(new AnnotationMatcher()),
	
	;
	
	private AbstractMatcher matcher;

	private GuarderType(AbstractMatcher matcher) {
		this.matcher = matcher;
	}

	public AbstractMatcher matcher() {
		return matcher;
	}
}
