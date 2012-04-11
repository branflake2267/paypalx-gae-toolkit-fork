
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * 
 * 						The sender identifier type contains information
 * 						to identify a PayPal account.
 * 					
 * 
 * <p>Java class for SenderIdentifier complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SenderIdentifier">
 *   &lt;complexContent>
 *     &lt;extension base="{http://svcs.paypal.com/types/common}AccountIdentifier">
 *       &lt;sequence>
 *         &lt;element name="useCredentials" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

public class SenderIdentifier {

    protected Boolean useCredentials;
    protected String email;

    /**
     * Gets the value of the useCredentials property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUseCredentials() {
        return useCredentials;
    }

    /**
     * Sets the value of the useCredentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUseCredentials(Boolean value) {
        this.useCredentials = value;
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

    public String serialize() throws UnsupportedEncodingException{
    	StringBuilder outString = new StringBuilder();
    	
    	if(this.useCredentials) {
	    	outString.append("sender.useCredentials=");
	    	outString.append(this.useCredentials);
	    	outString.append(ParameterUtils.PARAM_SEP);
    	}
    	if(this.email != null && this.email.length() > 0) {
			outString.append(ParameterUtils.createUrlParameter( "sender.email", this.email));
		}
		return outString.toString();
	}
    
    public SenderIdentifier(HashMap<String, String> params){
    	if(params.containsKey( "sender.email"))
    		this.email = params.get( "sender.email");
    	
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
