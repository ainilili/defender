package org.nico.defender.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nico.defender.enums.AccessLevel;

/**
 * The identifying what condition your api needed! 
 * 
 * @author nico
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Access {

	/**
	 * {@link AccessLevel}
	 * 
	 * @return
	 */
	AccessLevel value();
	
	/**
	 * Description
	 * 
	 * @return
	 */
	String description() default "niconico";
}
