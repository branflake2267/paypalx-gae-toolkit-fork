
package com.paypal.adaptive.api.responses;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;

import com.paypal.adaptive.core.AckCode;
import com.paypal.adaptive.core.Address;
import com.paypal.adaptive.core.PayError;
import com.paypal.adaptive.core.ResponseEnvelope;
import com.paypal.adaptive.exceptions.PayPalErrorException;

/**
 * ConvertCurrencyResponse is returned as a result of the ConvertCurrency API operation.
 */
public class GetShippingAddressesResponse {

	HashMap<String, String> getShippingAddressesResponseParams;
	protected ResponseEnvelope responseEnvelope;
	protected ArrayList<PayError> errorList;
	protected Address selectedAddress;
	
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



    public GetShippingAddressesResponse(String responseString) throws PayPalErrorException{
    	
    	getShippingAddressesResponseParams = new HashMap<String, String>();
    	// parse the string and load into the object
    	String[] nmValPairs = responseString.split("&");
    	for(String nmVal: nmValPairs){
    		String[] field = nmVal.split("=");
    		getShippingAddressesResponseParams.put(field[0], (field.length > 1)?field[1].trim():"");
    	}

    	responseEnvelope = new ResponseEnvelope(getShippingAddressesResponseParams);
    	    	
    	errorList = new ArrayList<PayError>();
    	// we will parse 10 errors for now
    	for(int i = 0; i < 10; i++){
    		if(getShippingAddressesResponseParams.containsKey("error(" + i +").errorId")){
    			PayError error = new PayError(getShippingAddressesResponseParams, i);
    			errorList.add(error);
    		} else {
    			break;
    		}
    	}
    	// parse Address
    	if(getShippingAddressesResponseParams.containsKey("selectedAddress.addresseeName")
    			|| getShippingAddressesResponseParams.containsKey("selectedAddress.addressId")){
    		this.selectedAddress = new Address(getShippingAddressesResponseParams, "selectedAddress");
    	}
    	
    }

	/**
	 * @return the selectedAddress
	 */
	public Address getSelectedAddress() {
		return selectedAddress;
	}

	/**
	 * @param selectedAddress the selectedAddress to set
	 */
	public void setSelectedAddress(Address selectedAddress) {
		this.selectedAddress = selectedAddress;
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
