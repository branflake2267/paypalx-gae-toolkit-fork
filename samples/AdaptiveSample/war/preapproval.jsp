<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.paypal.adaptive.core.DateUtil"%>
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
<input type="hidden" name="action" value="preapproval"/>
<center>
<table class="api">
	<tr>
		<td colspan="6" class="header">Adaptive Payments - Preapproval</td>
	</tr>
	<tr>
		<td class="field">Sender's Email:</td>
		<td><input type="text" size="50" maxlength="32" name="senderEmail"
			value="ppalav_1260941775_per@yahoo.com" /></td>
	</tr>
   <tr>
		<td class="field">Starting date:</td>
		<td><input type="text" size="50" maxlength="32" name="startingDate"
			value="<%=DateUtil.getCurrentDate() %>" /></td>
	</tr>
	<tr>
		<td class="field">Ending date:</td>
		<td><input type="text" size="50" maxlength="32" name="endingDate"
			value="<%=DateUtil.getDateAfter(30) %>" /></td>
	</tr>
	<tr>
		<td class="field">Maximum Amount per Payment:</td>
		<td><input type="text" size="50" maxlength="32" name="maxAmountPerPayment"
			value="1.00" /></td>
	</tr>
	<tr>
		<td class="field">Maximum Number of Payments:</td>
		<td><input type="text" size="50" maxlength="32" name="maxNumberOfPayments"
			value="10" /></td>
	</tr>
	
	<tr>
		<td class="field">Maximum Number of Payments Per Period:</td>
		<td><input type="text" size="50" maxlength="32" name="maxNumberOfPaymentsPerPeriod"
			value="3" /></td>
	</tr>
	<tr>
		<td class="thinfield">Maximum Total Amount:</td>
		<td><input type="text" size="50" maxlength="32" name="maxTotalAmountOfAllPayments"
			value="50.00" /></td>
	</tr>
	<tr>
		<td class="thinfield" width="52">currencyCode</td>
		<td><select name="currencyCode">
			<option value="USD" selected>USD</option>
			<option value="GBP">GBP</option>
			<option value="EUR">EUR</option>
			<option value="JPY">JPY</option>
			<option value="CAD">CAD</option>
			<option value="AUD">AUD</option>
		</select></td>
	</tr>
	
	<tr>
		<td class="thinfield" width="52"></td>
		<td colspan="5"><input type="submit" value="Submit"></td>
	</tr>
</table>
</center>
</form>
</body>
</html>