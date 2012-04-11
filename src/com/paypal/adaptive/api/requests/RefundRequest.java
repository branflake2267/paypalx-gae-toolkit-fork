
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

import com.paypal.adaptive.api.responses.RefundResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.CurrencyCodes;
import com.paypal.adaptive.core.ParameterUtils;
import com.paypal.adaptive.core.Receiver;
import com.paypal.adaptive.core.RequestEnvelope;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.InvalidAPICredentialsException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingAPICredentialsException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.RequestFailureException;

/**
 * Refund API refunds all or part of a payment.
 * 
 */
public class RefundRequest extends PayPalBaseRequest{

	private static final Logger log = Logger.getLogger(RefundRequest.class.getName());

    protected CurrencyCodes currencyCode;
    protected String payKey;
    protected String transactionId;
    protected String trackingId;
    protected List<Receiver> receiverList;

    public RefundRequest(String language, ServiceEnvironment env){
    	
    	requestEnvelope = new RequestEnvelope();
    	requestEnvelope.setErrorLanguage(language);
    	this.env = env;
    }
    
    public RefundResponse execute(APICredential credentialObj) 
    throws MissingParameterException, MissingAPICredentialsException, InvalidAPICredentialsException, 
    PayPalErrorException, RequestFailureException, IOException, InvalidResponseDataException{
    	String responseString = "";
    	// do input validation
    	/* - VALIDATE REQUIRED PARAMS- */
    	/*
    	 * check for the following things
    	 *  1. API Credentials
    	 *  2. Atleast one of payKey, transactionId, trackingId
    	 */
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
    	if(this.trackingId == null && this.transactionId == null && this.payKey == null){
    		throw new MissingParameterException("payKey");
    	}
    	// prepare request parameters
    	StringBuilder postParameters = new StringBuilder();
    	
    	// add request envelope
    	postParameters.append(requestEnvelope.serialize());
    	postParameters.append(ParameterUtils.PARAM_SEP);
    	
    	// add receiverList
    	int i = 0;
    	if(receiverList != null) {
	    	for(Receiver recvr: receiverList){
	    		postParameters.append(recvr.serialize(i));
	    		i++;
	    	}
    	}
    	// add currencyCode
    	if(this.currencyCode != null) {
	    	postParameters.append(ParameterUtils.createUrlParameter("currencyCode", this.getCurrencyCode().toString()));
	    	postParameters.append(ParameterUtils.PARAM_SEP);
    	}
    	// set trackingId
    	if(this.trackingId != null) {
    		postParameters.append(ParameterUtils.createUrlParameter("trackingId", this.getTrackingId()));
    		postParameters.append(ParameterUtils.PARAM_SEP);
    	}
    	// set transactionId
    	if(this.transactionId != null) {
    		postParameters.append(ParameterUtils.createUrlParameter("transactionId", this.getTransactionId()));
    		postParameters.append(ParameterUtils.PARAM_SEP);
    	}
    	
    	// set preapprovalKey
    	if(this.payKey != null) {
    		postParameters.append(ParameterUtils.createUrlParameter("payKey", this.getPayKey()));
    		postParameters.append(ParameterUtils.PARAM_SEP);
    	}
    	
    	if(log.isLoggable(Level.INFO))
    		log.info("Sending RefundRequest with: " + postParameters.toString());
    	
    	
    	// send request
		responseString = makeRequest(credentialObj, "Refund", postParameters.toString());
    	    	
    	// parse response
        RefundResponse response = new RefundResponse(responseString);
    	
    	// handle errors
    	return response;
    }

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

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public CurrencyCodes getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(CurrencyCodes value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the payKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayKey() {
        return payKey;
    }

    /**
     * Sets the value of the payKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayKey(String value) {
        this.payKey = value;
    }

    /**
     * Gets the value of the transactionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the value of the transactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionId(String value) {
        this.transactionId = value;
    }

    /**
     * Gets the value of the trackingId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrackingId() {
        return trackingId;
    }

    /**
     * Sets the value of the trackingId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrackingId(String value) {
        this.trackingId = value;
    }

    /**
	 * @param receiverList the receiverList to set
	 */
	public void setReceiverList(List<Receiver> receiverList) {
		this.receiverList = receiverList;
	}
	/**
	 * @param receiverList the receiverList to set
	 */
	public void addToReceiverList(Receiver receiver) {
		if(this.receiverList == null)
			this.receiverList = new ArrayList<Receiver>();
		
		this.receiverList.add(receiver);
	}
	/**
	 * @return the receiverList
	 */
	public List<Receiver> getReceiverList() {
		return receiverList;
	}

public String toString(){
		
		StringBuilder outStr = new StringBuilder();
		
		outStr.append("<table border=1>");
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
