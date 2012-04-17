/**
 * 
 */
package com.paypal.adaptive.core;

/**
 * Java class for APICredentials
 * @author palavilli
 */
public class APICredential {

	private String apiUsername;
	private String apiPassword;
	private String signature;
	private String appId;
	/**
	 * PayPal accountEmail used as the SenderEmail for Implicit Payments (when API Caller is same as Sender)
	 */
	private String accountEmail;
	
	/**
	 * @return the aPIUsername
	 */
	public String getAPIUsername() {
		return apiUsername;
	}
	/**
	 * @param apiUsername the aPIUsername to set
	 */
	public void setAPIUsername(String apiUsername) {
		this.apiUsername = apiUsername;
	}
	/**
	 * @return the aPIPassword
	 */
	public String getAPIPassword() {
		return apiPassword;
	}
	/**
	 * @param apiPassword the aPIPassword to set
	 */
	public void setAPIPassword(String apiPassword) {
	    this.apiPassword = apiPassword;
	}
	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}
	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
	    this.signature = signature;
	}
	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}
	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
	    this.appId = appId;
	}	
	/**
	 * @return the accountEmail
	 */
	public String getAccountEmail() {
		return accountEmail;
	}
	/**
	 * @param accountEmail the accountEmail to set
	 */
	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}
}
