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
 * <p>Java class for Receiver.
 * 
 */
public class Receiver {

	protected double amount;
	protected String email;
	protected String invoiceId;
	protected PaymentType paymentType = PaymentType.SERVICE;
	protected boolean primary = false;
	protected PhoneNumberType phone;
	protected PaymentSubType paymentSubType;
	
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param invoidId the invoiceId to set
	 */
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	/**
	 * @return the invoiceId
	 */
	public String getInvoiceId() {
		return invoiceId;
	}
	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
	/**
	 * @return the paymentType
	 */
	public PaymentType getPaymentType() {
		return paymentType;
	}
	/**
	 * @param primary the primary to set
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	/**
	 * @return the primary
	 */
	public boolean isPrimary() {
		return primary;
	}
	
	public String serialize(int index) throws UnsupportedEncodingException{
		StringBuilder outString = new StringBuilder();
		if(this.email != null) {
			outString.append(ParameterUtils.createUrlParameter("receiverList.receiver(" + 
					index + ").email", this.email));
			outString.append(ParameterUtils.PARAM_SEP);
		}
		if(this.phone != null) {
			outString.append(this.phone.serialize(index));
		}
		
		if(this.amount > 0) {
			outString.append(ParameterUtils.createUrlParameter("receiverList.receiver(" + 
					index + ").amount", Double.toString(this.amount)));
			outString.append(ParameterUtils.PARAM_SEP);
		}
		if(this.primary) {
			outString.append(ParameterUtils.createUrlParameter("receiverList.receiver(" + 
					index + ").primary",Boolean.toString(this.primary)));
			outString.append(ParameterUtils.PARAM_SEP);
		}
		if(this.paymentType != PaymentType.SERVICE) {
			outString.append(ParameterUtils.createUrlParameter("receiverList.receiver(" + 
					index + ").paymentType",this.paymentType.toString()));
			outString.append(ParameterUtils.PARAM_SEP);
		}
		if(this.invoiceId != null) {
			outString.append(ParameterUtils.createUrlParameter("receiverList.receiver(" + 
				index + ").invoiceId",this.invoiceId));
			outString.append(ParameterUtils.PARAM_SEP);
		}
		if(this.paymentSubType != null) {
			outString.append(ParameterUtils.createUrlParameter("receiverList.receiver(" + 
					index + ").paymentSubType",this.paymentSubType.toString()));
			outString.append(ParameterUtils.PARAM_SEP);
		}
		return outString.toString();
	}
	
	public Receiver(){
		// default constructor
	}
	
	public Receiver(HashMap<String, String> params, int index){
			
		if(params.containsKey("error(" + index +").receiver.email")){
			this.email = params.get("error(" + index +").receiver.email");
		}
		this.phone = new PhoneNumberType(params, index);
		
		if(params.containsKey("error(" + index +").receiver.amount")){
			this.amount = Double.parseDouble(params.get("error(" + index +").receiver.amount"));
		}
		if(params.containsKey("error(" + index +").receiver.primary")){
			this.primary = Boolean.parseBoolean(params.get("error(" + index +").receiver.primary"));
		}
		if(params.containsKey("error(" + index +").receiver.paymentType")){
			this.paymentType = PaymentType.valueOf(params.get("error(" + index +").receiver.paymentType"));
		}
		if(params.containsKey("error(" + index +").receiver.invoiceId")){
			this.invoiceId = params.get("error(" + index +").receiver.invoiceId");
		}
	}

	public PhoneNumberType getPhone() {
		return phone;
	}
	public void setPhone(PhoneNumberType phone){
		this.phone = phone;
	}
	public void setPhone(double countryCode, double extension, double phonenumber) {
		this.phone = new PhoneNumberType(countryCode, extension, phonenumber);		
	}
	public void setPhone(String countryCode, String extension, String phonenumber) {
		this.phone = new PhoneNumberType(Double.parseDouble(countryCode), 
				Double.parseDouble(extension), 
				Double.parseDouble(phonenumber));		
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
