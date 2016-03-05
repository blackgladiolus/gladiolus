package com.gladiolus.action.help;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 解析gladiolus-action.xml配置文件中的Action节点
 * 
 */
public class ActionXmlUtil {

	private static Map<String, ActionMapping> actionMappings = new HashMap<String, ActionMapping>();

	/**
	 * 获得action对应的xml映射对象,先找完全匹配的，如果没有完全与action匹配的映射对象则正则模糊匹配
	 * 
	 * @param action
	 * @return
	 */
	public ActionMapping getActionMapping(String action) {

		Iterator<String> it = this.actionMappings.keySet().iterator();

		// 寻找与action完全匹配的映射集合
		while (it.hasNext()) {
			String key = it.next();
			if (action.equalsIgnoreCase(key)) {
				return this.actionMappings.get(action);
			}
		}

		// 寻找与action模糊匹配的映射集合
		Object[] array = this.actionMappings.keySet().toArray();
		for (int i = array.length - 1; i >= 0; i--) {
			String key = array[i].toString();
			if (action.matches(key)) {
				return this.actionMappings.get(key);
			}
		}

		return null;
	}

	public Map<String, ActionMapping> parsexml(String xmlFileURL) {
		ActionMapping actionMapping = null;
		// 实例化SAXReader对象
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			// 获取XML文件对应的XML文档对象
			document = reader.read(xmlFileURL);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		// 获取根节点
		Element palcard = document.getRootElement();
		// 获取指定名称的所有节点
		List<Element> actionElements = palcard.elements("action");
		for (int i = 0; i < actionElements.size(); i++) {
			actionMapping = new ActionMapping();
			Element ele = actionElements.get(i);
			String actionName = ele.attributeValue("name");
			String actionClass = ele.attributeValue("class");
			actionMapping.setActionName(actionName);
			actionMapping.setActionClass(actionClass);

			List<Element> resultElements = ele.elements("result");
			for (int j = 0; j < resultElements.size(); j++) {
				Element resultElement = resultElements.get(j);
				String result = resultElement.attributeValue("name");
				actionMapping.addMapElement(result, resultElement.getText());
			}

			actionMappings.put(actionName, actionMapping);
		}
		return actionMappings;
	}
}
