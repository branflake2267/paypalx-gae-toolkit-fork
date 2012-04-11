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
public class ReceiverOptions {

	protected String description;
	protected String customId;
	protected InvoiceData invoiceData;
	protected ReceiverIdentifier receiver;
	/**
	 * 
	 */
	public ReceiverOptions() {
		// TODO Auto-generated constructor stub
	}

	public ReceiverOptions(HashMap<String, String> params, int index) {
		if(params.containsKey("receiverOptions("+index+").description")){
			this.description = params.get("receiverOptions("+index+").description");
		}
		if(params.containsKey("receiverOptions("+index+").customId")){
			this.customId = params.get("receiverOptions("+index+").customId");
		}
		this.invoiceData = new InvoiceData(params, "receiverOptions("+index+")");
		this.receiver = new ReceiverIdentifier(params, "receiverOptions("+index+")");
	}

	public String serialize(int index) throws UnsupportedEncodingException{
		StringBuilder outString = new StringBuilder();
		
		boolean isFirst = true;
		if(this.receiver != null ) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(this.receiver.serialize("receiverOptions("+index+")"));
			isFirst = false;
		}
		if(this.description != null && this.description.length() > 0) {
			outString.append(ParameterUtils.createUrlParameter("receiverOptions("+index+").description", this.description));
			isFirst = false;
		}
		if(this.customId != null && this.customId.length() > 0) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter( "receiverOptions("+index+").customId", this.customId));
			isFirst = false;
		}
		if(this.invoiceData != null) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(this.invoiceData.serialize("receiverOptions("+index+")"));
			isFirst = false;
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

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the customId
	 */
	public String getCustomId() {
		return customId;
	}

	/**
	 * @param customId the customId to set
	 */
	public void setCustomId(String customId) {
		this.customId = customId;
	}

	/**
	 * @return the invoiceData
	 */
	public InvoiceData getInvoiceData() {
		return invoiceData;
	}

	/**
	 * @param invoiceData the invoiceData to set
	 */
	public void setInvoiceData(InvoiceData invoiceData) {
		this.invoiceData = invoiceData;
	}

	/**
	 * @return the receiver
	 */
	public ReceiverIdentifier getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(ReceiverIdentifier receiver) {
		this.receiver = receiver;
	}

}
