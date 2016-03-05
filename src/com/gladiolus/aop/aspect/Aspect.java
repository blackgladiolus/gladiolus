package com.gladiolus.aop.aspect;

public interface Aspect {
	// 事先执行
	public Object doBefore();

	// 事后执行
	public Object doAfter();
}
