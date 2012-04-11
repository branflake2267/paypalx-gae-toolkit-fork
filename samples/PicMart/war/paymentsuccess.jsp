<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>you've successfuly made the payment!</title>

	<script type="text/javascript" charset="utf-8">
		
		function foo() {
			
			if (top && top.opener && top.opener.top) {
				top.opener.top.myDgFlow.dgSuccess();
				window.close();
			} else if (top.dgFlow) {
				top.myDgFlow.dgSuccess();
			} else {
				//alert("oops-a-daisy");
				window.location.href = "/picmart?status=success";
			}
			
		}
		
</script>
</head>
<body onLoad='foo();'>
<!-- 
<body onLoad='setTimeout(foo,200);'>
<center><div style='background:#fff;width:75px;padding:10px;margin-top:120px;'><img src='/images/ldng.gif' style='color:#000;margin:0 0 0 0;'/><br/><font face='verdana' style='color:#000' size='1'>LOADING</font></div></center>
-->
</body>
</html>