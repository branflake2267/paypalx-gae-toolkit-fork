/**
 * 
 */
package com.paypal.adaptive.api.requests;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paypal.adaptive.api.responses.GetShippingAddressesResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.ParameterUtils;
import com.paypal.adaptive.core.RequestEnvelope;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.RequestFailureException;



/**
 * PaymentDetails API provides information about a payment set up with the Pay API operation.
 * 
 */
public class GetShippingAddressesRequest extends PayPalBaseRequest{

	private static final Logger log = Logger.getLogger(GetShippingAddressesRequest.class.getName());

	protected String key;

 
    public GetShippingAddressesRequest(String language, ServiceEnvironment env){
    	
    	requestEnvelope = new RequestEnvelope();
    	requestEnvelope.setErrorLanguage(language);
    	this.env = env;
    	
    }

	
	public GetShippingAddressesResponse execute(APICredential credentialObj) 
	throws MissingParameterException, InvalidResponseDataException, RequestFailureException, IOException,
			PayPalErrorException {
    	String responseString = "";
    	// do input validation
    	if ( this.key == null || this.key=="") {
	    	throw new MissingParameterException("key");
	    }
    	
    	// prepare request parameters
    	StringBuilder postParameters = new StringBuilder();
    	
    	// add request envelope
    	postParameters.append(requestEnvelope.serialize());
    	postParameters.append(ParameterUtils.PARAM_SEP);
    	// add key
    	postParameters.append(ParameterUtils.createUrlParameter("key", this.key));
    	postParameters.append(ParameterUtils.PARAM_SEP);
    	    	
    	if(log.isLoggable(Level.INFO))
    		log.info("Sending GetShippingAddressesRequest with: " + postParameters.toString());    	
    	
    	// send request
		responseString = makeRequest(credentialObj, "GetShippingAddresses", postParameters.toString());
    	    	
    	// parse response
        GetShippingAddressesResponse response = new GetShippingAddressesResponse(responseString);
    	
    	// handle errors
    	return response;
    }


	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}


	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
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
