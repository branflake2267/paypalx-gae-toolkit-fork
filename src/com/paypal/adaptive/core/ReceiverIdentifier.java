/**
 * 
 */
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * @author palavilli
 *
 */
public class ReceiverIdentifier {
	
	 protected String email;
	 protected PhoneNumberType phone;

	/**
	 * 
	 */
	public ReceiverIdentifier() {
		// TODO Auto-generated constructor stub\
		super();
	}
	
	public ReceiverIdentifier(HashMap<String, String> params, String prefix) {
		if(params.containsKey( prefix + ".email"))
    		this.email = params.get( prefix + ".email");
    	if(params.containsKey( prefix + ".phone.phoneNumber")) {
    		this.phone = new PhoneNumberType(params, prefix);
    	}
	}

	/**
     * Gets the value of the phone property.
     * 
     * @return
     *     possible object is
     *     {@link PhoneNumberType }
     *     
     */
    public PhoneNumberType getPhone() {
        return phone;
    }

    /**
     * Sets the value of the phone property.
     * 
     * @param value
     *     allowed object is
     *     {@link PhoneNumberType }
     *     
     */
    public void setPhone(PhoneNumberType value) {
        this.phone = value;
    }
    
    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }
	
	public String serialize(String prefix) throws UnsupportedEncodingException{
		StringBuilder outString = new StringBuilder();
		boolean isFirst = true;
		if(this.email != null && this.email.length() > 0) {
			outString.append(ParameterUtils.createUrlParameter( prefix + ".receiver.email", this.email));
			isFirst = false;
		}
		if(this.phone != null && this.phone.countryCode != -1) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter( prefix + ".receiver.phone.countryCode", Double.toString(this.phone.countryCode)));
			isFirst = false;
		}
		if(this.phone != null && this.phone.phoneNumber != -1) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter( prefix + ".receiver.phone.phoneNumber", Double.toString(this.phone.phoneNumber)));
			isFirst = false;
		}
		if(this.phone != null && this.phone.extension != -1) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter( prefix + ".receiver.phone.extension", Double.toString(this.phone.extension)));
		}
		return outString.toString();
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
