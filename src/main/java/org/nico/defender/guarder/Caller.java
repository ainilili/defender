package org.nico.defender.guarder;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.nico.defender.annotation.Access;
import org.nico.defender.utils.AspectUtils;

public class Caller {

	private HttpServletRequest request;
	
	private ProceedingJoinPoint point;
	
	private Access access;
	
	private Class<?> targetClass;
	
	private Method targetMethod;
	
	public Caller(HttpServletRequest request, ProceedingJoinPoint point) {
		super();
		this.request = request;
		this.point = point;
		this.targetClass = AspectUtils.getClass(point);
		this.targetMethod = AspectUtils.getMethod(point);
		this.access = this.targetMethod.getDeclaredAnnotation(Access.class);
	}

	public Method getTargetMethod() {
		return targetMethod;
	}
	
	public Class<?> getTargetClass(){
		return targetClass;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public ProceedingJoinPoint getPoint() {
		return point;
	}

	public void setPoint(ProceedingJoinPoint point) {
		this.point = point;
	}

	public Access getAccess() {
		return access;
	}

	public void setAccess(Access access) {
		this.access = access;
	}
}
