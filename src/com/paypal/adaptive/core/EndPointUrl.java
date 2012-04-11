package com.paypal.adaptive.core;

/**
 * <p>Java class for EndPointUrl.
 */
public class EndPointUrl {

	public static String get(ServiceEnvironment env){
		if(env == ServiceEnvironment.PRODUCTION){
			return "https://svcs.paypal.com/AdaptivePayments/";			
		} else if(env == ServiceEnvironment.BETA_SANDBOX){
			return "https://svcs.beta-sandbox.paypal.com/AdaptivePayments/";		
		} else if(env == ServiceEnvironment.STAGING){
			//return "https://stage2vm5199.sc4.paypal.com:10279/AdaptivePayments/";
			return "https://stage2vm5258.sc4.paypal.com:10279/AdaptivePayments/";
		} else {
			return "https://svcs.sandbox.paypal.com/AdaptivePayments/";			
		} 
	}

	public static String getAuthorizationUrl(ServiceEnvironment env){
		return getStdAuthorizationUrl(env, false);
	}

	public static String getStdAuthorizationUrl(ServiceEnvironment env, boolean preaproval ){

		String addParams = null;
		if(preaproval) addParams = "cmd=_ap-preapproval";
		else addParams = "cmd=_ap-payment";

		if(env == ServiceEnvironment.PRODUCTION)
			return "https://www.paypal.com/cgi-bin/webscr?" + addParams;
		else if(env == ServiceEnvironment.BETA_SANDBOX)
			return "https://www.sandbox.paypal.com/cgi-bin/webscr?" + addParams;
		else if(env == ServiceEnvironment.STAGING) 
			return "https://www.stage2vm5199.qa.paypal.com/webscr?" + addParams;
		else //Sandbox is the default 
			return "https://www.sandbox.paypal.com/cgi-bin/webscr?" + addParams;		

	}

	public static String getEmbeddedAuthorizationUrl(ServiceEnvironment env, ExpType expType){

		StringBuilder url = new StringBuilder();
		
		if(env == ServiceEnvironment.PRODUCTION)
			url.append("https://www.paypal.com/webapps/adaptivepayment/flow/pay");
		else if(env == ServiceEnvironment.BETA_SANDBOX)
			url.append("https://www.sandbox.paypal.com/webapps/adaptivepayment/flow/pay");
		else if(env == ServiceEnvironment.STAGING) 
			url.append("https://stage2vm5258.sc4.paypal.com:8443/webapps/adaptivepayment/flow/pay");
		else //Sandbox is the default 
			url.append("https://www.sandbox.paypal.com/webapps/adaptivepayment/flow/pay");
		
		if(expType != null){
			url.append("?expType=");
			url.append(expType.toString());
		}
		
		return url.toString();
	}
}
