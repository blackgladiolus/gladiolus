package com.gladiolus.servlet.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;


/**
 * 将表单数据封装成bean对象
 * 
 * @author xieguoping
 * 
 */
public class FromToBean {

	private static FromToBean fromConveryBeans = new FromToBean();

	private FromToBean() {
	}

	public static FromToBean newInstance() {
		return fromConveryBeans;
	}

	/**
	 * 泛型方法将request域中的数据转换为entity对象
	 * 
	 * @param request
	 * @param entity
	 *            要转换成的对象
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T fromDataToBean(HttpServletRequest request, T entity) {
		Class clazz = entity.getClass();
		return (T) fromDataToBean(request, clazz);
	}

	/**
	 * 将request域中的数据转换为class对应的bean对象
	 * 
	 * @param request
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object fromDataToBean(HttpServletRequest request, Class clazz) {
		// 获得表单中对象和参数的键值对Map集合
		List<String> parametersNames = getClazzParametersNames(request,
				clazz.getSimpleName());
		// 获得对象的所有属性
		Field[] fields = clazz.getDeclaredFields();

		Object obj = null;
		try {
			obj = clazz.newInstance();
			for (int x = 0; x < fields.length; x++) {
				for (String temp : parametersNames) {
					if (temp.split("\\.")[1].equalsIgnoreCase(fields[x]
							.getName())) {
						String value = request.getParameter(temp);
						if (value != null && !value.trim().isEmpty()) {
							BeanUtils.setProperty(obj, fields[x].getName(),
									value);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("request域中的数据转换为"
					+ clazz.getSimpleName() + "bean对象失败！" + e);
		}
		return obj;
	}

	/**
	 * 将request中传入的表单数据封装成对象的集合
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Object> fromDataToBeans(HttpServletRequest request) {
		// 获得提交表单中包含的所有对象名
		Set<String> clazzNames = this.getFromAllClazzName(request);
		// 根据类名获得类的对象集合
		List<Class> clazzList = this.getClazzs(clazzNames);
		// 定义一个集合用来保存结果的集合
		List<Object> resultObj = new ArrayList<Object>();
		for (Class clazz : clazzList) {
			resultObj.add(this.fromDataToBean(request, clazz));
		}
		return resultObj;
	}

	/**
	 * 获得提交表单中包含的所有对象名
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Set<String> getFromAllClazzName(HttpServletRequest request) {
		Set<String> clazzNames = new HashSet<String>();// 保存类名
		Enumeration parameters = request.getParameterNames();
		while (parameters.hasMoreElements()) {
			Object obj = parameters.nextElement();
			String[] pra = obj.toString().split("\\.");
			if (pra.length > 1) {
				String clazzName = pra[0].substring(0, 1).toUpperCase()
						+ pra[0].substring(1);
				clazzNames.add(clazzName);
			}
		}
		return clazzNames;
	}

	/**
	 * 通过类路径下的名为kaiser_beans.xml的配置文件，获得对应的Class对象
	 * 
	 * @param entityNames
	 *            类名的集合
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<Class> getClazzs(Set<String> entityNames) {
		List<Class> clazzs = new ArrayList<Class>();// 保存类的clazz对象
		for (String temp : entityNames) {
			clazzs.add(this.getClazzs(temp));
		}
		return clazzs;
	}

	@SuppressWarnings("rawtypes")
	private Class getClazzs(String entityName) {
		EntityXmlUtil entityXmlUtil = EntityXmlUtil.newInstance();
		try {
			String realEntityName = entityName.substring(0, 1).toUpperCase()
					+ entityName.substring(1);// 获得类名
			String clazz = entityXmlUtil.getEntityClass(realEntityName);
			return Class.forName(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得类名对应的参数列表
	 * 
	 * @param request
	 * @param clazzName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<String> getClazzParametersNames(HttpServletRequest request,
			String clazzName) {
		List<String> parametersNames = new ArrayList<String>();// 类对应的参数
		Enumeration parameters = request.getParameterNames();// 获得request域中所有的参数
		while (parameters.hasMoreElements()) {
			Object parameter = parameters.nextElement();
			String[] pra = parameter.toString().split("\\.");
			if (pra != null | !pra.toString().equals("[]") | pra.length != 0) {
				if (clazzName.equalsIgnoreCase(pra[0])) {
					parametersNames.add(parameter.toString());
				}
			}
		}
		return parametersNames;
	}

}
