package com.gladiolus.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gladiolus.action.help.ActionMapping;

/**
 * BaseServlet用来作为其它Servlet的父类
 * 
 * @author qdmmy6
 * 
 *         一个类多个请求处理方法，每个请求处理方法的原型与service相同！ 原型 = 返回值类型 + 方法名称 + 参数列表
 */
@SuppressWarnings("serial")
public abstract class ActionSupport extends HttpServlet {

	private ActionMapping actionMapping;

	public void setActionMapping(ActionMapping actionMapping) {
		this.actionMapping = actionMapping;
	}


	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1. 获取operate参数，它是用户想调用的方法
		String methodName = actionMapping.getOperate();
		try {
			Method method = this.getClass().getMethod(methodName,
					HttpServletRequest.class, HttpServletResponse.class);
			String result = (String) method.invoke(this, request, response);
			this.forward(result, request, response);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("您要调用的方法：" + methodName + "它不存在!", e);
		} catch (Exception e) {
			throw new RuntimeException("您要调用的方法：" + methodName
					+ "时出现错误，请检查配置文件是否正确!", e);
		}
	}

	/**
	 * 处理结果转发重定向
	 * 
	 * @param result
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void forward(String result, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 如果请求处理方法返回不为空
		if (this.stringIsNotEmpty(result)) {
			int index = result.indexOf(":");// 获取第一个冒号的位置
			String mappingPath = null;// 映射的路径
			if (index == -1) {// 如果没有冒号，使用转发
				mappingPath = actionMapping.getResult().get(result);
				if (this.stringIsNotEmpty(mappingPath)) {
					request.getRequestDispatcher(mappingPath).forward(request,
							response);
				} else {
					throw new RuntimeException("该方法返回结果" + result
							+ "在action.xml中未配置正确的路径！");
				}
			} else {// 如果存在冒号
				String start = result.substring(0, index);// 分割出前缀
				String path = result.substring(index + 1);// 分割出路径
				mappingPath = actionMapping.getResult().get(path);
				if (this.stringIsNotEmpty(mappingPath)) {
					if (start.equalsIgnoreCase("f")) {// 前缀为f表示转发
						request.getRequestDispatcher(mappingPath).forward(
								request, response);
					} else if (start.equalsIgnoreCase("r")) {// 前缀为r表示重定向
						response.sendRedirect(request.getContextPath()
								+ mappingPath);
					}
				} else {
					throw new RuntimeException("该方法返回结果" + path
							+ "在action.xml中未配置正确的路径！");
				}
			}
		}
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	private boolean stringIsNotEmpty(String str) {
		return str != null && !str.trim().isEmpty();
	}
}
