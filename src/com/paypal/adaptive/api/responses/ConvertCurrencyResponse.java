
package com.paypal.adaptive.api.responses;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;

import com.paypal.adaptive.core.AckCode;
import com.paypal.adaptive.core.CurrencyConversionList;
import com.paypal.adaptive.core.PayError;
import com.paypal.adaptive.core.ResponseEnvelope;
import com.paypal.adaptive.exceptions.PayPalErrorException;

/**
 * ConvertCurrencyResponse is returned as a result of the ConvertCurrency API operation.
 */
public class ConvertCurrencyResponse {

	HashMap<String, String> convertCurrencyResponseParams;
	protected ResponseEnvelope responseEnvelope;
	protected ArrayList<PayError> errorList;
	protected ArrayList<CurrencyConversionList> estimatedAmountTable;
	
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

    /**
     * Sets the value of the responseEnvelope property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseEnvelope }
     *     
     */
    public void setResponseEnvelope(ResponseEnvelope value) {
        this.responseEnvelope = value;
    }

    public ArrayList<PayError> getErrorList() {
		return errorList;
	}

	public void setErrorList(ArrayList<PayError> errorList) {
		this.errorList = errorList;
	}


    public ConvertCurrencyResponse(String responseString) throws PayPalErrorException{
    	
    	convertCurrencyResponseParams = new HashMap<String, String>();
    	// parse the string and load into the object
    	String[] nmValPairs = responseString.split("&");
    	for(String nmVal: nmValPairs){
    		String[] field = nmVal.split("=");
    		convertCurrencyResponseParams.put(field[0], (field.length > 1)?field[1].trim():"");
    	}

    	responseEnvelope = new ResponseEnvelope(convertCurrencyResponseParams);
    	    	
    	errorList = new ArrayList<PayError>();
    	// we will parse 10 errors for now
    	for(int i = 0; i < 10; i++){
    		if(convertCurrencyResponseParams.containsKey("error(" + i +").errorId")){
    			PayError error = new PayError(convertCurrencyResponseParams, i);
    			errorList.add(error);
    		} else {
    			break;
    		}
    	}
    	/*
    	 *  &estimatedAmountTable.currencyConversionList(0).baseAmount.code=GBP
    	 * &estimatedAmountTable.currencyConversionList(0).baseAmount.amount=1.0
		 * &estimatedAmountTable.currencyConversionList(0).currencyList.currency(0).code=JPY
		 * &estimatedAmountTable.currencyConversionList(0).currencyList.currency(0).amount=231
		 * &estimatedAmountTable.currencyConversionList(1).baseAmount.code=EUR
		 * &estimatedAmountTable.currencyConversionList(1).baseAmount.amount=100.0
		 * &estimatedAmountTable.currencyConversionList(1).currencyList.currency(0).code=JPY
		 * &estimatedAmountTable.currencyConversionList(1).currencyList.currency(0).amount=15764
	   	 */
    	// parse estimatedAmountTable objects
    	for(int i=0; i<10;i++){
    		if(convertCurrencyResponseParams.containsKey(
    				"estimatedAmountTable.currencyConversionList(" + i + ").baseAmount.code")){
    			CurrencyConversionList convList = new CurrencyConversionList(convertCurrencyResponseParams, i);
    			this.addToEstimatedAmountTable(convList);
    		} else {
    			break;
    		}
    	}
    	
    }

	public ArrayList<CurrencyConversionList> getEstimatedAmountTable() {
		return estimatedAmountTable;
	}

	public void setEstimatedAmountTable(
			ArrayList<CurrencyConversionList> estimatedAmountTable) {
		this.estimatedAmountTable = estimatedAmountTable;
	}
	
	public void addToEstimatedAmountTable(CurrencyConversionList convList){
		if(this.estimatedAmountTable == null){
			this.estimatedAmountTable = new ArrayList<CurrencyConversionList>();
		}
		this.estimatedAmountTable.add(convList);
	}

public String toString(){
		
		StringBuilder outStr = new StringBuilder();
		
		outStr.append("<table border=1 width=100%>");
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
