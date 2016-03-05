package com.gladiolus.dao.help;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

public class ResultSetToBeans {

	/**
	 * 将数据绑定成对象的集合 使用要求：必须保证类的属性名和数据库字段名一致
	 * 
	 * @param rs
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> bindDataToBeans(ResultSet rs, Class<?> clazz) {
		// 保存转换后的结果
		List<T> resultList = new ArrayList<T>();
		// 获得对象的所有属性
		Field[] fields = clazz.getDeclaredFields();
		Object obj = null;
		try {
			while (rs.next()) {
				obj = clazz.newInstance();
				for (int i = 0; i < fields.length; i++) {
					Object rsValue = rs.getObject(fields[i].getName());
					if (rsValue != null) {
						BeanUtils
								.setProperty(obj, fields[i].getName(), rsValue);
					}
				}
				resultList.add((T) obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return resultList;
	}
}
