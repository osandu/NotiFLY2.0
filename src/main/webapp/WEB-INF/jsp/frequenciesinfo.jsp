<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Frequencies</title>
	<link href="<%=request.getContextPath()%>/styles/notiflyStyle" rel="stylesheet" type="text/css"/>
</head>
<body>
	<center>
		<img src = "<%=request.getContextPath()%>/images/notiflyTitle" usemap = "#logomap">
		<map name = "logomap">
			<area shape = "rect" coords = "(0,0,366,268)" href = "<%=request.getContextPath()%>">
		</map> 
		</center>
		<h2>Frequencies</h2>
		A frequency is an attribute that the user sets for each application and category that it subscribes to. The default value is a Real-time.
		<h3>Types</h3>
		<p>
		NotiFLY offers its users three different kinds of frequencies:</p>
		<ul><li><strong>Real-time: </strong>When a relevant notification is uploaded, the user is sent an email immediately</li>
		<li><strong>1 hour: </strong>A batch of emails (if any) are sent at the end of each hour 
		for any notifications relevant to the user that were uploaded in the hour</li>
		<li><strong>1 day: </strong>A batch of emails (if any) are sent at midnight 
		for any notifications relevant to the user that were uploaded day</li></ul>
		<br>
		<h3>Priorities</h3>
		<p>Users must note that the frequency that they set for each application or category does not mean that every notification 
		uploaded will be sent at this frequency. This is because if the notification is being uploaded by an application or to a category 
		that has a frequency with a higher priority, the notification is sent at this frequency.</p>
		<ul><li><strong>Priority 1:</strong> Real-time</li>
		<li><strong>Priority 2:</strong> 1 hour</li>
		<li><strong>Priority 3:</strong> 1 day</li></ul>
</body>
</html>