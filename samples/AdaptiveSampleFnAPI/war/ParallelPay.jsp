<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="com.paypal.adaptive.core.CurrencyCodes" %>
    <%@ page import="com.paypal.adaptive.core.PaymentType" %>
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
<input type="hidden" name="action" value="parallelPay"/>
<center>
<table class="api">
	<tr>
		<td colspan="4" class="header">Parallel Pay</td>
	</tr>
		
	<tr>
		<td class="thinfield">Select number of Receivers:</td>
		<td colspan=3>
			<select name="numberOfReceivers">
				<option value="2" selected>2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
			</select>
		</td>
	</tr>	
	<tr>
		<td class="thinfield">Receiver Email:</td>
		<td><input type="text" size="50" name="receiveremail" value="pp+sel_1261964325_biz@yahoo.com" /></td>
		<td class="thinfield">Amount:</td>
		<td><input type="text" name="amount" value="1.0"></td>
	</tr>
	<tr>
		<td class="thinfield">Receiver Email:</td>
		<td><input type="text" size="50" name="receiveremail" value="ppalav_1260515409_biz@yahoo.com" /></td>
		<td class="thinfield">Amount:</td>
		<td><input type="text" name="amount" value="2.0"></td>
	</tr>
	
	<tr>
		<td class="thinfield" width="52">Currency Code</td>
		<td colspan=3><select name="currencyCode">
		<%
		 for(CurrencyCodes code: CurrencyCodes.values()){
			 
			 out.print("<option value=\"" + code.toString() + "\"");
			 if(code == CurrencyCodes.USD){
				 out.print(" selected");
			 }
			 out.print(">" + code.toString() + "</option>");
			 
		 }
		%>
		</select></td>
	</tr>
	<tr>
		<td class="thinfield" width="52">Payment Type</td>
		<td colspan=3><select name="paymentType">
		<%
		 for(PaymentType ptype: PaymentType.values()){
			 
			 out.print("<option value=\"" + ptype.toString() + "\"");
			 if(ptype == PaymentType.PERSONAL){
				 out.print(" selected");
			 }
			 out.print(">" + ptype.toString() + "</option>");
			 
		 }
		%>
		</select></td>
	</tr>
	<tr>
		<td class="thinfield">Memo:</td>
		<td colspan=3><input type="text" size="50" maxlength="32" name="memo"
			value="add your mrmo" /></td>
	</tr>
	<tr>
		<td class="thinfield">Sender's Email (Optional):</td>
		<td colspan=3><input type="text" size="50" maxlength="32" name="email"
			value="" /></td>
	</tr>
	<tr>
		<td class="thinfield" width="52"></td>
		<td colspan="3"><input type="submit" value="Submit"></td>
	</tr>
</table>
</center>
</form>
</body>
</html>