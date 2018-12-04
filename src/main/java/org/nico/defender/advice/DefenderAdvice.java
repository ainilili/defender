package org.nico.defender.advice;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.nico.defender.Defender;
import org.nico.defender.Guarder;
import org.nico.defender.annotation.Access;
import org.nico.defender.utils.AspectUtils;
import org.nico.defender.utils.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class DefenderAdvice {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DefenderAdvice.class);

	@Pointcut("@annotation(access)")
	public void defend(Access access){}
	
	@Around(value="defend(access)")
	public Object around(ProceedingJoinPoint point, Access access) throws Throwable {
		
		Defender.getInstance().initGuarder();
		
		boolean pass = true;
		
		Class<?> targetClass = AspectUtils.getClass(point);
		if(ReflectUtils.hasAnnotation(targetClass, Access.class)) {
			List<Guarder> guarders = Defender.getInstance().getGuarders(targetClass.getPackage().getName());
			if(! CollectionUtils.isEmpty(guarders)) {
				for(Guarder guarder: guarders) {
					boolean verifyResult = guarder.getVerify().action(getRequest(), point);
					if(! verifyResult) {
						pass = false;
						break;
					}
				}
			}
		}else {
			Method targetMethod = AspectUtils.getMethod(point);
			if(targetMethod != null) {
				if(ReflectUtils.hasAnnotation(targetMethod, Access.class)) {
					List<Guarder> guarders = Defender.getInstance().getGuarders(targetClass.getPackage().getName());
					if(! CollectionUtils.isEmpty(guarders)) {
						for(Guarder guarder: guarders) {
							boolean verifyResult = guarder.getVerify().action(getRequest(), point);
							if(! verifyResult) {
								pass = false;
								break;
							}
						}
					}
				}
			}
		}
		
		if(pass) {
			return point.proceed();
		}else {
			return Defender.getInstance().getNotPass();
		}
		
	}
	
	public HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return requestAttributes.getRequest();
	}
	
}
