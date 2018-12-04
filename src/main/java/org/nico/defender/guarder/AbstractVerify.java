package org.nico.defender.guarder;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;

public interface AbstractVerify {

	public boolean action(HttpServletRequest request, ProceedingJoinPoint point);
}
