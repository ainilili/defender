package org.nico.defender;

import java.util.function.Supplier;

import org.nico.defender.guarder.AbstractVerify;

public class Guarder {

	private String name;
	
	private String pattern;
	
	private AbstractVerify verify; 
	
	private Object error;
	
	private Guarder() {}
	
	public static Guarder builder(){
		return new Guarder();
	}
	
	public Guarder name(String name){
		this.name = name;
		return this;
	}
	
	public Guarder pattern(String pattern){
		this.pattern = pattern;
		return this;
	}
	
	public Guarder verify(AbstractVerify verify){
		this.verify = verify;
		return this;
	}
	
	public Guarder error(Supplier<Object> error){
		this.error = error.get();
		return this;
	}
	
	public Guarder error(Object obj){
		this.error = obj;
		return this;
	}

	public Object getError() {
		return error;
	}

	public String getName() {
		return name;
	}

	public String getPattern() {
		return pattern;
	}

	public AbstractVerify getVerify() {
		return verify;
	}
	
	public boolean isMatches(String location){
		return location.startsWith(pattern);
	}
}
