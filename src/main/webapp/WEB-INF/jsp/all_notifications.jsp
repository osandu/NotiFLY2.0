<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>NotiFLY Home</title>
	<link href="<%=request.getContextPath()%>/style" rel="stylesheet" type="text/css"/>
</head>
<body>
	<div id = "header">
		<img src = "<%=request.getContextPath()%>/logo" usemap = "#logomap">
		<map name = "logomap">
			<area shape = "rect" coords = "(0,0,366,268)" href = "<%=request.getContextPath()%>">
		</map> 
		<br><a href = "<%=request.getContextPath()%>/user">User Home</a>
		<a href = "<%=request.getContextPath()%>/logout">Logout</a><hr>
	</div>
	
	<div id = "container">
	<table id = "notifications">
		<tr>
			<th>APPLICATION</th>
			<th>MESSAGE</th>
			<th>UPLOADED</th>
			<th>CATEGORIES</th>
		</tr>		
		<c:forEach items="${notifications}" var="note" varStatus="vs">
			<tr>
				<td>${note.message}</td>
				<td><a href = '<%=request.getContextPath()%>/applications/${note.application}'>${note.application}</a></td>	
				<td>${note.updateDate}</td>
				<td>
					<c:forEach items="${note.categories}" var="category">
						<a href = '<%=request.getContextPath()%>/categories/${category}'>${category}</a>
					</c:forEach>
				</td>
			</tr>
		</c:forEach>
	</table>
	</div>
	
</body>
</html>