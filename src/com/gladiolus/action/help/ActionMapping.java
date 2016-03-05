package com.gladiolus.action.help;

import java.util.HashMap;
import java.util.Map;

/**
 * kaiser-action.xml中单个action的映射
 * 
 * @author xieguoping
 * 
 */
public class ActionMapping {
	// 操作类型（login、query、delete……）
	private String operate;
	// xml中action的name属性
	private String actionName;
	// xml中action的class属性
	private String actionClass;
	// xml中action配置的result键值对
	private Map<String, String> result = new HashMap<String, String>();

	public Map<String, String> addMapElement(String key, String value) {
		result.put(key, value);
		return this.result;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionClass() {
		return actionClass;
	}

	public void setActionClass(String actionClass) {
		this.actionClass = actionClass;
	}

	public Map<String, String> getResult() {
		return result;
	}

	public void setResult(Map<String, String> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ActionMapping [operate=" + operate + ", actionName="
				+ actionName + ", actionClass=" + actionClass
				+ ", resultMapping=" + result + "]";
	}

}
