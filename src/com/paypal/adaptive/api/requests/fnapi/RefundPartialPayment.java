/**
 * 
 */
package com.paypal.adaptive.api.requests.fnapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.paypal.adaptive.api.requests.RefundRequest;
import com.paypal.adaptive.api.responses.RefundResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.CurrencyCodes;
import com.paypal.adaptive.core.Receiver;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.InvalidAPICredentialsException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingAPICredentialsException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.RequestAlreadyMadeException;
import com.paypal.adaptive.exceptions.RequestFailureException;

/**
 * Java class to send partial refund payment.
 *
 */
public class RefundPartialPayment {

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
     * Required CurrencyCode
     */
    protected CurrencyCodes currencyCode;
	/*
	 * Optional payKey (one of payKey or trackingId must be set)
	 */
	protected String payKey;
	/*
	 * Optional trackingId (one of payKey or trackingId must be set)
	 */
	protected String trackingId;
	/*
	 * Required Receivers to refund
	 */
	protected List<Receiver> receivers;
	
	// internal field
    protected boolean requestProcessed = false;
    
    public RefundPartialPayment() throws MissingParameterException{
    	
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
		if(currencyCode == null){
			// throw error
			throw new MissingParameterException("CurrencyCode");
		}
		if( (payKey == null || payKey.length() <= 0) 
				&& (trackingId == null || trackingId.length() <= 0)) {
    		throw new MissingParameterException("payKey or trackingId");
		}
		if(receivers == null || receivers.size() <=0 ){
			// throw error
			throw new MissingParameterException("Receivers");
		}
	}
    public RefundResponse makeRequest() throws MissingParameterException, 
    RequestAlreadyMadeException, MissingAPICredentialsException, 
    InvalidAPICredentialsException, PayPalErrorException, RequestFailureException, IOException, InvalidResponseDataException {
		
		validate();
		
		RefundRequest refundRequest = new RefundRequest(language, env);
		
		// payKey gets first preference
		if(payKey != null && payKey.length() > 0) 
			refundRequest.setPayKey(payKey);
		else if (trackingId != null && trackingId.length() > 0)
			refundRequest.setTrackingId(trackingId);
		
		refundRequest.setCurrencyCode(currencyCode);
		// set receivers
		refundRequest.setReceiverList(receivers);
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
	 * @return the trackingId
	 */
	public String getTrackingId() {
		return trackingId;
	}

	/**
	 * @param trackingId the trackingId to set
	 */
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	/**
	 * @return the receivers
	 */
	public List<Receiver> getReceivers() {
		return receivers;
	}

	/**
	 * @param receivers the receivers to set
	 */
	public void setReceivers(List<Receiver> receivers) {
		this.receivers = receivers;
	}
	/**
	 * @param receiver the receiver to set
	 */
	public void addToReceivers(Receiver receiver) {
		if(this.receivers == null){
			this.receivers = new ArrayList<Receiver>();
		}
		this.receivers.add(receiver);
	}

	/**
	 * @return the currencyCode
	 */
	public CurrencyCodes getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(CurrencyCodes currencyCode) {
		this.currencyCode = currencyCode;
	}
    
}
