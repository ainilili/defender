package org.nico.defender.guarder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.nico.defender.matcher.AbstractMatcher;
import org.springframework.util.CollectionUtils;

public class Guarder {

	private String name;
	
	private List<String> patterns;
	
	private GuarderType type;
	
	private AbstractPreventer preventer; 
	
	private Object error;
	
	private int order;
	
	private Guarder() {
		this.patterns = new ArrayList<>();
	}
	
	public static Guarder builder(GuarderType type){
		return new Guarder().type(type);
	}
	
	public Guarder name(String name){
		this.name = name;
		return this;
	}
	
	public Guarder pattern(String pattern){
		this.patterns.add(pattern);
		return this;
	}
	
	public Guarder type(GuarderType type){
		this.type = type;
		return this;
	}
	
	public Guarder order(int order){
		this.order = order;
		return this;
	}
	
	public Guarder preventer(AbstractPreventer preventer){
		this.preventer = preventer;
		return this;
	}
	
	public Result detection(Caller caller) {
		return this.preventer.detection(caller);
	}
	
	public Guarder error(Supplier<Object> error){
		this.error = error.get();
		return this;
	}
	
	public Guarder error(Object obj){
		this.error = obj;
		return this;
	}

	public int getOrder() {
		return order;
	}

	public Object errorMessage() {
		return error;
	}

	public String getName() {
		return name;
	}

	public List<String> getPatterns() {
		return patterns;
	}

	public AbstractPreventer getPreventer() {
		return preventer;
	}

	public boolean isMatches(Caller caller){
		boolean matches = false;
		if(! CollectionUtils.isEmpty(patterns)) {
			for(String pattern: patterns) {
				AbstractMatcher matcher = type.matcher();
				matches = matcher.match(caller, pattern);
				if(matches) {
					break;
				}
			}
		}
		return matches;
	}

	public GuarderType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Guarder [name=" + name + ", patterns=" + patterns + ", type=" + type + ", order=" + order + "]";
	}

}
