package com.gladiolus.dao.annotatioin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xieguoping
 * value:数据库表名
 */

@Target(ElementType.TYPE)
// 作用域 类或接口
@Retention(RetentionPolicy.RUNTIME)
// 有效期为运行时
public @interface Table {
	String value();
}