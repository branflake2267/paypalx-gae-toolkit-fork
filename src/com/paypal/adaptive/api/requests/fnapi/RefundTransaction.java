/**
 * 
 */
package com.paypal.adaptive.api.requests.fnapi;

import java.io.IOException;

import com.paypal.adaptive.api.requests.RefundRequest;
import com.paypal.adaptive.api.responses.RefundResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.InvalidAPICredentialsException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingAPICredentialsException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.RequestAlreadyMadeException;
import com.paypal.adaptive.exceptions.RequestFailureException;

/**
 * <p> Java class to send a Refund tranaaction. 
 *
 */
public class RefundTransaction {

	/*
	 * Required APICredential
	 */
	protected APICredential credentialObj;
	/*
     * Required language for localization
     */
    protected String language;
    /*
	 * Required Environment
	 */
	protected ServiceEnvironment env;
	/*
	 * Required payKey
	 */
	protected String transactionId;
	
	// internal field
    protected boolean requestProcessed = false;
    
    public RefundTransaction(String transactionId) throws MissingParameterException{
    	if(transactionId == null || transactionId.length() <= 0)
    		throw new MissingParameterException("transactionId");
    	this.transactionId = transactionId;
    }
    
    public void validate() 
	throws MissingParameterException,RequestAlreadyMadeException {
		
		if(requestProcessed){
			// throw error
			throw new RequestAlreadyMadeException();
		}
		if(language == null){
			// throw error
			throw new MissingParameterException("language");
		}
		if(env == null){
			// throw error
			throw new MissingParameterException("ServiceEnvironment");
		}
	}
    public RefundResponse makeRequest() throws MissingParameterException, 
    RequestAlreadyMadeException, MissingAPICredentialsException, 
    InvalidAPICredentialsException, PayPalErrorException, RequestFailureException, IOException, InvalidResponseDataException {
		
		validate();
		
		RefundRequest refundRequest = new RefundRequest(language, env);
		refundRequest.setTransactionId(transactionId);
		
		RefundResponse refundResp = refundRequest.execute(credentialObj);
		return refundResp;
	}

	/**
	 * @return the credentialObj
	 */
	public APICredential getCredentialObj() {
		return credentialObj;
	}

	/**
	 * @param credentialObj the credentialObj to set
	 */
	public void setCredentialObj(APICredential credentialObj) {
		this.credentialObj = credentialObj;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the env
	 */
	public ServiceEnvironment getEnv() {
		return env;
	}

	/**
	 * @param env the env to set
	 */
	public void setEnv(ServiceEnvironment env) {
		this.env = env;
	}
    
}
