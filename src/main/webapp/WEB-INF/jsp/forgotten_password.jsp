<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
	<title>NotiFLY Password Reminder</title>
	<link href="<%=request.getContextPath()%>/styles/notiflyStyle" rel="stylesheet" type="text/css"/>
</head>

<body>
	<center>
	<img src = "<%=request.getContextPath()%>/images/notiflyTitle" usemap = "#logomap">
	<map name = "logomap">
		<area shape = "rect" coords = "(0,0,366,268)" href = "<%=request.getContextPath()%>">
	</map>
	<br><a href = "new_user">Register</a><hr>
	<h2>User Login Page</h2>
	<form:form method="POST" action="forgottenpasswordsend">
		<table>
			<tr>
				<td><label id="emailAddresslabel">Email Address</label></td>
				<td><input name="emailAddress" type = text/></td>
			</tr>
			<tr>
				<td><input type = 'submit' value = 'Get Password'/></td>
			</tr>
		</table>
	</form:form>
	</center>
</body>
</html>