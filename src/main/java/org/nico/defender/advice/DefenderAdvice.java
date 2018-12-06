package org.nico.defender.advice;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.nico.defender.Defender;
import org.nico.defender.guarder.Caller;
import org.nico.defender.guarder.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@ConditionalOnBean({Defender.class})
public class DefenderAdvice {
	
	private static Logger LOGGER = LoggerFactory.getLogger(DefenderAdvice.class);
	
	@Pointcut("")
	public void defend(){}
	
	@Around(value="defend()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Defender defender = Defender.getInstance();
		
		Caller caller = new Caller(getRequest(), point);
		
		long start = System.nanoTime();
		
		//Interception
		Result intercepter = defender.intercept(caller);
		boolean access = intercepter.isAccess();
		
		long end = System.nanoTime();
		LOGGER.debug("Defended " + caller.getTargetMethod().getName() + " takes " + (end - start)/1000000 + "MS, result " + (access ? "[SUCCESS]" : "[FAILURE]"));
		if(access) {
			return point.proceed();
		}else {
			return intercepter.getError();
		}
	}
	
	private HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return requestAttributes.getRequest();
	}
	
}
