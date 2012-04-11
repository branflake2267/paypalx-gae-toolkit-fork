<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<% 
	String payKey = (String)request.getAttribute("payKey");
	String stdUrl = (String)request.getAttribute("stdUrl");
	String epUrl = (String)request.getAttribute("epUrl");
	String payReqStr = (String)request.getAttribute("payReqStr");
	String payRespStr = (String)request.getAttribute("payRespStr");
	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Adaptive Payments Test Tool</title>
<script type="text/javascript" src ="https://www.paypalobjects.com/js/external/dg.js"></script>
</head>
<body>
<h2>Pay API Response - Authorization required</h2>
<table border=1>
<tr>
<td>
<form method="GET" action="<%= stdUrl %>">
<input type="hidden" name="cmd" value="_ap-payment"/>
<table class="api">
<tr>
		<td colspan="6" class="header">Standard Experience</td>
	</tr>
	<tr>
		<td class="thinfield">Pay Key:</td>
		<td><input type="text" size="50" maxlength="32" name="paykey"
			value="<%= payKey %>" /></td>
	</tr>
	<tr>
		<td class="thinfield" width="52"></td>
		<td><input id="authz_button" type="submit" value="Launch Standard Payment Flow"></td>
	</tr>
</table>
</form>
<form method="GET" action="<%= epUrl %>">
<table class="api">
	<tr>
		<td colspan="6" class="header">Embedded Experience</td>
	</tr>
	<tr>
		<td class="thinfield">Pay Key:</td>
		<td><input type="text" size="50" maxlength="32" name="paykey"
			value="<%= payKey %>" /></td>
	</tr>
	<tr>
		<td class="thinfield">Preapproval Key (Optional):</td>
		<td><input type="text" size="50" maxlength="32" name="preapprovalkey"
			value="" /></td>
	</tr>
	<tr>
		<td class="thinfield">Experience Type:</td>
		<td><select name="expType">
			<option value="">select</option>
			<option value="light">light</option>
			<option value="mini">mini</option>
		</select>
		</td>
	</tr>
	<tr>
		<td class="thinfield" width="52"></td>
		<td><input id="ep_authz_button" type="submit" value="Launch Embedded Payment Flow"></td>
	</tr>
</table>
</form>
</td>
<td>
<%= payReqStr %>
<br/>
<%= payRespStr %>
</td>
</tr>
</table>
<script type="text/javascript" charset="utf-8">
    var dgFlow = new PAYPAL.apps.DGFlow({trigger: 'ep_authz_button'});
    function get_full_url(url_path) {
        var loc = window.location;
        var url = "" + loc.protocol + "//" + loc.host + url_path;
        return url;
    }
    
	function MyDGFlow(dgFlow) {
		    this.mainObj = dgFlow;
		    this.dgSuccess = function (payKey) {
		    	this.mainObj.closeFlow();
		    	window.location.href = get_full_url("/adaptivesample?action=payDetails&payKey=" + payKey);
		    };
		    this.dgCancel = function () {
		    	this.mainObj.closeFlow();
		    	alert("You've canceled your payment!");
		    };
	    
	}
 	var myDgFlow = new MyDGFlow(dgFlow);
</script>
</body>
</html>