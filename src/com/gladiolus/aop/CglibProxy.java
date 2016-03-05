package com.gladiolus.aop;

import java.lang.reflect.Method;
import java.util.List;

import com.gladiolus.aop.aspect.Aspect;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 通过Cglib实现在方法调用前后添加其他方法
 * 
 * @author jiqinlin
 * 
 */
public class CglibProxy implements MethodInterceptor {

	// 代理的原始对象
	private Object target = null;

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public List<Aspect> getAspectList() {
		return aspectList;
	}

	public void setAspectList(List<Aspect> aspectList) {
		this.aspectList = aspectList;
	}

	// 切面集合
	private List<Aspect> aspectList = null;

	public Object createProxy(Object target) {
		this.target = target;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(this.target.getClass());// 设置代理目标
		enhancer.setCallback(this);// 设置回调
		enhancer.setClassLoader(target.getClass().getClassLoader());
		return enhancer.create();
	}

	/**
	 * 在代理实例上处理方法调用并返回结果
	 * 
	 * @param proxy
	 *            代理类
	 * @param method
	 *            被代理的方法
	 * @param params
	 *            该方法的参数数组
	 * @param methodProxy
	 */
	@Override
	public Object intercept(Object proxy, Method method, Object[] params,
			MethodProxy methodProxy) throws Throwable {

		for (Aspect aspect : aspectList) {
			aspect.doBefore();
		}
		// 调用原始对象的方法
		Object result = methodProxy.invokeSuper(proxy, params);
		for (int i = aspectList.size() - 1; i >= 0; i--) {
			aspectList.get(i).doAfter();
		}

		return result;
	}

}