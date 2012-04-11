<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Adaptive Payments Test Tool</title>
</head>
<body>
<jsp:include page="apinavigation.jsp"></jsp:include>
<br/>

<form method="POST">
<input type="hidden" name="action" value="getFundingPlans"/>
<center>
<table class="api">
	<tr>
		<td colspan="6" class="header">Adaptive Payments - GetFundingPlans</td>
	</tr>
	
	<tr>
		<td class="thinfield">Pay Key:</td>
		<td><input type="text" size="50" maxlength="32" name="payKey" value="" /></td>
	</tr>

	<tr>
		<td class="field" width="52"></td>
		<td colspan="5"><input type="submit" value="Submit"></td>
	</tr>
</table>
</center>
</form>
</body>
</html>