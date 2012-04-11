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
<input type="hidden" name="action" value="pay"/>
<center>
<table class="api">
<tr>
		<td colspan="6" class="header">Adaptive Payments - Pay</td>
	</tr>
	<tr>
		<td class="thinfield">ActionType:</td>
		<td>
			<select name="actionType">
				<option value="PAY">PAY</option>
				<option value="CREATE">CREATE</option>
				<option value="PAY_PRIMARY">PAY_PRIMARY</option>
			</select>
		</td>
	</tr>
	<tr>
		<td class="thinfield">Sender's Email:</td>
		<td><input type="text" size="50" maxlength="32" name="email"
			value="" /></td>
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
		<td class="thinfield">Preapproval Key:</td>
		<td><input type="text" size="50" maxlength="32" name="preapprovalKey"
			value="" /></td>
	</tr>
	<tr>
		<td class="thinfield">Memo:</td>
		<td><input type="text" size="50" maxlength="32" name="memo"
			value="" /></td>
	</tr>
	<TR>
		<TD class="thinfield" height="14" colSpan="3">
		<P align="center">Pay Details</P>
		</TD>
	</TR>
	<tr>
		<td width="52">Payee</td>
		<td>ReceiverEmail (Required):</td>
		<td>Amount(Required):</td>
		<td>Primay Receiver(optional):</td>

	</tr>
	<tr>
		<td width="52">
		<P align="right">1</P>
		</td>
		<td><input type="text" name="receiveremail" size="50"
			value="ppalavilli@yahoo.com"></td>
		<td><input type="text" name="amount" 
			value="1.0"></td>
		<td><input type="text" name="primary"  
			value="false"></td>	
	</tr>		
	<tr>
		<td width="52">
		<P align="right">2</P>
		</td>		
		<td><input type="text" name="receiveremail" size="50"
			value=""></td>
		<td><input type="text" name="amount" 
			value=""></td>
		<td><input type="text" name="primary"  
			value=""></td>		
	</tr>
	<tr>
		<td class="thinfield" width="52">feesPayer</td>
		<td><select name="feesPayer">
			<option value="SENDER" selected>SENDER</option>
			<option value="PRIMARYRECEIVER">PRIMARYRECEIVER</option>
			<option value="EACHRECEIVER">EACHRECEIVER</option>
			<option value="SECONDARYONLY">SECONDARYONLY</option>
		</select></td>
	</tr>
	
	<tr>
		<td class="thinfield" width="52"></td>
		<td><input id="std_pay_button" type="submit" value="Submit"></td>
	</tr>
</table>
</center>
</form>
</body>
</html>