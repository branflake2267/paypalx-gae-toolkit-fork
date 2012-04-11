<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Order Page</title>
</head>
<body>
<center><h3> Please confirm the picture and click the Buy Now button to purchase a print through PayPal</h3></center>

<table border=1 cellspacing=0 cellpadding=5 width=50% align="center">
<tr align="center"><th>Title</th><th>Image</th></tr>
<tr align="center">
<td> <% out.print(request.getParameter("title")); %></td>
<td> <% out.print("<img src=\""+ request.getParameter("id") + "\">"); %></td>
</tr>
<tr align="center"><td></td><td><a href="/picmart?order=1&id=<%= request.getParameter("id") %>&title=<%= request.getParameter("title") %>"><img src="images/btn_buynowCC_LG.gif" /></a></td></tr>
</table>


</body>
</html>