<%@ page contentType="text/html" %>
<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE hmlt>
<html>
	<head>
		<meta http-equiv="Content-type" content="text/html;charset=UTF-8">
		<title>user1</title>
	</head>
	<body>
		<div id="userInfo">
			<h1>username:${sessionScope.user.username}</h1>
			<h1>password:${sessionScope.user.password}</h1>
			<h1>roleID:${sessionScope.user.roleID}</h1>
			<h1><a href="UserServlet?methodName=logout">注销</a></h1>
		</div>
	</body>
</html>