package org.nico.defender.entity;

import java.util.function.Supplier;

import org.nico.defender.guarder.AbstractPreventer;

public class Guarder {

	private String name;
	
	private String pattern;
	
	private AbstractPreventer preventer; 
	
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
	
	public Guarder preventer(AbstractPreventer preventer){
		this.preventer = preventer;
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

	public AbstractPreventer getPreventer() {
		return preventer;
	}

	public boolean isMatches(String location){
		return location.startsWith(pattern);
	}
}
