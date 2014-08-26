<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>User Account</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/jqueryAutocomplete" />
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/jqueryAutocomplete"></script>
	<link href="<%=request.getContextPath()%>/styles/notiflyStyle" rel="stylesheet" type="text/css"/>
	<script language = "javascript">
			function addRow(tableID,name,frequency) {
				var table = document.getElementById(tableID);
				var rowcount = table.rows.length;
				var row = table.insertRow(rowcount);
				
				var cell0 = row.insertCell(0);
				var element0 = document.createElement("input");
				element0.type = "checkbox";
				var centr = document.createElement("center");
				centr.appendChild(element0);
				cell0.appendChild(centr);
				
				var cell1 = row.insertCell(1);
				var element1 = document.createElement("input");
				element1.className = name;
				element1.type = "text";
				element1.name = name;
				cell1.appendChild(element1);
				
				var cell2 = row.insertCell(2);
				var element2 = document.createElement("select");
				element2.name = frequency;
				var realtime = document.createElement("option");
				realtime.value = "Real-time";
				realtime.select = true;
				realtime.innerHTML = "Real-time";
				var hour = document.createElement("option");
				hour.value = "1 hour";
				hour.innerHTML = "1 hour";
				var day = document.createElement("option");
				day.value = "1 day";
				day.innerHTML = "1 day";
				element2.appendChild(realtime);
				element2.appendChild(hour);
				element2.appendChild(day);
				cell2.appendChild(element2);
				if(element1.className == "application") autocompleteApplication();
				else if(element1.className == "category") autocompleteCategory();
			}
			
			function deleteRow(tableID) {
				try {
					var table = document.getElementById(tableID);
					var rowCount = table.rows.length;
					
					for(var i = 1; i<rowCount; i++)
					{
						var row = table.rows[i];
						var chckbox =  row.cells[0].childNodes[0].childNodes[0];
						if(null != chckbox && true == chckbox.checked) {
							table.deleteRow(i);
							rowCount--;
							i--;
						}
					}
				}
				catch (e) {
					alert(e);
				}
			}
			
			function autocompleteCategory()
			{
				$(".category").autocomplete("${pageContext.request.contextPath}/getcategories");				
			}
			
			function autocompleteApplication()
			{
				$(".application").autocomplete("${pageContext.request.contextPath}/getapplications");
			}			
		</script>
</head>
<body>
	<center>
	<img src = "<%=request.getContextPath()%>/images/notiflyTitle" usemap = "#logomap">
	<map name = "logomap">
		<area shape = "rect" coords = "(0,0,366,268)" href = "<%=request.getContextPath()%>">
	</map> 
	<br><a href = "<%=request.getContextPath()%>/user">User Home</a>
	<a href = "<%=request.getContextPath()%>/logout">Logout</a><hr>
	<h2><strong>User Account Details</strong></h2>
	<h3>Personal Details</h3>
	<form:form modelAttribute = "user" method = "POST" action = "update">
		<table id = "personaldetails">
			<tr>
				<td><label id = "emailAddressLabel">Email Address:</label></td>
				<td>${user.emailAddress}</td>
			</tr>
			<tr>
				<td><label id = "firstNameLabel">First Name:</label></td>
				<td><input name = "firstname" type = text value = "${user.firstName}"/></td>
			</tr>
			<tr>
				<td><label id = "lastNameLabel">Last Name:</label></td>
				<td><input name = "lastname" type = text value = "${user.lastName}"/></td>
			</tr>
		</table>
		<input type = "submit" value = "Save" id="saveUserDet"/>
		<br><a href="'<%=request.getContextPath()%>/frequenciesinfo'" onclick="window.open('<%=request.getContextPath()%>/frequenciesinfo',
				'popup','width=600,height=500,scrollbars=yes,resizable=yes,toolbar=no,directories=no,location=no,menubar=no,status=no,left=50,top=0'); return false">
				Learn about frequencies</a><br><br>
		</form:form>
		<h3>Applications Subscriptions </h3>
		<input type = "button" value = "Add Application" onclick = "addRow('applications','application','appfrequency')"/>
		<input type = "button" value = "Delete Applications" onclick = "deleteRow('applications')"/>
		<form:form method = "POST" action = "applications/update" id = "user">
		<table id = "applications">
			<tr>
				<th></th>
				<th>Application</th>
				<th>Frequency</th>
			</tr>
			<c:forEach items = "${applications}" var = "a">
				<tr>
					<td><center><input type = "checkbox" name = "appchck"/></center></td>
					<td><input class = "application" name = "application" value = "${a.key}"/></td>
					<td><select name = "appfrequency">
						<option value = "${a.value}">${a.value}</option>
						<option value = "Real-time" >Real-time</option>
						<option value = "1 hour">1 hour</option>
						<option value = "1 day">1 day</option>
					</select></td>
				</tr>
			</c:forEach>
		</table>
		<input type = "submit" value = "Save" id="saveApps"/>
		</form:form>
		<h3>Categories Subscriptions </h3>
		<input type = "button" value = "Add Category" onclick = "addRow('categories','category','catfrequency')"/>
		<input type = "button" value = "Delete Categories" onclick = "deleteRow('categories')"/>
		<form:form id = "user" method = "POST" action = "categories/update">
		<table id = "categories">
			<tr>
				<th></th>
				<th>Category</th>
				<th>Frequency</th>
			</tr>
			<c:forEach items = "${categories}" var = "c">
				<tr>
					<td><center><input type = "checkbox" name = "categorychk"/></center></td>
					<td><input class = "category" name = "category" value = "${c.key}"/></td>
					<td><select name = "catfrequency">
						<option value = "${c.value}">${c.value}</option>
						<option value = "Real-time">Real-time</option>
						<option value = "1 hour">1 hour</option>
						<option value = "1 day">1 day</option>
					</select></td>
				</tr>
			</c:forEach>
		</table>
		<input type = "submit" value = "Save" id="saveCats"/>
		</form:form>
	</center>
	<script> 
		autocompleteApplication();
		autocompleteCategory();
	</script>
</body>
</html>