
package com.paypal.adaptive.api.requests;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paypal.adaptive.api.responses.CancelPreapprovalResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.ParameterUtils;
import com.paypal.adaptive.core.RequestEnvelope;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.RequestFailureException;


/**
 * CancelPreapprovel API cancels a preapproval set up with the Preapproval API operation. 
 * 
 */
public class CancelPreapprovalRequest extends PayPalBaseRequest{

	private static final Logger log = Logger.getLogger(CancelPreapprovalRequest.class.getName());

	protected String preapprovalKey;
	


	public CancelPreapprovalRequest(String language, ServiceEnvironment env){

		requestEnvelope = new RequestEnvelope();
		requestEnvelope.setErrorLanguage(language);
		this.env = env;

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
	 * Gets the value of the preapprovalKey property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getPreapprovalKey() {
		return preapprovalKey;
	}

	/**
	 * Sets the value of the preapprovalKey property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setPreapprovalKey(String value) {
		this.preapprovalKey = value;
	}


	public CancelPreapprovalResponse execute(APICredential credentialObj) 
	throws MissingParameterException, InvalidResponseDataException, RequestFailureException, IOException  {
		String responseString = "";
		// do input validation
		if ( this.preapprovalKey == null || this.preapprovalKey=="") {
			throw new MissingParameterException("payKey");
		}

		// prepare request parameters
		StringBuilder postParameters = new StringBuilder();

		// add request envelope
		postParameters.append(requestEnvelope.serialize());
		postParameters.append(ParameterUtils.PARAM_SEP);
		// add payKey
		postParameters.append(ParameterUtils.createUrlParameter("preapprovalKey", this.preapprovalKey));
		postParameters.append(ParameterUtils.PARAM_SEP);

		if(log.isLoggable(Level.INFO))
			log.info("Sending CancePreapprovalRequest with: " + postParameters.toString());

		
		// send request
		responseString = makeRequest(credentialObj, "CancelPreapproval", postParameters.toString());

		// parse response
		CancelPreapprovalResponse response = new CancelPreapprovalResponse(responseString);

		// handle errors
		return response;
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
