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
		</script>
		<link href="<%=request.getContextPath()%>/style" rel="stylesheet" type="text/css"/>
	</head>
	
<body>
	<center>
		<img src = "<%=request.getContextPath()%>/logo" usemap = "#logomap">
		<map name = "logomap">
			<area shape = "rect" coords = "(0,0,366,268)" href = "<%=request.getContextPath()%>">
		</map> 
		<br><a href = "login">Login</a><hr>
		<h2>User Registration</h2>
		<form:form method = "POST" action = "reg">
		<table>
			<tr>
				<td><label id = "emailAddressLabel">Email Address:</label></td>
				<td><input name = "emailAddress" type = text/></td>
			</tr>
			<tr>
				<td><label id = "password">Password:</label></td>
				<td><input name = "password" type = "password"/></td>
			</tr>
			<tr>
				<td><label id = "firstNameLabel">First Name:</label></td>
				<td><input name = "firstname" type = text/></td>
			</tr>
			<tr>
				<td><label id = "lastNameLabel">Last Name:</label></td>
				<td><input name = "lastname" type = text/></td>
			</tr>
		</table>
		<a href="frequenciesinfo" onclick="window.open('frequenciesinfo',
				'popup','width=600,height=500,scrollbars=yes,resizable=yes,toolbar=no,directories=no,location=no,menubar=no,status=no,left=50,top=0'); return false">
				Learn about frequencies</a>
		<br><br>
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
				<td><input name = "category"/></td>
				<td><select name = "catfrequency">
					<option value = "Real-time" select="true">Real-time</option>
					<option value = "1 hour">1 hour</option>
					<option value = "1 day">1 day</option>
				</select></td>
			</tr>
		</table>
		<br>
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
				<td><input name = "application"/></td>
				<td><select name = "appfrequency">
					<option value = "Real-time" select="true">Real-time</option>
					<option value = "1 hour">1 hour</option>
					<option value = "1 day">1 day</option>
				</select></td>
			</tr>
		</table>
		<br>
		<input type = "submit" value = "Register"/>
		</form:form>
	</center>		
</body>
	
</html>