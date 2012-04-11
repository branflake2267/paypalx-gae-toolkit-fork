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
<input type="hidden" name="action" value="convertCurrency"/>
<center>
<table class="api">
	<tr>
		<td colspan="6" class="header">Adaptive Payments - ConvertCurrency</td>
	</tr>
	<tr>
                <td width="52">ConversionDetails</td>
                <td>Amount(Required):</td>
                <td>FromCurrencyCode(Required):</td>
        </tr>
        <tr>
                <td width="52">
                <P align="right">1</P>
                </td>
                <td><input type="text" name="baseamount"
                        value="1.00"></td>
                <td><input type="text" name="fromcode"
                        value="GBP"></td>

        </tr>
        <tr>
                <td width="52">
                <P align="right">2</P>
                </td>
                <td><input type="text" name="baseamount"
                        value="100.00"></td>
                <td><input type="text" name="fromcode"
                        value="EUR"></td>
            </tr>
        <tr>
                <td width="52">convertToCurrencyList</td>
                <td>ToCurrencyCode(Required):</td>
        </tr>
        <tr>
        <td width="52">
                <P align="right">1</P>
                </td>
                <td><input type="text" name="tocode"
                        value="USD"></td>
        </tr>
	        <tr>
           <td width="52">
                <P align="right">2</P>
                </td>
                <td><input type="text" name="tocode"
                        value="CAD"></td>
        </tr>
        <tr>
           <td width="52">
                <P align="right">3</P>
                </td>
                <td><input type="text" name="tocode"
                        value="JPY"></td>
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