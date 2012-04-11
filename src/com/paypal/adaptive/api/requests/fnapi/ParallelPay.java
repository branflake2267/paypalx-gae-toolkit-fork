/**
 * 
 */
package com.paypal.adaptive.api.requests.fnapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

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
import com.paypal.adaptive.exceptions.NotEnoughReceivers;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.PaymentExecException;
import com.paypal.adaptive.exceptions.PaymentInCompleteException;
import com.paypal.adaptive.exceptions.ReceiversCountMismatchException;
import com.paypal.adaptive.exceptions.RequestAlreadyMadeException;
import com.paypal.adaptive.exceptions.RequestFailureException;

/**
 * <p> Java class to make Parallel Payment requests.
 *
 */
public class ParallelPay {

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
	protected List<Receiver> receivers;
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
     * Required CancelUrl
     */
    protected String cancelUrl;
    /*
     * Required returnUrl
     */
    protected String returnUrl;
    /*
     * Required User IP Address
     */
    protected String userIp;
    /*
     * Optional ipnURL
     */
    protected String ipnURL;
    /*
	 * Optional Sender Email
	 */
	protected String senderEmail;
    
    // internal field
    protected boolean requestProcessed = false;
    // internal field
    protected int numberOfReceivers;
    /*
     * Default constructor
     */
    public ParallelPay(int numberOfReceivers) throws NotEnoughReceivers{
    	if(numberOfReceivers < 2){
    		// throw exception
    		throw new NotEnoughReceivers(2, numberOfReceivers);
    	}
    	this.numberOfReceivers = numberOfReceivers;
		this.receivers = new ArrayList<Receiver>(numberOfReceivers);
	}
    
	public PayResponse makeRequest()
	throws IOException, MalformedURLException, MissingAPICredentialsException,
	   		InvalidAPICredentialsException, MissingParameterException, 
	   		UnsupportedEncodingException, RequestFailureException,
	   		InvalidResponseDataException, PayPalErrorException,
	   		RequestAlreadyMadeException, PaymentExecException,
	   		AuthorizationRequiredException, PaymentInCompleteException,
	   		ReceiversCountMismatchException {
		
		validate();
		
		PaymentDetails paymentDetails = new PaymentDetails(ActionType.PAY);
		PayRequest payRequest = new PayRequest(language, env);
		paymentDetails.setReceiverList(receivers);
		if(ipnURL != null){
			paymentDetails.setIpnNotificationUrl(ipnURL);
		}
		paymentDetails.setCurrencyCode(currencyCode);
		paymentDetails.setCancelUrl(cancelUrl);
		paymentDetails.setReturnUrl(returnUrl);
		if(senderEmail != null && senderEmail.length() > 0){
			paymentDetails.setSenderEmail(senderEmail);
		}
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
                        // throw exception to redirect user for authorization
                        AuthorizationRequiredException ex = new AuthorizationRequiredException();
                        ex.setPayKey(payResp.getPayKey());
                        throw ex;
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
	throws MissingParameterException,RequestAlreadyMadeException, ReceiversCountMismatchException {
		
		if(requestProcessed){
			// throw error
			throw new RequestAlreadyMadeException();
		}
		if(language == null){
			// throw error
			throw new MissingParameterException("language");
		}
		if(receivers == null){
			// throw error
			throw new MissingParameterException("Receivers");
		}
		if(numberOfReceivers != receivers.size()) {
			// throw error
			throw new ReceiversCountMismatchException(numberOfReceivers, receivers.size());
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
		if(returnUrl == null){
			// throw error
			throw new MissingParameterException("returnUrl");
		}
		if(cancelUrl == null){
			// throw error
			throw new MissingParameterException("cancelUrl");
		}
		if(userIp == null){
			// throw error
			throw new MissingParameterException("userIp");
		}
		if(applicationName == null){
			// throw error
			throw new MissingParameterException("applicationName");
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
	public List<Receiver> getReceivers() {
		return receivers;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceivers(List<Receiver> receivers) {
		this.receivers = receivers;
	}
	/**
	 * @param receiver the receiver to set
	 */
	public void addToReceivers(Receiver receiver) {
		this.receivers.add(receiver);
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
	 * @return the cancelUrl
	 */
	public String getCancelUrl() {
		return cancelUrl;
	}

	/**
	 * @param cancelUrl the cancelUrl to set
	 */
	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	/**
	 * @return the returnUrl
	 */
	public String getReturnUrl() {
		return returnUrl;
	}

	/**
	 * @param returnUrl the returnUrl to set
	 */
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
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
