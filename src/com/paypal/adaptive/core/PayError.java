/**
 * 
 */
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;

/**
 * <p>Java class for PayError.
 */
public class PayError  {

	protected ErrorData error;
	protected Receiver receiver;
	
	/**
	 * @param error the error to set
	 */
	public void setError(ErrorData error) {
		this.error = error;
	}
	/**
	 * @return the error
	 */
	public ErrorData getError() {
		return error;
	}
	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}
	/**
	 * @return the receiver
	 */
	public Receiver getReceiver() {
		return receiver;
	}
	
	public PayError(HashMap<String, String> params, int index){
		error = new ErrorData(params, index);
		if(params.containsKey("error(" + index +").receiver.amount")
				|| params.containsKey("error(" + index +").receiver.email")
				|| params.containsKey("error(" + index +").receiver.paymentType")
				|| params.containsKey("error(" + index +").receiver.phone")
				|| params.containsKey("error(" + index +").receiver.primary")
				|| params.containsKey("error(" + index +").receiver.invoiceId")){
			this.receiver = new Receiver(params, index);
		}
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
