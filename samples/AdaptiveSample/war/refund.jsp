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
<input type="hidden" name="action" value="refund"/>

<center>
<table class="api">
	<tr>
		<td colspan="6" class="header">Adaptive Payments - Refund</td>
	</tr>
	
	<tr>
		<td class="field">Pay Key:</td>
		<td><input type="text" size="50" maxlength="32" name="payKey"
			value="" /></td>
	</tr>

	<tr>
		<td class="field" width="52">currencyCode</td>
		<td><select name="currencyCode">
			<option value="USD" selected>USD</option>
			<option value="GBP">GBP</option>
			<option value="EUR">EUR</option>
			<option value="JPY">JPY</option>
			<option value="CAD">CAD</option>
			<option value="AUD">AUD</option>
		</select></td>
	</tr>
	<TR>
		<TD class="field" height="14" colSpan="3">
		<P align="center">Refund Details</P>
		</TD>
	</TR>
	<tr>
		<td width="52">Receivers</td>
		<td>ReceiverEmail (Required):</td>
		<td>Amount(Required):</td>

	</tr>
	<tr>
		<td width="52">
		<P align="right">1</P>
		</td>
		<td><input type="text" name="receiveremail" size="50"
			value="pd_1265515509_biz@yahoo.com"></td>
		<td><input type="text" name="amount" size="5" maxlength="7"
			value="0.10"></td>

	</tr>
	<tr>
		<td width="52">
		<P align="right">2</P>
		</td>		
		<td><input type="text" name="receiveremail" size="50"
			value="pp+sel_1261964325_biz@yahoo.com"></td>
		<td><input type="text" name="amount" 
			value="9.0"></td>	
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