
package com.paypal.adaptive.api.requests;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paypal.adaptive.api.responses.ConvertCurrencyResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.CurrencyCodes;
import com.paypal.adaptive.core.CurrencyType;
import com.paypal.adaptive.core.ParameterUtils;
import com.paypal.adaptive.core.RequestEnvelope;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.InvalidAPICredentialsException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingAPICredentialsException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.RequestFailureException;


/**
 * The ConvertCurrency API provides Foreign Exchange currency conversion rates for a list of amounts.
 * 
 */
public class ConvertCurrencyRequest extends PayPalBaseRequest{

	private static final Logger log = Logger.getLogger(ConvertCurrencyRequest.class.getName());

    protected List<CurrencyType> baseAmountList;
    protected List<CurrencyCodes> convertToCurrencyList;
    
    /**
     * Gets the value of the requestEnvelope property.
     * 
     * @return
     *     possible object is
     *     {@link RequestEnvelope }
     *     
     */
    public RequestEnvelope getRequestEnvelope() {
        return requestEnvelope;
    }

    /**
     * Sets the value of the requestEnvelope property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestEnvelope }
     *     
     */
    public void setRequestEnvelope(RequestEnvelope value) {
        this.requestEnvelope = value;
    }


    public ConvertCurrencyRequest(String language, ServiceEnvironment env){
    	
    	requestEnvelope = new RequestEnvelope();
    	requestEnvelope.setErrorLanguage(language);
    	this.env = env;    	
    }
    
    public ConvertCurrencyResponse execute(APICredential credentialObj) 
    throws MissingParameterException, MissingAPICredentialsException, InvalidAPICredentialsException, 
    RequestFailureException, InvalidResponseDataException, IOException, PayPalErrorException {
    	String responseString = "";
    	// do input validation
    	if(credentialObj == null){
    		throw new MissingAPICredentialsException();
    	} else if(credentialObj != null) {
			InvalidAPICredentialsException ex = new InvalidAPICredentialsException();
			if (credentialObj.getAppId() == null
					|| credentialObj.getAppId().length() <= 0) {
				ex.addToMissingCredentials("AppId");
			}
			if (credentialObj.getAPIPassword() == null
					|| credentialObj.getAPIPassword().length() <= 0) {
				ex.addToMissingCredentials("APIPassword");
			}

			if (credentialObj.getAPIUsername() == null
					|| credentialObj.getAPIUsername().length() <= 0) {
				ex.addToMissingCredentials("APIUsername");
			}
			if (credentialObj.getSignature() == null
					|| credentialObj.getSignature().length() <= 0) {
				ex.addToMissingCredentials("Signature");
			}
			if(ex.getMissingCredentials() != null){
				throw ex;
			} else {
				ex = null;
			}
		}
    	// atleast one baseamountlist and one convertToCurrencylist
    	if(this.baseAmountList == null || this.baseAmountList.size() <=0 )
    		throw new MissingParameterException("baseAmountList");

    	if(this.convertToCurrencyList == null || this.convertToCurrencyList.size() <=0 )
    		throw new MissingParameterException("convertToCurrencyList");

    	// prepare request parameters
    	StringBuilder postParameters = new StringBuilder();
    	
    	// add request envelope
    	postParameters.append(requestEnvelope.serialize());
    	postParameters.append(ParameterUtils.PARAM_SEP);
    	
    	// add baseAmountList
    	int i = 0;
    	if(baseAmountList != null) {
	    	for(CurrencyType currType: baseAmountList){
	    		postParameters.append(currType.serialize(i));
	    		i++;
	    	}
    	}
    	// add receiverList
    	i = 0;
    	if(convertToCurrencyList != null) {
	    	for(CurrencyCodes code: convertToCurrencyList){
	    		postParameters.append(ParameterUtils.createUrlParameter("convertToCurrencyList.currencyCode("+i+")", code.toString()));
	    		postParameters.append(ParameterUtils.PARAM_SEP);
	    		i++;
	    	}
    	}
    	if(log.isLoggable(Level.INFO))
    		log.info("Sending ConvertCurrency Request with: " + postParameters.toString());

    	// send request
		responseString = makeRequest(credentialObj, "ConvertCurrency", postParameters.toString());
    	    	
    	// parse response
        ConvertCurrencyResponse response = new ConvertCurrencyResponse(responseString);
    	
    	// handle errors
    	return response;
    }

	public List<CurrencyType> getBaseAmountList() {
		return baseAmountList;
	}

	public void setBaseAmountList(List<CurrencyType> baseAmountList) {
		this.baseAmountList = baseAmountList;
	}
	
	public void addToBaseAmountList(CurrencyType currType){
		if(this.baseAmountList == null){
			this.baseAmountList = new ArrayList<CurrencyType>();
		}
		this.baseAmountList.add(currType);
	}

	public List<CurrencyCodes> getConvertToCurrencyList() {
		return convertToCurrencyList;
	}

	public void setConvertToCurrencyList(List<CurrencyCodes> convertToCurrencyList) {
		this.convertToCurrencyList = convertToCurrencyList;
	}
	public void addToConvertToCurrencyList(CurrencyCodes code){
		if(this.convertToCurrencyList == null)
			this.convertToCurrencyList = new ArrayList<CurrencyCodes>();
		
		this.convertToCurrencyList.add(code);
	}

public String toString(){
		
		StringBuilder outStr = new StringBuilder();
		
		outStr.append("<table border=1 width=100%>");
		outStr.append("<tr><th>");
		outStr.append(this.getClass().getSimpleName());
		outStr.append("</th><td></td></tr>");
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo( this.getClass(), Object.class );
			for ( PropertyDescriptor pd : info.getPropertyDescriptors() ) {
				try {
					String name = pd.getName();
					Object value = this.getClass().getDeclaredField(name).get(this);
					if(value != null) {
						outStr.append("<tr><td>");
						outStr.append(pd.getName());
						outStr.append("</td><td>");
						outStr.append(value.toString());
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				outStr.append("</td></tr>");
			}
	    } catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outStr.append("</table>");
		return outStr.toString(); 
		
	}

}
