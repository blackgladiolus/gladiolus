package com.gladiolus.dao;

import java.util.Map;

import com.gladiolus.util.parsexml.BeansXmlUtil;

public class DaoFactory {

	private static DaoFactory daoFactory = new DaoFactory();

	private DaoFactory() {
	}

	/**
	 * 单例模式，返回daoFactory
	 * 
	 * @return
	 */
	public static DaoFactory newInstance() {
		return daoFactory;
	}

	/**
	 * 根据参数得到具体的dao
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public GenericDao getDao(String id) {

		Map<String, String> beanMap = BeansXmlUtil.getBeansXmlUtil()
				.getBeanMap();
		try {
			return (GenericDao) Class.forName(beanMap.get(id)).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
