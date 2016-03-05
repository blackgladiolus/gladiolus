<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
	<form action="user.login" method="post">
		用户名： <input type="text" name="User.user_id"><br> 
		密码： <input type="password" name="User.user_password"><br> 
		<input type="submit" value="提交">
	</form>
</body>
</html>
