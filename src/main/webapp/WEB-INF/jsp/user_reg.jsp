<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>User Registration</title>
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
				var element1 = document.createElement("label");
				element1.innerHTML = rowcount;
				cell1.appendChild(element1);
				
				var cell2 = row.insertCell(2);
				var element2 = document.createElement("input");
				element2.className = name;
				element2.type = "text";
				element2.name = name;
				cell2.appendChild(element2);
				
				var cell3 = row.insertCell(3);
				var element3 = document.createElement("select");
				element3.name = frequency;
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
				element3.appendChild(realtime);
				element3.appendChild(hour);
				element3.appendChild(day);
				cell3.appendChild(element3);
				if(element2.className == "application") autocompleteApplication();
				else if(element2.className == "category") autocompleteCategory();
			}
			
			function deleteRow(tableID) {
				try {
					var table = document.getElementById(tableID);
					var rowCount = table.rows.length;
					
					for(var i = 0; i<rowCount; i++)
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
			
			function validateEmailAddress()
			{
				var emailAddress = document.getElementsByName("emailAddress")[0].value;
				var response = document.getElementById("emailAddressResponse");
				var exp = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
				if((emailAddress.length<4)||(exp.test(emailAddress) == false)){
					response.setAttribute("style","display:inline");
					response.setAttribute("class","error");
					response.innerHTML = "Invalid email address";
				}
				else response.setAttribute("style","display:none");
			}
			
			function validatePassword()
			{
				var password = document.getElementsByName("password")[0].value;
				var response = document.getElementById("passwordResponse");
				if(password.length<5){
					response.setAttribute("style","display:block; text-align:center");
					response.setAttribute("class","error");
					response.colspan = "2";
					response.innerHTML = "Passwords need to be more than 5 chararcters";
				}
				else response.setAttribute("style","display:none");
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
		<link rel="stylesheet" type="text/css" href="styles/jqueryAutocomplete" />
	    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
	    <script src="js/jqueryAutocomplete"></script>
		<link href="<%=request.getContextPath()%>/styles/notiflyStyle" rel="stylesheet" type="text/css"/>
	</head>
	
<body>
	<center>
		<img src = "<%=request.getContextPath()%>/images/notiflyTitle" usemap = "#logomap">
		<map name = "logomap">
			<area shape = "rect" coords = "(0,0,366,268)" href = "<%=request.getContextPath()%>">
		</map> 
		<br><a href = "login">Login</a><hr>
		<h2>User Registration</h2>
		<form:form method = "POST" action = "reg" id = "userform" commandName = "userForm">
		<table>
			<tr >
				<td><form:label path = "emailAddress" id = "emailAddressLabel">Email Address:</form:label></td>
				<td><form:input path = "emailAddress" name = "emailAddress" onchange = "validateEmailAddress()"/></td>
				<td><form:errors path = "emailAddress" cssClass = "error"/></td>
			</tr>
			<tr>
				<td></td><td id = "emailAddressResponse" style = "display:none; text-align:center"></td>
			</tr>
			<tr>
				<td><form:label path = "password" id = "password">Password:</form:label></td>
				<td><form:input path = "password" name = "password" type = "password" onchange = "validatePassword()"/></td>
				<td><form:errors path = "password" cssClass = "error"/></td>
			</tr>
			<tr>
				<td></td><td id = "passwordResponse" style = "display:none; width:99%;"></td>
			</tr>
			<tr>
				<td><form:label path = "firstName" id = "firstNameLabel">First Name:</form:label></td>
				<td><form:input path = "firstName" name = "firstname" type = "text"/></td>
				<td><form:errors path = "firstName" cssClass = "error"/></td>
			</tr>
			<tr>
				<td><form:label path = "lastName" id = "lastNameLabel">Last Name:</form:label></td>
				<td><form:input path = "lastName" name = "lastname" type = "text"/></td>
				<td><form:errors path = "lastName" cssClass = "error"/></td>
			</tr>
		</table>
		<a href="frequenciesinfo" onclick="window.open('frequenciesinfo',
				'popup','width=600,height=500,scrollbars=yes,resizable=yes,toolbar=no,directories=no,location=no,menubar=no,status=no,left=50,top=0'); return false">
				Learn about frequencies</a>
		<br><br>
		<table id = applications>
			<tr>
				<th>Applications Subscriptions</th>
				<th><input type = "button" value = "Add Application" onclick = "addRow('applications','application','appfrequency')"/></th>
				<th><input type = "button" value = "Delete Applications" onclick = "deleteRow('applications')"/></th>
				<th>Frequency</th>
			</tr>
			<tr>
				<td><center><input type = "checkbox" name = "appchck"/></center></td>
				<td><label id = "applicationlabel">1</label>
				<td><input class = "application" name = "application"/></td>
				<td><select name = "appfrequency">
					<option value = "Real-time" selected>Real-time</option>
					<option value = "1 hour">1 hour</option>
					<option value = "1 day">1 day</option>
				</select></td>
			</tr>
		</table>
		<br>
		<table id = categories>
			<tr>
				<th>Categories Subscriptions</th>
				<th><input type = "button" value = "Add Category" onclick = "addRow('categories','category','catfrequency')"/></th>
				<th><input type = "button" value = "Delete Categories" onclick = "deleteRow('categories')"/></th>
				<th>Frequency</th>
			</tr>
			<tr>
				<td><center><input type = "checkbox" name = "categorychk"/></center></td>
				<td><label id = "categorylabel">1</label>
				<td><input class = "category" name = "category" type = "text"/></td>			
				<td><select name = "catfrequency">
					<option value = "Real-time" selected>Real-time</option>
					<option value = "1 hour">1 hour</option>
					<option value = "1 day">1 day</option>
				</select></td>
			</tr>
		</table>
		<br>
		<input type = "submit" value = "Register"/>
		</form:form>
	</center>		
	<script> autocompleteApplication();</script>
	<script> autocompleteCategory();</script>
</body>
	
</html>