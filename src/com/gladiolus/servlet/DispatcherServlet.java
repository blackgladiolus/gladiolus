package com.gladiolus.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gladiolus.action.ActionSupport;
import com.gladiolus.action.help.ActionMapping;
import com.gladiolus.action.help.ActionXmlUtil;
import com.gladiolus.util.ClassUtil;

/**
 * 解析action.xml进行用户请求转发
 * 
 * @author xieguoping
 * 
 */
@SuppressWarnings("serial")
public class DispatcherServlet extends HttpServlet {
	// action.xml配置文件路径
	private final String actionXmlPath = ClassUtil
			.getClassPath(this.getClass()) + "gladiolus-action.xml";
	// 解析action.xml的方法
	private static ActionXmlUtil actionXmlUtil = new ActionXmlUtil();

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 得到表单action的值
		String action = request.getServletPath().replaceAll("/", "");
		// 获得action对应的映射对象
		ActionMapping actionMapping = actionXmlUtil.getActionMapping(action);
		int index = action.lastIndexOf(".");// 获取操作类型的位置
		if (index == -1) {
			throw new RuntimeException(
					" 该action访问路径未定义操作类型; form表单中的action定义格式为: ‘action名’+‘.’+‘操作方法名’ 例如：userAction.login ");
		}
		// 得到视图层传入的操作类型（login、query、delete……）
		String operate = action.substring(index + 1, action.length());
		// 将操作类型设置到action映射对象中
		actionMapping.setOperate(operate);
		try {
			ActionSupport ac = (ActionSupport) Class.forName(
					actionMapping.getActionClass()).newInstance();// 得到映射对象中映射的action的完全类路径,并反射调用
			ac.setActionMapping(actionMapping);
			ac.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() throws ServletException {
		// dispatcherServlet初始化时解析action配置文件，获得所有的action配置映射集合
		actionXmlUtil.parsexml(actionXmlPath);
	}

}
