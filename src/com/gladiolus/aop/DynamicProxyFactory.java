package com.gladiolus.aop;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.gladiolus.aop.aspect.Aspect;
/**
 * 动态代理工厂类，得到代理对象
 * @author xieguoping
 *
 */
public class DynamicProxyFactory {

	private DynamicProxyFactory() {

	}

	public static Object newInstance(Object target, Aspect aspect) {
		List<Aspect> aspectList = new ArrayList<Aspect>();
		aspectList.add(aspect);
		return newInstance(target, aspectList);
	}

	public static Object newInstance(Object target, List<Aspect> aspectList) {

		if (!target.getClass().getInterfaces().getClass().getSimpleName()
				.equals("Class[]")) {
			SimpleInvocationnHandler handler = new SimpleInvocationnHandler();
			handler.setTarget(target);
			handler.setAspectList(aspectList);
			return Proxy.newProxyInstance(target.getClass().getClassLoader(),
					target.getClass().getInterfaces(), handler);
		} else {
			CglibProxy cglb = new CglibProxy();
			cglb.setTarget(target);
			cglb.setAspectList(aspectList);
			return cglb.createProxy(target);
		}

	}
}
