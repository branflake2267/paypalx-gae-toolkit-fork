
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;

/**
 * <p>Java class for Address.
 */
public class Address {

    protected String addresseeName;
    protected BaseAddress baseAddress;
    protected String addressId;

    
    public Address(HashMap<String, String> params, String prefix){
		
		if(params.containsKey( prefix + ".addresseeName")){
			this.addresseeName = params.get( prefix + ".addresseeName");
		}
		this.baseAddress = new BaseAddress(params, prefix); 
		if(params.containsKey( prefix + ".addressId")){
			this.addressId = params.get( prefix + ".addressId");
		}
	}
    
    /**
     * Gets the value of the addresseeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddresseeName() {
        return addresseeName;
    }

    /**
     * Sets the value of the addresseeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddresseeName(String value) {
        this.addresseeName = value;
    }

    /**
     * Gets the value of the baseAddress property.
     * 
     * @return
     *     possible object is
     *     {@link BaseAddress }
     *     
     */
    public BaseAddress getBaseAddress() {
        return baseAddress;
    }

    /**
     * Sets the value of the baseAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseAddress }
     *     
     */
    public void setBaseAddress(BaseAddress value) {
        this.baseAddress = value;
    }

	/**
	 * @return the addressId
	 */
	public String getAddressId() {
		return addressId;
	}

	/**
	 * @param addressId the addressId to set
	 */
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	
public String toString(){
		
		StringBuilder outStr = new StringBuilder();
		
		outStr.append("<table>");
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
