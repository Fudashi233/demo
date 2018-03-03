<%@ page contentType="text/html" %>
<%@ page pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
		<title>Login</title>
	</head>
	<body>
		<div id="loginForm">
			<form action="${pageContext.request.contextPath}/UserServlet?methodName=login" method="POST">
				<input type="text" name="username" value="" /><br />
				<input type="password" name="password" value="" /><br />
				<input type="radio" name="roleID" value="1" required/>普通用户<br />
				<input type="radio" name="roleID" value="2" required/>普通管理员<br />
				<input type="radio" name="roleID" value="3" required/>超级管理员<br />
				<input type="submit" />
			</form>
		</div>
	</body>
</html>