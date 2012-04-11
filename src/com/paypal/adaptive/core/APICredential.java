/**
 * 
 */
package com.paypal.adaptive.core;

/**
 * Java class for APICredentials
 * @author palavilli
 *
 */
public class APICredential {

	protected String APIUsername;
	protected String APIPassword;
	protected String Signature;
	protected String AppId;
	/**
	 * PayPal accountEmail used as the SenderEmail for Implicit Payments (when API Caller is same as Sender)
	 */
	protected String accountEmail;
	
	/**
	 * @return the aPIUsername
	 */
	public String getAPIUsername() {
		return APIUsername;
	}
	/**
	 * @param aPIUsername the aPIUsername to set
	 */
	public void setAPIUsername(String aPIUsername) {
		APIUsername = aPIUsername;
	}
	/**
	 * @return the aPIPassword
	 */
	public String getAPIPassword() {
		return APIPassword;
	}
	/**
	 * @param aPIPassword the aPIPassword to set
	 */
	public void setAPIPassword(String aPIPassword) {
		APIPassword = aPIPassword;
	}
	/**
	 * @return the signature
	 */
	public String getSignature() {
		return Signature;
	}
	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		Signature = signature;
	}
	/**
	 * @return the appId
	 */
	public String getAppId() {
		return AppId;
	}
	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		AppId = appId;
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
