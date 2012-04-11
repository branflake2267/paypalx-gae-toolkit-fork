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

import com.paypal.adaptive.api.responses.ExecutePaymentResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.ActionType;
import com.paypal.adaptive.core.ParameterUtils;
import com.paypal.adaptive.core.RequestEnvelope;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.AuthorizationRequiredException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.PaymentExecException;
import com.paypal.adaptive.exceptions.PaymentInCompleteException;
import com.paypal.adaptive.exceptions.RequestFailureException;



/**
 * PaymentDetails API provides information about a payment set up with the Pay API operation.
 * 
 */
public class ExecutePaymentRequest extends PayPalBaseRequest{

	private static final Logger log = Logger.getLogger(ExecutePaymentRequest.class.getName());

	protected String payKey;
	protected ActionType actionType = null;
	protected String fundingPlanId;
 
    public ExecutePaymentRequest(String language, ServiceEnvironment env){
    	
    	requestEnvelope = new RequestEnvelope();
    	requestEnvelope.setErrorLanguage(language);
    	this.env = env;
    	
    }

	public String getPayKey() {
		return payKey;
	}

	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}

		
	public ExecutePaymentResponse execute(APICredential credentialObj) throws MissingParameterException, InvalidResponseDataException, RequestFailureException, IOException, PayPalErrorException, PaymentExecException, AuthorizationRequiredException, PaymentInCompleteException {
    	String responseString = "";
    	// do input validation
    	if ( this.payKey == null || this.payKey=="") {
	    	throw new MissingParameterException("payKey");
	    }
    	
    	// prepare request parameters
    	StringBuilder postParameters = new StringBuilder();
    	
    	// add request envelope
    	postParameters.append(requestEnvelope.serialize());
    	postParameters.append(ParameterUtils.PARAM_SEP);
    	// add payKey
    	postParameters.append(ParameterUtils.createUrlParameter("payKey", this.payKey));
    	postParameters.append(ParameterUtils.PARAM_SEP);
    	// add actionType if set
    	if(this.actionType != null){
    		postParameters.append(ParameterUtils.createUrlParameter("actionType", this.actionType.toString()));
        	postParameters.append(ParameterUtils.PARAM_SEP);
        }
    	if(this.fundingPlanId != null){
    		postParameters.append(ParameterUtils.createUrlParameter("fundingPlanId", this.fundingPlanId));
        	postParameters.append(ParameterUtils.PARAM_SEP);
    	}
    	
    	    	
    	if(log.isLoggable(Level.INFO))
    		log.info("Sending PaymentDetails Request with: " + postParameters.toString());    	
    	
    	// send request
		responseString = makeRequest(credentialObj, "ExecutePayment", postParameters.toString());
    	    	
    	// parse response
        ExecutePaymentResponse response = new ExecutePaymentResponse(responseString);
    	
    	// handle errors
    	return response;
    }

	/**
	 * @return the actionType
	 */
	public ActionType getActionType() {
		return actionType;
	}

	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	/**
	 * @return the fundingPlanId
	 */
	public String getFundingPlanId() {
		return fundingPlanId;
	}

	/**
	 * @param fundingPlanId the fundingPlanId to set
	 */
	public void setFundingPlanId(String fundingPlanId) {
		this.fundingPlanId = fundingPlanId;
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
