package com.gladiolus.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import com.gladiolus.aop.aspect.Aspect;

public class SimpleInvocationnHandler implements InvocationHandler {
	// 代理的目标对象
	private Object target = null;
	// 切面集合
	private List<Aspect> aspectList = null;

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public void setAspectList(List<Aspect> aspectList) {
		this.aspectList = aspectList;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		for (Aspect aspect : aspectList) {
			aspect.doBefore();
		}
		Object object = method.invoke(getTarget(), args);

		for (int i = aspectList.size() - 1; i >= 0; i--) {
			aspectList.get(i).doAfter();
		}
		return object;
	}

}
