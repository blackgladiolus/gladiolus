<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<html>
<head>
<title>Kaiser</title>
</head>

<body>
	用户编号：${requestScope.user.user_id}
	<br> 用户名：${requestScope.user.name}
	<br> 密码：${requestScope.user.password}
	<br>
</body>
</html>
