<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>you've successfuly made the payment!</title>

	<script type="text/javascript" charset="utf-8">
		
	function gup( name ) {
	  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	  var regexS = "[\\?&]"+name+"=([^&#]*)";
	  var regex = new RegExp( regexS );
	  var results = regex.exec( window.location.href );
	  if( results == null )
	    return "";
	  else
	    return results[1];
	}
	function get_full_url(url_path) {
        var loc = window.location;
        var url = "" + loc.protocol + "//" + loc.host + url_path;
        return url;
    }
	
		function foo() {
			
			if (top && top.opener && top.opener.top) {
				top.opener.top.myDgFlow.dgSuccess(gup('payKey'));
			} else if (top.dgFlow) {
				top.myDgFlow.dgSuccess(gup('payKey'));
			} else {
				window.location.href = get_full_url("/adaptivesample?action=payDetails&payKey=" + gup('payKey'));
			}
			
		}
		
</script>
</head>
<body onLoad='setTimeout(foo,200);'>
<center><div style='background:#fff;width:75px;padding:10px;margin-top:120px;'><img src='/ldng.gif' style='color:#000;margin:0 0 0 0;'/><br/><font face='verdana' style='color:#000' size='1'>LOADING</font></div></center>
</body>
</html>