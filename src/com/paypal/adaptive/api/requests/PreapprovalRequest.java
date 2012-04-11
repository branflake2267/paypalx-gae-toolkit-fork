
package com.paypal.adaptive.api.requests;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paypal.adaptive.api.responses.PreapprovalResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.ClientDetails;
import com.paypal.adaptive.core.ParameterUtils;
import com.paypal.adaptive.core.PreapprovalDetails;
import com.paypal.adaptive.core.RequestEnvelope;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.InvalidAPICredentialsException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingAPICredentialsException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.PaymentExecException;
import com.paypal.adaptive.exceptions.RequestFailureException;


/**
 * Preaproval API provides way to obtain preapprovals, which are an approval to make future payments on the
senders behalf.
 * 
 */
public class PreapprovalRequest extends PayPalBaseRequest{

	private static final Logger log = Logger.getLogger(PreapprovalRequest.class.getName());


    protected ClientDetails clientDetails;
    protected PreapprovalDetails preapprovalDetails;
    
    
    public PreapprovalRequest(String language, ServiceEnvironment env){
    	
    	requestEnvelope = new RequestEnvelope();
    	requestEnvelope.setErrorLanguage(language);
    	this.env = env;
    	preapprovalDetails = new PreapprovalDetails();
    	
    }
    
    public PreapprovalResponse execute(APICredential credentialObj)
    throws IOException, MalformedURLException, MissingAPICredentialsException,
	   InvalidAPICredentialsException, MissingParameterException, 
	   UnsupportedEncodingException, RequestFailureException,
	   InvalidResponseDataException, PaymentExecException{
    	String responseString = "";
    	// do input validation
    	
    	/*
    	 * check for the following things
    	 *  1. API Credentials
    	 *  2. Atleast one receiver has been set
    	 *  3. CurrencyCode is set
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
    	
    	if(this.preapprovalDetails.getCancelUrl() == null){
    		throw new MissingParameterException("cancelUrl");
    	}
    	if(this.preapprovalDetails.getReturnUrl() == null){
    		throw new MissingParameterException("returnUrl");
    	}
    	if(this.preapprovalDetails.getStartingDate() == null){
    		throw new MissingParameterException("startingDate");
    	}
    	if(this.preapprovalDetails.getEndingDate() == null){
    		throw new MissingParameterException("endingDate");
    	}
    	if(this.preapprovalDetails.getMaxTotalAmountOfAllPayments() <= 0){
    		throw new MissingParameterException("maxTotalAmountOfAllPayments");
    	}
    	if(this.preapprovalDetails.getCurrencyCode() == null){
    		throw new MissingParameterException("CurrencyCode");
    	}
    	
    	// prepare request parameters
    	StringBuilder postParameters = new StringBuilder();
    	
    	// add request envelope
    	postParameters.append(requestEnvelope.serialize());
    	postParameters.append(ParameterUtils.PARAM_SEP);
    	// set clientDetails
    	postParameters.append(this.clientDetails.serialize());
    	postParameters.append(ParameterUtils.PARAM_SEP);
    	// add payment details
    	postParameters.append(this.preapprovalDetails.serialize());
    	    	
    	if(log.isLoggable(Level.INFO))
    		log.info("Sending PreapprovalRequest with: " + postParameters.toString());
    	
    	// send request
		responseString = makeRequest(credentialObj, "Preapproval", postParameters.toString());
        
    	// parse response
        PreapprovalResponse response = new PreapprovalResponse(responseString);
    	
    	// handle errors
    	return response;
    }
    
	/**
	 * @param clientDetails the clientDetails to set
	 */
	public void setClientDetails(ClientDetails clientDetails) {
		this.clientDetails = clientDetails;
	}
	/**
	 * @return the clientDetails
	 */
	public ClientDetails getClientDetails() {
		return clientDetails;
	}

	public PreapprovalDetails getPreapprovalDetails() {
		return preapprovalDetails;
	}

	public void setPreapprovalDetails(PreapprovalDetails preapprovalDetails) {
		this.preapprovalDetails = preapprovalDetails;
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
