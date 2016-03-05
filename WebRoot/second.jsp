<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Kaiser</title>
</head>

<body>
这是跳转后的页面
	<form action="" name="" method="post">
		用户名： <input type="text" name="User.user_id" value="${requestScope.user.user_id}"><br>
		 密码： <input type="text" name="user.user_password" value="${requestScope.user.user_password}"><br>
		 生日： <input type="text" name="User.user_birthday" value="${requestScope.user.user_birthday}"><br> 
	</form>
</body>
</html>
