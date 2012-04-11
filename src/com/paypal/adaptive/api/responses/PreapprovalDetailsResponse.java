
package com.paypal.adaptive.api.responses;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;

import com.paypal.adaptive.core.PayError;
import com.paypal.adaptive.core.PreapprovalDetails;
import com.paypal.adaptive.core.ResponseEnvelope;

/**
 * PreapprovalDetailsResponse is returned as a result of the PreapprovalDetails API operation.
 */
public class PreapprovalDetailsResponse {

	HashMap<String, String> preapprovalDetailsResponseParams;

    protected ResponseEnvelope responseEnvelope;
    protected PreapprovalDetails preapprovalDetails;
    protected ArrayList<PayError> payErrorList;

    
    public ArrayList<PayError> getPayErrorList() {
		return payErrorList;
	}


	public void setPayErrorList(ArrayList<PayError> payErrorList) {
		this.payErrorList = payErrorList;
	}


	public ResponseEnvelope getResponseEnvelope() {
		return responseEnvelope;
	}


	public void setResponseEnvelope(ResponseEnvelope responseEnvelope) {
		this.responseEnvelope = responseEnvelope;
	}


	public PreapprovalDetails getPreapprovalDetails() {
		return preapprovalDetails;
	}


	public void setPreapprovalDetails(PreapprovalDetails preapprovalDetails) {
		this.preapprovalDetails = preapprovalDetails;
	}


	/*responseEnvelope.timestamp=2010-03-26T12%3A01%3A54.779-07%3A00&responseEnvelope.ack=Success&
	 * responseEnvelope.correlationId=da19364ca27ac&responseEnvelope.build=1238639&approved=true&
	 * cancelUrl=http%3A%2F%2Flocalhost%3A8888%2Fadaptivesample%3FactionType%3Dpreapproval%26cancel%3D1&
	 * curPayments=0&curPaymentsAmount=0.00&curPeriodAttempts=0&currencyCode=USD&dateOfMonth=0&dayOfWeek=NO_DAY_SPECIFIED&
	 * endingDate=2010-03-28T11%3A37%3A56.0-07%3A00&maxTotalAmountOfAllPayments=100.00&paymentPeriod=NO_PERIOD_SPECIFIED&
	 * pinType=NOT_REQUIRED&returnUrl=http%3A%2F%2Flocalhost%3A8888%2Fadaptivesample%3Freturn%3D1%26actionType%3Dpreapproval
	 * %26preapprovalKey%3D%24%7BpreapprovalKey%7D
	 * &senderEmail=ppalav_1260941775_per%40yahoo.com
	 * &startingDate=2010-03-26T12%3A37%3A56.0-07%3A00&status=ACTIVE&addressList.address(0).addresseeName=&
	 * addressList.address(0).baseAddress.line1=1+Main+St&addressList.address(0).baseAddress.line2=&
	 * addressList.address(0).baseAddress.city=San+Jose&addressList.address(0).baseAddress.state=CA&
	 * addressList.address(0).baseAddress.postalCode=95131&addressList.address(0).baseAddress.countryCode=US&
	 * addressList.address(0).baseAddress.type=BILLING
	*/
	public PreapprovalDetailsResponse(String responseString){
    	
    	preapprovalDetailsResponseParams = new HashMap<String, String>();
    	// parse the string and load into the object
    	String[] nmValPairs = responseString.split("&");
    	for(String nmVal: nmValPairs){
    		String[] field = nmVal.split("=");
    		preapprovalDetailsResponseParams.put(field[0], (field.length > 1)?field[1].trim():"");
    	}

    	responseEnvelope = new ResponseEnvelope(preapprovalDetailsResponseParams);
    	
    	preapprovalDetails = new PreapprovalDetails(preapprovalDetailsResponseParams);
    	
    	// we will parse 10 errors for now
    	for(int i = 0; i < 10; i++){
    		if(preapprovalDetailsResponseParams.containsKey("error(" + i +").errorId")){
    			PayError pErr = new PayError( preapprovalDetailsResponseParams, i);
    			payErrorList.add(pErr);
    		} else {
    			break;
    		}
    	}
    	
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
