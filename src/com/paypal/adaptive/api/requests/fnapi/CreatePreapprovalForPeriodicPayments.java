/**
 * 
 */
package com.paypal.adaptive.api.requests.fnapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import com.paypal.adaptive.api.requests.PreapprovalRequest;
import com.paypal.adaptive.api.responses.PreapprovalResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.ClientDetails;
import com.paypal.adaptive.core.CurrencyCodes;
import com.paypal.adaptive.core.DayOfWeek;
import com.paypal.adaptive.core.PaymentPeriodType;
import com.paypal.adaptive.core.PinType;
import com.paypal.adaptive.core.PreapprovalDetails;
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
 * <p> Java class to create Preapproval for periodic payments.
 *
 */
public class CreatePreapprovalForPeriodicPayments {

	/*
	 * Required applicationName
	 */
	protected String applicationName;
	/*
	 * Required APICredential
	 */
	protected APICredential credentialObj;
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
     * Required startingDate
     */
	protected String startingDate;
	/*
	 * Required maxTotalAmountOfAllPayments
	 */
    protected double maxTotalAmountOfAllPayments;
    /*
     * Required maxAmountPerPayment
     */
    protected double maxAmountPerPayment;
    /*
     * Required endingDate
     */
    protected String endingDate;
    /*
     * Required paymentPeriod
     */
    protected PaymentPeriodType paymentPeriod;
    /*
     * Optional dayOfMonth - Required when PaymentPeriod is monthly
     */
    protected int dateOfMonth;
    /*
     * Optional dayOfMonth - Required when PaymentPeriod is weekly
     */
    protected DayOfWeek dayOfWeek;
    /*
     * Required maxNumberOfPaymentsPerPeriod
     */
    protected int maxNumberOfPaymentsPerPeriod;
    /*
     * Optional ipnURL
     */
    protected String ipnURL;
    /*
     * Optional PinType
     */
    protected PinType pinType;
    /*
	 * Optional Sender Email
	 */
	protected String senderEmail;
    // internal field
    protected boolean requestProcessed = false;
    
    /*
     * Default constructor
     */
    public CreatePreapprovalForPeriodicPayments(PaymentPeriodType paymentPeriod) throws MissingParameterException{
    	if(paymentPeriod == null){
    		// throw error
    		throw new MissingParameterException("paymentPeriod");
    	}
		this.paymentPeriod = paymentPeriod;
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
		if(startingDate == null){
			// throw error
			throw new MissingParameterException("startingDate");
		}
		if(endingDate == null){
			// throw error
			throw new MissingParameterException("endingDate");
		}
		if(maxTotalAmountOfAllPayments <=0 ){
			// throw error
			throw new MissingParameterException("maxTotalAmountOfAllPayments");
		}
		if(maxAmountPerPayment <= 0){
			// thow error
			throw new MissingParameterException("maxAmountPerPayment");
		}
		if(paymentPeriod == PaymentPeriodType.MONTHLY){
			if(dateOfMonth < 0 || dateOfMonth > 31){
				// throw error
				throw new MissingParameterException("dayOfMonth");
			}
		} else if(paymentPeriod == PaymentPeriodType.WEEKLY){
			if(dayOfWeek == null){
				// throw error
				throw new MissingParameterException("dayOfWeek");
			}
		}
		if(maxNumberOfPaymentsPerPeriod <=0 ){
			// throw error
			throw new MissingParameterException("maxNumberOfPaymentsPerPeriod");
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
			// throw applicationName
			throw new MissingParameterException("applicationName");
		}
	}
    
    public PreapprovalResponse makeRequest()
	throws IOException, MalformedURLException, MissingAPICredentialsException,
	   		InvalidAPICredentialsException, MissingParameterException, 
	   		UnsupportedEncodingException, RequestFailureException,
	   		InvalidResponseDataException, PayPalErrorException,
	   		RequestAlreadyMadeException, PaymentExecException,
	   		AuthorizationRequiredException, PaymentInCompleteException {
		
		validate();
		
		PreapprovalRequest preapprovalRequest = new PreapprovalRequest(language, env);
		PreapprovalDetails preapprovalDetails = new PreapprovalDetails();
		
		// set clientDetails
		ClientDetails clientDetails = new ClientDetails();
		clientDetails.setIpAddress(userIp);
		clientDetails.setApplicationId(applicationName);
		preapprovalRequest.setClientDetails(clientDetails);
		
		preapprovalDetails.setCancelUrl(cancelUrl);
		preapprovalDetails.setReturnUrl(returnUrl);
		if(ipnURL != null){
			preapprovalDetails.setIpnNotificationUrl(ipnURL);
		}
		preapprovalDetails.setCurrencyCode(currencyCode);	
		preapprovalDetails.setEndingDate(endingDate);
		preapprovalDetails.setStartingDate(startingDate);
		
		preapprovalDetails.setMaxTotalAmountOfAllPayments(maxTotalAmountOfAllPayments);	
		preapprovalDetails.setMaxAmountPerPayment(maxAmountPerPayment);
		preapprovalDetails.setMaxNumberOfPaymentsPerPeriod(maxNumberOfPaymentsPerPeriod);
		preapprovalDetails.setPaymentPeriod(paymentPeriod);
		preapprovalDetails.setDateOfMonth(dateOfMonth);
		preapprovalDetails.setDayOfWeek(dayOfWeek);
		
		if(pinType != null)
			preapprovalDetails.setPinType(pinType);
		
		if(senderEmail != null && senderEmail.length() > 0){
			preapprovalDetails.setSenderEmail(senderEmail);
		}
		// set preapproval details
		preapprovalRequest.setPreapprovalDetails(preapprovalDetails);
		
		
		PreapprovalResponse payResp = preapprovalRequest.execute(credentialObj);
		return payResp;
	
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
	 * @return the startingDate
	 */
	public String getStartingDate() {
		return startingDate;
	}
	/**
	 * @param startingDate the startingDate to set
	 */
	public void setStartingDate(String startingDate) {
		this.startingDate = startingDate;
	}
	/**
	 * @return the maxTotalAmountOfAllPayments
	 */
	public double getMaxTotalAmountOfAllPayments() {
		return maxTotalAmountOfAllPayments;
	}
	/**
	 * @param maxTotalAmountOfAllPayments the maxTotalAmountOfAllPayments to set
	 */
	public void setMaxTotalAmountOfAllPayments(double maxTotalAmountOfAllPayments) {
		this.maxTotalAmountOfAllPayments = maxTotalAmountOfAllPayments;
	}
	/**
	 * @return the endingDate
	 */
	public String getEndingDate() {
		return endingDate;
	}
	/**
	 * @param endingDate the endingDate to set
	 */
	public void setEndingDate(String endingDate) {
		this.endingDate = endingDate;
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
	 * @return the maxAmountPerPayment
	 */
	public double getMaxAmountPerPayment() {
		return maxAmountPerPayment;
	}
	/**
	 * @param maxAmountPerPayment the maxAmountPerPayment to set
	 */
	public void setMaxAmountPerPayment(double maxAmountPerPayment) {
		this.maxAmountPerPayment = maxAmountPerPayment;
	}
	/**
	 * @return the paymentPeriod
	 */
	public PaymentPeriodType getPaymentPeriod() {
		return paymentPeriod;
	}
	/**
	 * @param paymentPeriod the paymentPeriod to set
	 */
	public void setPaymentPeriod(PaymentPeriodType paymentPeriod) {
		this.paymentPeriod = paymentPeriod;
	}
	/**
	 * @return the maxNumberOfPaymentsPerPeriod
	 */
	public int getMaxNumberOfPaymentsPerPeriod() {
		return maxNumberOfPaymentsPerPeriod;
	}
	/**
	 * @param maxNumberOfPaymentsPerPeriod the maxNumberOfPaymentsPerPeriod to set
	 */
	public void setMaxNumberOfPaymentsPerPeriod(int maxNumberOfPaymentsPerPeriod) {
		this.maxNumberOfPaymentsPerPeriod = maxNumberOfPaymentsPerPeriod;
	}
	/**
	 * @return the dateOfMonth
	 */
	public int getDateOfMonth() {
		return dateOfMonth;
	}
	/**
	 * @param dateOfMonth the dateOfMonth to set
	 */
	public void setDateOfMonth(int dateOfMonth) {
		this.dateOfMonth = dateOfMonth;
	}
	/**
	 * @return the dayOfWeek
	 */
	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}
	/**
	 * @param dayOfWeek the dayOfWeek to set
	 */
	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	/**
	 * @return the pinType
	 */
	public PinType getPinType() {
		return pinType;
	}
	/**
	 * @param pinType the pinType to set
	 */
	public void setPinType(PinType pinType) {
		this.pinType = pinType;
	}
}
