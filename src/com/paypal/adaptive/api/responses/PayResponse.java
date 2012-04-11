
package com.paypal.adaptive.api.responses;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;

import com.paypal.adaptive.core.FundingPlan;
import com.paypal.adaptive.core.PayError;
import com.paypal.adaptive.core.PaymentExecStatus;
import com.paypal.adaptive.core.ResponseEnvelope;


/**
 * PayResponse is returned as a result of the Pay API operation.
 */
public class PayResponse {

	HashMap<String, String> payResponseParams;
	protected ResponseEnvelope responseEnvelope;
	protected String payKey;
	protected PaymentExecStatus paymentExecStatus;
	protected ArrayList<PayError> payErrorList;
	protected FundingPlan defaultFundingPlan;
    
    public PayResponse(String responseString) {
    	
    	payResponseParams = new HashMap<String, String>();
    	// parse the string and load into the object
    	String[] nmValPairs = responseString.split("&");
    	for(String nmVal: nmValPairs){
    		String[] field = nmVal.split("=");
    		payResponseParams.put(field[0].trim(), (field.length > 1)?field[1].trim():"");
    	}

    	if(payResponseParams.containsKey("payKey")){
    		this.payKey = payResponseParams.get("payKey");
    	}
    	
    	if(payResponseParams.containsKey("paymentExecStatus")){
    		this.paymentExecStatus = PaymentExecStatus.valueOf(payResponseParams.get("paymentExecStatus"));
    	}
    	responseEnvelope = new ResponseEnvelope(payResponseParams);
    	
    	payErrorList = new ArrayList<PayError>();
    	// we will parse 10 errors at max for now
    	for(int i = 0; i < 10; i++){
    		if(payResponseParams.containsKey("error(" + i +").errorId")){
    			PayError pErr = new PayError( payResponseParams, i);
    			payErrorList.add(pErr);
    		} else {
    			break;
    		}
    	}
    	
    	// load defaultfundingPlan when it's available
    	if(paymentExecStatus != null 
    			&& paymentExecStatus == PaymentExecStatus.COMPLETED
    				|| paymentExecStatus == PaymentExecStatus.PROCESSING
    				|| paymentExecStatus == PaymentExecStatus.PENDING){
    			// no further action required so treat these as success
    			// load defaultFundingPlan
    			this.defaultFundingPlan = new FundingPlan(payResponseParams, "defaultFundingPlan");
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
	 * @param payKey the payKey to set
	 */
	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}
	/**
	 * @return the payKey
	 */
	public String getPayKey() {
		return payKey;
	}
	/**
	 * @param paymentExecStatus the paymentExecStatus to set
	 */
	public void setPaymentExecStatus(PaymentExecStatus paymentExecStatus) {
		this.paymentExecStatus = paymentExecStatus;
	}
	/**
	 * @return the paymentExecStatus
	 */
	public PaymentExecStatus getPaymentExecStatus() {
		return paymentExecStatus;
	}
	/**
	 * @param payErrorList the payErrorList to set
	 */
	public void setPayErrorList(ArrayList<PayError> payErrorList) {
		this.payErrorList = payErrorList;
	}
	
	/**
	 * @param errorList the payErrorList to set
	 */
	public void addToPayErrorList(PayError payError) {
		if(this.payErrorList == null)
			this.payErrorList = new ArrayList<PayError>();
		
		this.payErrorList.add(payError);
	}
	
	/**
	 * @return the payErrorList
	 */
	public ArrayList<PayError> getPayErrorList() {
		return payErrorList;
	}
    
	/**
	 * @return the defaultFundingPlan
	 */
	public FundingPlan getDefaultFundingPlan() {
		return defaultFundingPlan;
	}
	/**
	 * @param defaultFundingPlan the defaultFundingPlan to set
	 */
	public void setDefaultFundingPlan(FundingPlan defaultFundingPlan) {
		this.defaultFundingPlan = defaultFundingPlan;
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
