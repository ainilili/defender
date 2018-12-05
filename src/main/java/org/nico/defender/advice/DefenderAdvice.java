package org.nico.defender.advice;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.nico.defender.Defender;
import org.nico.defender.annotation.Access;
import org.nico.defender.entity.Caller;
import org.nico.defender.entity.Guarder;
import org.nico.defender.utils.AspectUtils;
import org.nico.defender.utils.ReflectUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class DefenderAdvice {
	
	@Pointcut("@annotation(access)")
	public void defend(Access access){}
	
	@Around(value="defend(access)")
	public Object around(ProceedingJoinPoint point, Access access) throws Throwable {
		Defender defender = Defender.getInstance();
		
		defender.initialize();
		
		boolean pass = true;
		
		List<Guarder> guarders = null;
		Guarder prohibitor = null;
		
		Class<?> targetClass = AspectUtils.getClass(point);
		if(ReflectUtils.hasAnnotation(targetClass, Access.class)) {
			guarders = defender.getGuarders(targetClass.getPackage().getName());
		}else {
			Method targetMethod = AspectUtils.getMethod(point);
			if(targetMethod != null) {
				if(ReflectUtils.hasAnnotation(targetMethod, Access.class)) {
					guarders = defender.getGuarders(targetClass.getPackage().getName());
				}
			}
		}
		
		if(! CollectionUtils.isEmpty(guarders)) {
			for(Guarder guarder: guarders) {
				boolean verifyResult = guarder.getPreventer().detection(new Caller(getRequest(), point, access));
				if(! verifyResult) {
					pass = false;
					prohibitor = guarder;
					break;
				}
			}
		}
		
		if(pass) {
			return point.proceed();
		}else {
			return prohibitor.getError();
		}
		
	}
	
	public HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return requestAttributes.getRequest();
	}
	
}
