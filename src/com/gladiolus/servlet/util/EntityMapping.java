package com.gladiolus.servlet.util;

public class EntityMapping {
	// xml中entity的name属性
	private String name;
	// xml中entity的class属性
	private String className;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "EntityMapping [name=" + name + ", className=" + className + "]";
	}
}
