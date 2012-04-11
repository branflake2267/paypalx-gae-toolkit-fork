/**
 * 
 */
package com.paypal.adaptive.api.responses;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;

import com.paypal.adaptive.core.PayError;
import com.paypal.adaptive.core.PaymentOptions;
import com.paypal.adaptive.core.ResponseEnvelope;


/**
 * PaymentDetailsResponse is returned as a result of the PaymentDetails API operation..
 */
public class GetPaymentOptionsResponse {

	HashMap<String, String> paymentOptionsResponseParams;
	protected ResponseEnvelope responseEnvelope;

	protected PaymentOptions paymentOptions;
	protected ArrayList<PayError> payErrorList;


	public ArrayList<PayError> getPayErrorList() {
		return payErrorList;
	}


	public GetPaymentOptionsResponse(String responseString){

		paymentOptionsResponseParams = new HashMap<String, String>();
		// parse the string and load into the object
		String[] nmValPairs = responseString.split("&");
		for(String nmVal: nmValPairs){
			String[] field = nmVal.split("=");
			paymentOptionsResponseParams.put(field[0], (field.length > 1)?field[1].trim():"");
		}

		responseEnvelope = new ResponseEnvelope(paymentOptionsResponseParams);

		paymentOptions = new PaymentOptions(paymentOptionsResponseParams);

		payErrorList = new ArrayList<PayError>();
		// we will parse 10 errors for now
		for(int i = 0; i < 10; i++){
			if(paymentOptionsResponseParams.containsKey("error(" + i +").errorId")){
				PayError pErr = new PayError( paymentOptionsResponseParams, i);
				payErrorList.add(pErr);
			} else {
				break;
			}
		}
	}


	public ResponseEnvelope getResponseEnvelope() {
		return responseEnvelope;
	}


	public PaymentOptions getPaymentOptions() {
		return paymentOptions;
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
