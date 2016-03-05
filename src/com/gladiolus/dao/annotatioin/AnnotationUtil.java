package com.gladiolus.dao.annotatioin;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class AnnotationUtil {

	/**
	 * 获得类级别中表名的注解
	 * 
	 * @param clazz
	 * @return
	 */

	public String getClassTable(Class clazz) {
		Table table = (Table) clazz// 获取Table注解
				.getAnnotation(Table.class);
		return table == null ? null : table.value();
	}


	public List<Field> getForeignColumns(Class clazz) {
		List<Field> InderectColumn = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			ForeignColumn inderectColumn = field
					.getAnnotation(ForeignColumn.class);
			if (inderectColumn != null) {
				InderectColumn.add(field);
			}
		}
		return InderectColumn;
	}

	/**
	 * 获得类中间接表字段的集合
	 * 
	 * @param clazz
	 * @return
	 */
	public List<Field> getInderectColumns(Class clazz) {
		List<Field> InderectColumn = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			InderectColumn inderectColumn = field
					.getAnnotation(InderectColumn.class);
			if (inderectColumn != null) {
				InderectColumn.add(field);
			}
		}
		return InderectColumn;
	}

	/**
	 * 获得有主键注解的属性
	 * 
	 * @param clazz
	 * @return
	 */
	public Field getPrimaryField(Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		Field primaryField = null;
		for (Field field : fields) {
			if (field.isAnnotationPresent(PrimaryColumn.class)) {
				primaryField = field;
			}
		}
		return primaryField;
	}

	 

	/**
	 * 类是否含有TableAnnotation注解，存在返回true，不存在返回false
	 * 
	 * @param clazz
	 * @return
	 */
	public boolean isHasTableAnnotation(Class clazz) {
		boolean isExist = clazz.isAnnotationPresent(Table.class);
		if (!isExist) {
			System.out.println(clazz.getName() + "类不存在TableAnnotation注解");
			return false;
		}
		return true;
	}
}
