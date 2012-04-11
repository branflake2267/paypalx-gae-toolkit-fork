/**
 * 
 */
package com.paypal.adaptive.exceptions;

import java.io.UnsupportedEncodingException;

import com.paypal.adaptive.core.EndPointUrl;
import com.paypal.adaptive.core.ExpType;
import com.paypal.adaptive.core.ServiceEnvironment;

/**
 * @author palavilli
 *
 */
public class AuthorizationRequiredException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1100431404605253884L;

	/**
	 * 
	 */
	protected String payKey;

	/**
	 * 
	 */
	protected String preapprovalKey;
	
	public AuthorizationRequiredException(){
		//...
	}

	/**
	 * @return the payKey
	 */
	public String getPayKey() {
		return payKey;
	}

	/**
	 * @param payKey the payKey to set
	 */
	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}
	
	
	/**
	 * @return the preapprovalKey
	 */
	public String getPreapprovalKey() {
		return preapprovalKey;
	}

	/**
	 * @param preapprovalKey the preapprovalKey to set
	 */
	public void setPreapprovalKey(String preapprovalKey) {
		this.preapprovalKey = preapprovalKey;
	}
	
	/**
	 * Generates the PayPal Authorization Url with appropriate parameters
	 * @param env
	 * @return Authorization Url
	 * @throws UnsupportedEncodingException
	 */
	public String getAuthorizationUrl(ServiceEnvironment env) 
	throws UnsupportedEncodingException{
		
		StringBuilder outStr = new StringBuilder();
		boolean isPreapproval = (preapprovalKey != null);
		outStr.append(EndPointUrl.getStdAuthorizationUrl(env, isPreapproval));
		if(outStr.indexOf("?") <= 0){
			outStr.append("?");
		} else {
			outStr.append("&");
		}
		
		if(payKey != null){
			outStr.append("paykey=");
			outStr.append(java.net.URLEncoder.encode(payKey, "UTF-8"));
			
		} else if(preapprovalKey != null){
			outStr.append("preapprovalkey=");
			outStr.append(java.net.URLEncoder.encode(preapprovalKey, "UTF-8"));
		}
		return outStr.toString();
	}
	
	public String getEmbeddedPaymentsAuthorizationUrl(ServiceEnvironment env, ExpType expType) 
	throws UnsupportedEncodingException{
		
		StringBuilder outStr = new StringBuilder();
		outStr.append(EndPointUrl.getEmbeddedAuthorizationUrl(env, expType));
		
		if(payKey != null){
			if(outStr.indexOf("?") <= 0){
				outStr.append("?paykey=");
			} else {
				outStr.append("&paykey=");
			}
			outStr.append(java.net.URLEncoder.encode(payKey, "UTF-8"));
		} 
		
		if(preapprovalKey != null){
			if(outStr.indexOf("?") <= 0){
				outStr.append("?preapprovalkey=");
			} else {
				outStr.append("&preapprovalkey=");
			}
			outStr.append(java.net.URLEncoder.encode(preapprovalKey, "UTF-8"));
		}
		return outStr.toString();
	}

	
}
