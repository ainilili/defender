package org.nico.defender.entity;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.nico.defender.annotation.Access;
import org.nico.defender.utils.AspectUtils;

public class Caller {

	private HttpServletRequest request;
	
	private ProceedingJoinPoint point;
	
	private Access access;
	
	public Caller(HttpServletRequest request, ProceedingJoinPoint point, Access access) {
		super();
		this.request = request;
		this.point = point;
		this.access = access;
	}

	public Method getTargetMethod() {
		return AspectUtils.getMethod(point);
	}
	
	public Class<?> getTargetClass(){
		return AspectUtils.getClass(point);
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
