package org.nico.defender;

import org.nico.defender.guarder.AbstractVerify;

public class Guarder {

	private String pattern;
	
	private AbstractVerify verify; 
	
	private String beanName;

	public Guarder(String pattern, AbstractVerify verify) {
		this.pattern = pattern;
		this.verify = verify;
	}

	public AbstractVerify getVerify() {
		return verify;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setVerify(AbstractVerify verify) {
		this.verify = verify;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public boolean isMatches(String location) {
		return location.startsWith(pattern);
	}
	
}
