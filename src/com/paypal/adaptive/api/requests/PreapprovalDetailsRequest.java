package com.paypal.adaptive.api.requests;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paypal.adaptive.api.responses.PreapprovalDetailsResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.ParameterUtils;
import com.paypal.adaptive.core.RequestEnvelope;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.RequestFailureException;

/**
 * PreapprovalDetails API provides information about a preapproval set up with
 * the Preapproval API operation.
 * 
 */
public class PreapprovalDetailsRequest extends PayPalBaseRequest {

	private static final Logger log = Logger
			.getLogger(PreapprovalDetailsRequest.class.getName());

	protected String preapprovalKey;
	protected boolean getBillingAddress = false;

	/**
	 * Gets the value of the requestEnvelope property.
	 * 
	 * @return possible object is {@link RequestEnvelope }
	 * 
	 */
	public RequestEnvelope getRequestEnvelope() {
		return requestEnvelope;
	}

	/**
	 * Sets the value of the requestEnvelope property.
	 * 
	 * @param value
	 *            allowed object is {@link RequestEnvelope }
	 * 
	 */
	public void setRequestEnvelope(RequestEnvelope value) {
		this.requestEnvelope = value;
	}

	/**
	 * Gets the value of the preapprovalKey property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPreapprovalKey() {
		return preapprovalKey;
	}

	/**
	 * Sets the value of the preapprovalKey property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPreapprovalKey(String value) {
		this.preapprovalKey = value;
	}

	/**
	 * Gets the value of the getBillingAddress property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public boolean isGetBillingAddress() {
		return getBillingAddress;
	}

	/**
	 * Sets the value of the getBillingAddress property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setGetBillingAddress(boolean value) {
		this.getBillingAddress = value;
	}

	public PreapprovalDetailsRequest(String language, ServiceEnvironment env) {

		requestEnvelope = new RequestEnvelope();
		requestEnvelope.setErrorLanguage(language);
		this.env = env;
	}

	public PreapprovalDetailsResponse execute(APICredential credentialObj)
			throws MissingParameterException, InvalidResponseDataException,
			RequestFailureException, IOException {
		String responseString = "";
		// do input validation
		if (preapprovalKey == null | preapprovalKey.length() <= 0)
			throw new MissingParameterException("preapprovalKey");
		// prepare request parameters
		StringBuilder postParameters = new StringBuilder();

		// add request envelope
		postParameters.append(requestEnvelope.serialize());
		postParameters.append(ParameterUtils.PARAM_SEP);
		// set preapprovalKey
		postParameters.append("preapprovalKey=");
		postParameters.append(this.preapprovalKey);

		// add getBillingAddress
		if (this.getBillingAddress) {
			postParameters.append(ParameterUtils.PARAM_SEP);
			postParameters.append("getBillingAddress=");
			postParameters.append(this.getBillingAddress);
		}

		if (log.isLoggable(Level.INFO))
			log.info("Sending PreapprovalDetails Request with: "
					+ postParameters.toString());

		// send request
		responseString = makeRequest(credentialObj, "PreapprovalDetails", postParameters.toString());

		// parse response
		PreapprovalDetailsResponse response = new PreapprovalDetailsResponse(
				responseString);

		// handle errors
		return response;
	}

	public String toString() {

		StringBuilder outStr = new StringBuilder();

		outStr.append("<table border=1>");
		outStr.append("<tr><th>");
		outStr.append(this.getClass().getSimpleName());
		outStr.append("</th><td></td></tr>");
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(this.getClass(), Object.class);
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				try {
					String name = pd.getName();
					Object value = this.getClass().getDeclaredField(name)
							.get(this);
					if (value != null) {
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
