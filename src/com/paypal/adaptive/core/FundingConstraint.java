
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;



/**
 * <p>Java class for FundingConstraint complex type.
 * 
 */

public class FundingConstraint {

	protected FundingType fundingType;

	/**
	 * Gets the value of the fundingType property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public FundingType getFundingType() {
	    return fundingType;
	}

	/**
	 * Sets the value of the fundingType property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setFundingType(FundingType value) {
	    this.fundingType = value;
	}
	
	public String serialize(int index) throws UnsupportedEncodingException{
		// prepare request parameters
    	return ParameterUtils.createUrlParameter("fundingConstraint.allowedFundingType.fundingTypeInfo(" + index + ").fundingType", this.fundingType.toString());
	}
	
	public FundingConstraint(HashMap<String, String> params, int index){
		if(params.containsKey("fundingConstraint.allowedFundingType.fundingTypeInfo(" + index + ").fundingType")) {
			this.fundingType = FundingType.valueOf(params.get("fundingConstraint.allowedFundingType.fundingTypeInfo(" + index + ").fundingType"));
		}
	}

	public FundingConstraint(FundingType fundingType){
		this.fundingType = fundingType;
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
