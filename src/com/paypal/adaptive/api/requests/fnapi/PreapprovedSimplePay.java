/**
 * 
 */
package com.paypal.adaptive.api.requests.fnapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import com.paypal.adaptive.api.requests.PayRequest;
import com.paypal.adaptive.api.responses.PayResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.AckCode;
import com.paypal.adaptive.core.ActionType;
import com.paypal.adaptive.core.ClientDetails;
import com.paypal.adaptive.core.CurrencyCodes;
import com.paypal.adaptive.core.PaymentDetails;
import com.paypal.adaptive.core.PaymentExecStatus;
import com.paypal.adaptive.core.Receiver;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.AuthorizationRequiredException;
import com.paypal.adaptive.exceptions.InvalidAPICredentialsException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingAPICredentialsException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.PaymentExecException;
import com.paypal.adaptive.exceptions.PaymentInCompleteException;
import com.paypal.adaptive.exceptions.RequestAlreadyMadeException;
import com.paypal.adaptive.exceptions.RequestFailureException;

/**
 * <p> Java class to make preapproval API call.
 *
 */
public class PreapprovedSimplePay {

	/*
	 * Required applicationName
	 */
	protected String applicationName;
	/*
	 * Required APICredential
	 */
	protected APICredential credentialObj;
	/*
	 * Required Receiver info
	 */
	protected Receiver receiver;
	/*
	 * Required Environment
	 */
	protected ServiceEnvironment env;
    /*
     * Required memo
     */
    protected String memo;
    /*
     * Required CurrencyCode
     */
    protected CurrencyCodes currencyCode;
    /*
     * Required language for localization
     */
    protected String language;
    /*
     * Required User IP Address
     */
    protected String userIp;
    /*
     * Required preapprovalKey
     */
    protected String preapprovalKey;
    /*
     * Optional ipnURL
     */
    protected String ipnURL;
    /*
	 * Required Sender Email
	 */
	protected String senderEmail;
    
    // internal field
    protected boolean requestProcessed = false;
    
    /*
     * Default constructor
     */
    public PreapprovedSimplePay(String preapprovalKey) throws MissingParameterException{
    	if(preapprovalKey == null || preapprovalKey.length() <=0 )
    		throw new MissingParameterException("preapprovalKey");
		this.preapprovalKey = preapprovalKey;
	}
    
	public PayResponse makeRequest()
	throws IOException, MalformedURLException, MissingAPICredentialsException,
	   		InvalidAPICredentialsException, MissingParameterException, 
	   		UnsupportedEncodingException, RequestFailureException,
	   		InvalidResponseDataException, PayPalErrorException,
	   		RequestAlreadyMadeException, PaymentExecException,
	   		AuthorizationRequiredException, PaymentInCompleteException {
		
		validate();
		
		PaymentDetails paymentDetails = new PaymentDetails(ActionType.PAY);
		PayRequest payRequest = new PayRequest(language, env);
		paymentDetails.addToReceiverList(receiver);
		if(ipnURL != null){
			paymentDetails.setIpnNotificationUrl(ipnURL);
		}
		paymentDetails.setCurrencyCode(currencyCode);
		// Preapproved payments do not require cancel and returnUrl as there is 
		// no authorization required - this is a bug in Pay API
		paymentDetails.setCancelUrl("http://www.paypal.com");
		paymentDetails.setReturnUrl("http://www.paypal.com");
		
		if(senderEmail != null && senderEmail.length() > 0){
			paymentDetails.setSenderEmail(senderEmail);
		}
		paymentDetails.setPreapprovalKey(preapprovalKey);
		// set clientDetails
		ClientDetails clientDetails = new ClientDetails();
		clientDetails.setIpAddress(userIp);
		clientDetails.setApplicationId(applicationName);
		payRequest.setClientDetails(clientDetails);
		
		// set payment details
		payRequest.setPaymentDetails(paymentDetails);
		PayResponse payResp = payRequest.execute(credentialObj);
		// if there is an API level error handle those first - look for responseEnvelope/ack
        if(payResp.getResponseEnvelope().getAck() == AckCode.Failure
                || payResp.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
                // throw error
                throw new PayPalErrorException(payResp.getResponseEnvelope(), payResp.getPayErrorList());
        }

        // if it's a payment execution error throw an exception
        if(payResp.getPaymentExecStatus() != null){
                if(payResp.getPaymentExecStatus() == PaymentExecStatus.ERROR ) {

                        PaymentExecException peex = new PaymentExecException(payResp.getPaymentExecStatus());
                        peex.setPayErrorList(payResp.getPayErrorList());
                        peex.setResponseEnvelope(payResp.getResponseEnvelope());
                        throw peex;

                } else if( payResp.getPaymentExecStatus() == PaymentExecStatus.INCOMPLETE
                                || payResp.getPaymentExecStatus() == PaymentExecStatus.REVERSALERROR ){
                        PaymentInCompleteException ex = new PaymentInCompleteException(payResp.getPaymentExecStatus());
                        ex.setPayErrorList(payResp.getPayErrorList());
                        ex.setPayKey(payResp.getPayKey());
                        ex.setResponseEnvelope(payResp.getResponseEnvelope());
                        throw ex;

                } else if(payResp.getPaymentExecStatus() == PaymentExecStatus.CREATED){
                        // this shouldn't occur for preapproved payments
                	PaymentExecException peex = new PaymentExecException(payResp.getPaymentExecStatus());
                    peex.setResponseEnvelope(payResp.getResponseEnvelope());
                    throw peex;  
                } else if(payResp.getPaymentExecStatus() == PaymentExecStatus.COMPLETED
                                || payResp.getPaymentExecStatus() == PaymentExecStatus.PROCESSING
                                || payResp.getPaymentExecStatus() == PaymentExecStatus.PENDING){
                        // no further action required so treat these as success
                } else {
                        // unknown paymentExecStatus - throw exception
                        PaymentExecException peex = new PaymentExecException(payResp.getPaymentExecStatus());
                        peex.setPayErrorList(payResp.getPayErrorList());
                        peex.setResponseEnvelope(payResp.getResponseEnvelope());
                        throw peex;
                }
        }
		return payResp;
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
		if(receiver == null){
			// throw error
			throw new MissingParameterException("Receiver");
		}
		if(currencyCode == null){
			// throw error
			throw new MissingParameterException("CurrencyCode");
		}
		if(env == null){
			// throw error
			throw new MissingParameterException("ServiceEnvironment");
		}
		if(memo == null){
			// throw error
			throw new MissingParameterException("memo");
		}
		if(userIp == null){
			// throw error
			throw new MissingParameterException("userIp");
		}
		if(applicationName == null){
			// throw applicationName
			throw new MissingParameterException("applicationName");
		}
		if(senderEmail == null){
			// throw error
			throw new MissingParameterException("senderEmail");
		}
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
	 * @return the receiver
	 */
	public Receiver getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
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
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
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
	 * @return the ipnURL
	 */
	public String getIpnURL() {
		return ipnURL;
	}

	/**
	 * @param ipnURL the ipnURL to set
	 */
	public void setIpnURL(String ipnURL) {
		this.ipnURL = ipnURL;
	}

	/**
	 * @return the senderEmail
	 */
	public String getSenderEmail() {
		return senderEmail;
	}

	/**
	 * @param senderEmail the senderEmail to set
	 */
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	/**
	 * @return the userIp
	 */
	public String getUserIp() {
		return userIp;
	}

	/**
	 * @param userIp the userIp to set
	 */
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
}
