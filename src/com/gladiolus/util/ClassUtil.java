package com.gladiolus.util;

import java.beans.IntrospectionException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ClassUtil {

	/**
	 * 获得类路径
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getClassPath(Class clazz) {
		return clazz.getClassLoader().getResource("/").getPath();
	}

	/**
	 * 获得所有的对象对应的字节码文件
	 * 
	 * @param objects
	 * @return
	 */

	public static List<Class> getClass(List<Object> objects) {
		List<Class> clazzs = new ArrayList<Class>();
		for (Object obj : objects) {
			clazzs.add(obj.getClass());
		}
		return clazzs;
	}

	/**
	 * 获得所有的对象对应的字节码文件
	 * 
	 * @param objects
	 * @return
	 */
	public static Class[] getClass(Object[] objects) {
		Class[] clazzs = new Class[objects.length];
		for (int i = 0; i < objects.length; i++) {
			clazzs[i] = objects[i].getClass();
		}
		return clazzs;
	}

	/**
	 * 获得clazz对象中所有的写方法
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method[] getWriteMethods(Class clazz) {
		// 获得clazz对象中所有的私有属性
		Field[] fields = clazz.getDeclaredFields();
		Method[] writeMethods = new Method[fields.length];
		for (int i = 0; i < fields.length; i++) {
			// 获得写方法;
			writeMethods[i] = getWriteMethod(clazz, fields[i]);
		}
		return writeMethods;
	}

	/**
	 * 获得clazz对象中属性的写方法
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method getWriteMethod(Class clazz, Field field) {
		PropertyDescriptor pd = null;
		Method writeMethod = null;
		try {
			pd = new PropertyDescriptor(field.getName(), clazz);
			// 获得写方法;
			writeMethod = pd.getWriteMethod();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return writeMethod;
	}

	/**
	 * 获得属性的值
	 * 
	 * @param obj
	 * @param field
	 * @return
	 */
	public Object getFieldValue(Object obj, Field field) {
		Object value = null;
		try {
			PropertyDescriptor pd = new PropertyDescriptor(field.getName(),
					obj.getClass());
			Method readMethod = pd.getReadMethod();
			value = readMethod.invoke(obj);
			if (value == null) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}
