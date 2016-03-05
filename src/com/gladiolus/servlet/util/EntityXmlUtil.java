package com.gladiolus.servlet.util;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 解析bean配置文件
 * 
 * @author xieguoping
 * 
 */
public class EntityXmlUtil {

	private static EntityXmlUtil entityXmlUtil = new EntityXmlUtil();

	private List<EntityMapping> entityMappings = new ArrayList<EntityMapping>();
	// xml配置文件的路径
	private final String path = this.getClass().getClassLoader()
			.getResource("/").getPath()
			+ "gladiolus-action.xml";

	private EntityXmlUtil() {
		this.parsexml(path);
	}

	public static EntityXmlUtil newInstance() {
		return entityXmlUtil;
	}

	@SuppressWarnings("unchecked")
	private List<EntityMapping> parsexml(String path) {
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
		Element palcard = document.getRootElement();
		// 获取指定名称的所有节点
		List<Element> entityElements = palcard.elements("entity");
		EntityMapping entityMapping = null;
		for (int i = 0; i < entityElements.size(); i++) {
			entityMapping = new EntityMapping();
			Element ele = entityElements.get(i);
			String eleAttribute_name = ele.attributeValue("name");
			entityMapping.setName(eleAttribute_name);
			String eleAttribute_class = ele.attributeValue("class");
			entityMapping.setClassName(eleAttribute_class);
			entityMappings.add(entityMapping);
		}
		return entityMappings;
	}

	public String getEntityClass(String entityName) {
		for (EntityMapping entity : entityMappings) {
			if (entityName.equalsIgnoreCase(entity.getName())) {
				return entity.getClassName();
			}
		}
		return null;
	}

}
