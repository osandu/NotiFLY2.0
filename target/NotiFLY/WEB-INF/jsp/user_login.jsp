<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
	<title>User Login</title>
	<link href="<%=request.getContextPath()%>/style" rel="stylesheet" type="text/css"/>
</head>

<body>
	<center>
	<img src = "<%=request.getContextPath()%>/logo" usemap = "#logomap">
	<map name = "logomap">
		<area shape = "rect" coords = "(0,0,366,268)" href = "<%=request.getContextPath()%>">
	</map>
	<br><a href = "new_user">Register</a><hr>
	<h2>User Login Page</h2>
	<form:form method="POST" action="user">
	<table>
		<tr>
			<td><label id="emailAddresslabel">Email Address</label></td>
			<td><input name="emailAddress" type = text/></td>
		</tr>
		<tr>
			<td><label id="passwordlabel">Password</label></td>
			<td><input name="password" type = "password"/></td>
		</tr>
		    <tr>
        <td colspan="2">
            <center><button type="submit">Login</button></center>
        </td>
    </tr>
	</table>
	</form:form>
	</center>
</body>
</html>
	