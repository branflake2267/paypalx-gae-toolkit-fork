
package com.paypal.adaptive.api.responses;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;

import com.paypal.adaptive.core.AckCode;
import com.paypal.adaptive.core.PayError;
import com.paypal.adaptive.core.ResponseEnvelope;
import com.paypal.adaptive.exceptions.AuthorizationRequiredException;
import com.paypal.adaptive.exceptions.PayPalErrorException;


/**
 * PreapprovalResponse is returned as a result of the Preapproval API operation.
 */
public class PreapprovalResponse {

	HashMap<String, String> payResponseParams;
	protected ResponseEnvelope responseEnvelope;
	protected String preapprovalKey;
	protected ArrayList<PayError> errorList;
    
    public PreapprovalResponse(String responseString){
    	
    	payResponseParams = new HashMap<String, String>();
    	// parse the string and load into the object
    	String[] nmValPairs = responseString.split("&");
    	for(String nmVal: nmValPairs){
    		String[] field = nmVal.split("=");
    		payResponseParams.put(field[0], (field.length > 1)?field[1].trim():"");
    	}

    	if(payResponseParams.containsKey("preapprovalKey")){
    		this.preapprovalKey = payResponseParams.get("preapprovalKey");
    	}

    	responseEnvelope = new ResponseEnvelope(payResponseParams);
    	
    	errorList = new ArrayList<PayError>();
    	// we will parse 10 errors for now
    	for(int i = 0; i < 10; i++){
    		if(payResponseParams.containsKey("error(" + i +").errorId")){
    			PayError pErr = new PayError( payResponseParams, i);
    			errorList.add(pErr);
    		} else {
    			break;
    		}
    	}
    	
    }
	/**
	 * @param responseEnvelope the responseEnvelope to set
	 */
	public void setResponseEnvelope(ResponseEnvelope responseEnvelope) {
		this.responseEnvelope = responseEnvelope;
	}
	/**
	 * @return the responseEnvelope
	 */
	public ResponseEnvelope getResponseEnvelope() {
		return responseEnvelope;
	}

	/**
	 * @param errorList the errorList to set
	 */
	public void seErrorList(ArrayList<PayError> errorList) {
		this.errorList = errorList;
	}
	
	/**
	 * @param errorList the payErrorList to set
	 */
	public void addToErrorList(ArrayList<PayError> errorList) {
		if(this.errorList == null)
			this.errorList = new ArrayList<PayError>();
		
		this.errorList = errorList;
	}
	
	/**
	 * @return the errorList
	 */
	public ArrayList<PayError> getErrorList() {
		return errorList;
	}
	public String getPreapprovalKey() {
		return preapprovalKey;
	}
	public void setPreapprovalKey(String preapprovalKey) {
		this.preapprovalKey = preapprovalKey;
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
