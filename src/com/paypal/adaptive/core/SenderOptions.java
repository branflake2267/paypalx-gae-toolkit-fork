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
public class SenderOptions {

	protected boolean requireShippingAddressSelection = false;
	/**
	 * 
	 */
	public SenderOptions() {
		// TODO Auto-generated constructor stub
	}
	
	public SenderOptions(HashMap<String, String> params) {
		if(params.containsKey("senderOptions.requireShippingAddressSelection")){
			this.requireShippingAddressSelection = Boolean.parseBoolean(params.get("senderOptions.requireShippingAddressSelection"));
		}
	}

	public String serialize() throws UnsupportedEncodingException{
		StringBuilder outString = new StringBuilder();
		if(this.requireShippingAddressSelection) {
			outString.append(ParameterUtils.createUrlParameter("senderOptions.requireShippingAddressSelection", Boolean.toString(this.requireShippingAddressSelection)));
		}
		return outString.toString();
	}


	/**
	 * @return the requireShippingAddressSelection
	 */
	public boolean isRequireShippingAddressSelection() {
		return requireShippingAddressSelection;
	}

	/**
	 * @param requireShippingAddressSelection the requireShippingAddressSelection to set
	 */
	public void setRequireShippingAddressSelection(
			boolean requireShippingAddressSelection) {
		this.requireShippingAddressSelection = requireShippingAddressSelection;
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
