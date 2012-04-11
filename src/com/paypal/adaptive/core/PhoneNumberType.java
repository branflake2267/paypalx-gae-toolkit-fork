package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * <p>Java class for PhoneNumberType.
 * 
 */
public class PhoneNumberType {

	protected double countryCode = -1;
	protected double extension = -1;
	protected double phoneNumber = -1;
	
	public double getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(double countryCode) {
		this.countryCode = countryCode;
	}
	public double getExtension() {
		return extension;
	}
	public void setExtension(double extension) {
		this.extension = extension;
	}
	public double getPhonenumber() {
		return phoneNumber;
	}
	public void setPhonenumber(double phonenumber) {
		this.phoneNumber = phonenumber;
	}
	
	PhoneNumberType(double countryCode, double extension, double phonenumber){
		this.countryCode = countryCode;
		this.extension = extension;
		this.phoneNumber = phonenumber;
	}
	
	PhoneNumberType(HashMap<String, String> params, int index){
		if(params.containsKey("error(" + index +").receiver.phone.countryCode")){
			this.countryCode = Double.parseDouble(params.get("error(" + index +").receiver.phone.countryCode"));
		}
		if(params.containsKey("error(" + index +").receiver.phone.extension")){
			this.extension = Double.parseDouble(params.get("error(" + index +").receiver.phone.extension"));
		}
		if(params.containsKey("error(" + index +").receiver.phone.phonenumber")){
			this.countryCode = Double.parseDouble(params.get("error(" + index +").receiver.phone.phonenumber"));
		}
	}
	
	PhoneNumberType(HashMap<String, String> params, String prefix){
		if(params.containsKey( prefix + ".countryCode")){
			this.countryCode = Double.parseDouble(params.get( prefix + ".countryCode"));
		}
		if(params.containsKey( prefix + ".extension")){
			this.extension = Double.parseDouble(params.get( prefix + ".extension"));
		}
		if(params.containsKey( prefix + ".phonenumber")){
			this.countryCode = Double.parseDouble(params.get( prefix +  ".phonenumber"));
		}
	}
	
	public String serialize(int index) throws UnsupportedEncodingException{
		StringBuilder outString = new StringBuilder();
		if(this.countryCode != -1) {
			outString.append(ParameterUtils.createUrlParameter("receiverList.receiver(" + 
					index + ").phone.countryCode", Double.toString(this.countryCode)));
			outString.append(ParameterUtils.PARAM_SEP);
		}
		if(this.extension != -1) {
			outString.append(ParameterUtils.createUrlParameter("receiverList.receiver(" + 
					index + ").phone.extension", Double.toString(this.extension)));
			outString.append(ParameterUtils.PARAM_SEP);
		}
		if(this.phoneNumber != -1) {
			outString.append(ParameterUtils.createUrlParameter("receiverList.receiver(" + 
					index + ").phone.phonenumber", Double.toString(this.phoneNumber)));
			outString.append(ParameterUtils.PARAM_SEP);
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
