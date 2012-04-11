<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.paypal.adaptive.core.PaymentPeriodType" %>
<%@ page import="com.paypal.adaptive.core.CurrencyCodes" %>
<%@ page import="com.paypal.adaptive.core.DateUtil"%>
<%@ page import="com.paypal.adaptive.core.DayOfWeek"%>
<%@ page import="com.paypal.adaptive.core.PinType"%>

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
<input type="hidden" name="action" value="createPreapprovalForPeriodicPayments"/>
<center>
<table class="api">
	<tr>
		<td colspan="6" class="header">Periodic Preapproval</td>
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
		<td class="thinfield">Maximum Total Amount:</td>
		<td><input type="text" size="50" maxlength="32" name="maxTotalAmountOfAllPayments"
			value="50.00" /></td>
	</tr>
	<tr>
		<td class="thinfield">Maximum Amount per Payments:</td>
		<td><input type="text" size="50" maxlength="32" name="maxAmountPerPayment"
			value="2.0" /></td>
	</tr>
	<tr>
		<td class="thinfield">Payment Period:</td>
		<td colspan=3><select name="paymentPeriod">
		<%
		 for(PaymentPeriodType code: PaymentPeriodType.values()){
			 
			 out.print("<option value=\"" + code.toString() + "\"");
			 out.print(">" + code.toString() + "</option>");
			 
		 }
		%>
		</select></td>
	</tr>
	<tr>
		<td class="thinfield">Payment Day of week if WEEKELY is selected</td>
		<td colspan=3><select name="dayOfWeek">
		<%
		 for(DayOfWeek code: DayOfWeek.values()){
			 
			 out.print("<option value=\"" + code.toString() + "\"");
			 out.print(">" + code.toString() + "</option>");
			 
		 }
		%>
		</select></td>
	</tr>
	<tr>
		<td class="thinfield">Date of Month if MONTHLY is selection - 0-31 where 0 means no preference::</td>
		<td><input type="text" size="50" maxlength="32" name="dateOfMonth"
			value="" /></td>
	</tr>
	<tr>
		<td class="thinfield">Maximum number of payments per period:</td>
		<td><input type="text" size="50" maxlength="32" name="maxNumberOfPaymentsPerPeriod" value="" /></td>
	</tr>
	
	<tr>
		<td class="thinfield" width="52">currencyCode</td>
		<td><select name="currencyCode">
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
		<td class="thinfield">Pin Type (Optional):</td>
		<td colspan=3><select name="pinType">
		<%
		 for(PinType code: PinType.values()){
			 
			 out.print("<option value=\"" + code.toString() + "\"");
			 out.print(">" + code.toString() + "</option>");
			 
		 }
		%>
		</select></td>
	</tr>
	<tr>
		<td class="thinfield">Memo:</td>
		<td><input type="text" size="50" maxlength="32" name="memo"
			value="add your memo" /></td>
	</tr>
	<tr>
		<td class="field">Sender's Email (Optional):</td>
		<td><input type="text" size="50" maxlength="32" name="senderEmail"
			value="ppalav_1260941775_per@yahoo.com" /></td>
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