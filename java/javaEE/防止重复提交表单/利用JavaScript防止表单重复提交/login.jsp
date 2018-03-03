<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<title>Index</title>
</head>
<body>
	<h1>login.jsp</h1>
	<hr />
	<form action="<c:url value='/LoginServlet' />" method="post">
		<input type="text" name="username" /><br /> 
		<input type="password" name="password" /><br /> 
		<input type="submit"/>
	</form>
</body>
<script type="text/javascript">

    var form = document.querySelector("form");
    form.addEventListener("submit", function(event) {
    	var submitBtn = form.querySelector("input[type=submit]");
    	submitBtn.setAttribute("disabled", "disabled");
    });

</script>
</html>