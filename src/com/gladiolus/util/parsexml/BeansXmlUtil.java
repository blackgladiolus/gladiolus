package com.gladiolus.util.parsexml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class BeansXmlUtil {

	//保存gladiolus-beans.xml中bean
	private static Map<String, String> beanMap = null;

	private static BeansXmlUtil beansXmlUtil = new BeansXmlUtil();

	// xml配置文件的路径
	private final String xmlPath = this.getClass().getClassLoader()
			.getResource("\\").getPath()
			+ "gladiolus-beans.xml";

	private BeansXmlUtil() {
	}

	public static BeansXmlUtil getBeansXmlUtil() {
		return beansXmlUtil;
	}

	public Map<String, String> getBeanMap() {

		if (beanMap == null) {
			synchronized (BeansXmlUtil.class) {
				if (beanMap == null) {
					this.parsexml(xmlPath);
				}
			}
		}
		return beanMap;
	}

	@SuppressWarnings({ "unchecked" })
	private Map<String, String> parsexml(String path) {

		beanMap = new HashMap<String, String>();

		// 实例化SAXReader对象
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			// 获取XML文件对应的XML文档对象
			document = reader.read(path);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		// 获取根节点
		Element root = document.getRootElement();

		// 获取指定名称的所有节点
		List<Element> entityElements = root.elements("bean");

		for (int i = 0; i < entityElements.size(); i++) {
			Element ele = entityElements.get(i);
			String id = ele.attributeValue("id");
			String className = ele.attributeValue("class");
			beanMap.put(id, className);
		}
		return null;
	}

}
