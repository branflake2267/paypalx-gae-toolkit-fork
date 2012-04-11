/**
 * 
 */
package com.paypal.adaptive.api.requests.fnapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.paypal.adaptive.api.requests.ConvertCurrencyRequest;
import com.paypal.adaptive.api.responses.ConvertCurrencyResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.CurrencyCodes;
import com.paypal.adaptive.core.CurrencyType;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.InvalidAPICredentialsException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingAPICredentialsException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.RequestAlreadyMadeException;
import com.paypal.adaptive.exceptions.RequestFailureException;

/**
 * <p>Java class for converting currencies.
 * 
 */
public class ConvertCurrency {

	/*
	 * Required APICredential
	 */
	protected APICredential credentialObj;
	/*
	 * Required Environment
	 */
	protected ServiceEnvironment env;
    /*
     * Required language for localization
     */
    protected String language;
    /*
     * Required baseAmountList
     */
    protected List<CurrencyType> baseAmountList;
    /*
     * Required ConvertToCurrencyList
     */
    protected List<CurrencyCodes> convertToCurrencyList;
    
    // internal field
    protected boolean requestProcessed = false;
    
    /*
     * Default constructor
     */
    public ConvertCurrency(){
		
	}
    
	public ConvertCurrencyResponse makeRequest() throws MissingParameterException, 
	RequestAlreadyMadeException, MissingAPICredentialsException, InvalidAPICredentialsException, 
	RequestFailureException, InvalidResponseDataException, IOException, PayPalErrorException
	 {
		
		validate();
		
		ConvertCurrencyRequest currencyRequest = new ConvertCurrencyRequest(language, env);
		currencyRequest.setBaseAmountList(baseAmountList);
		currencyRequest.setConvertToCurrencyList(convertToCurrencyList);
		ConvertCurrencyResponse currResp = currencyRequest.execute(credentialObj);
		return currResp;
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
		if(this.baseAmountList == null || this.baseAmountList.size() <=0)
			throw new MissingParameterException("baseAmountList");
		
		if(this.convertToCurrencyList == null || this.convertToCurrencyList.size() <= 0)
			throw new MissingParameterException("convertToCurrencyList");
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
	 * @return the baseAmountList
	 */
	public List<CurrencyType> getBaseAmountList() {
		return baseAmountList;
	}

	/**
	 * @param baseAmountList the baseAmountList to set
	 */
	public void setBaseAmountList(List<CurrencyType> baseAmountList) {
		this.baseAmountList = baseAmountList;
	}
	/**
	 * @param currencyType the currencyTyoe to set
	 */
	
	public void addToBaseAmountList(CurrencyType currType){
		if(this.baseAmountList == null){
			this.baseAmountList = new ArrayList<CurrencyType>();
		}
		this.baseAmountList.add(currType);
	}

	/**
	 * @return the convertToCurrencyList
	 */
	public List<CurrencyCodes> getConvertToCurrencyList() {
		return convertToCurrencyList;
	}

	/**
	 * @param convertToCurrencyList the convertToCurrencyList to set
	 */
	public void setConvertToCurrencyList(List<CurrencyCodes> convertToCurrencyList) {
		this.convertToCurrencyList = convertToCurrencyList;
	}
	/**
	 * 
	 * @param code
	 */
	public void addToConvertToCurrencyList(CurrencyCodes code){
		if(this.convertToCurrencyList == null)
			this.convertToCurrencyList = new ArrayList<CurrencyCodes>();
		
		this.convertToCurrencyList.add(code);
	}
	

}
