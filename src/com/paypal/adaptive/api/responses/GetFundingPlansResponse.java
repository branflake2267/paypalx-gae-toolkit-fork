
package com.paypal.adaptive.api.responses;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;

import com.paypal.adaptive.core.AckCode;
import com.paypal.adaptive.core.FundingPlan;
import com.paypal.adaptive.core.PayError;
import com.paypal.adaptive.core.ResponseEnvelope;
import com.paypal.adaptive.exceptions.PayPalErrorException;

/**
 * ConvertCurrencyResponse is returned as a result of the ConvertCurrency API operation.
 */
public class GetFundingPlansResponse {

	HashMap<String, String> getFundingPlansResponseParams;
	protected ResponseEnvelope responseEnvelope;
	protected ArrayList<PayError> errorList;
	protected ArrayList<FundingPlan> fundingPlan;
	
    /**
     * Gets the value of the responseEnvelope property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseEnvelope }
     *     
     */
    public ResponseEnvelope getResponseEnvelope() {
        return responseEnvelope;
    }

    public ArrayList<PayError> getErrorList() {
		return errorList;
	}



    public GetFundingPlansResponse(String responseString){
    	
    	getFundingPlansResponseParams = new HashMap<String, String>();
    	// parse the string and load into the object
    	String[] nmValPairs = responseString.split("&");
    	for(String nmVal: nmValPairs){
    		String[] field = nmVal.split("=");
    		getFundingPlansResponseParams.put(field[0], (field.length > 1)?field[1].trim():"");
    	}

    	responseEnvelope = new ResponseEnvelope(getFundingPlansResponseParams);
    	    	
    	errorList = new ArrayList<PayError>();
    	// we will parse 10 errors for now
    	for(int i = 0; i < 10; i++){
    		if(getFundingPlansResponseParams.containsKey("error(" + i +").errorId")){
    			PayError error = new PayError(getFundingPlansResponseParams, i);
    			errorList.add(error);
    		} else {
    			break;
    		}
    	}
    	// parse fundingPlans
    	for(int i=0; i<10;i++){
    		if(getFundingPlansResponseParams.containsKey(
    				"fundingPlan(" + i + ").fundingPlanId")){
    			FundingPlan convList = new FundingPlan(getFundingPlansResponseParams, "fundingPlan(" + i + ")");
    			this.addToFundingPlan(convList);
    		} else {
    			break;
    		}
    	}
    	
    }

	public ArrayList<FundingPlan> getFundingPlan() {
		return fundingPlan;
	}

	public void setFundingPlan(
			ArrayList<FundingPlan> fundingPlan) {
		this.fundingPlan = fundingPlan;
	}
	
	public void addToFundingPlan(FundingPlan fundingPlan){
		if(this.fundingPlan == null){
			this.fundingPlan = new ArrayList<FundingPlan>();
		}
		this.fundingPlan.add(fundingPlan);
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
