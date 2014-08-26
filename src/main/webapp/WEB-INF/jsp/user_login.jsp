<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
	<title>User Login</title>
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
	<form:form method="POST" action="user">
	<table>
		<tr>
			<td><label id="emailAddresslabel">Email Address:</label></td>
			<td><input name="emailAddress" type = text/></td>
		</tr>
		<tr>
			<td><label id="passwordlabel">Password:</label></td>
			<td><input name="password" type = "password"/></td>
		</tr>
		<tr>
	        <td colspan="2">
	            <center><input type = "submit" value = "Login"/></center>
	        </td>
        </tr>
        <tr>
	        <td colspan="2">
	        	<center><a href = "<%=request.getContextPath()%>/forgottenpassword">Forgotten Password</a></center>
	        </td>
        </tr>
	</table>
	</form:form>
	</center>
</body>
</html>
	