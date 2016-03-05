package com.gladiolus.dao.annotatioin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
// 作用于属性
@Retention(RetentionPolicy.RUNTIME)
// 有效期为运行时
public @interface ForeignColumn {

	String Table();

	String ColumnName();

}
