/**
 * 
 */
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;

/**
 * <p>Java class for ClientDetails.
 */
public class ClientDetails {

	protected String ipAddress;
    protected String deviceId;
    protected String applicationId;
    protected String model;
    protected String geoLocation;
    protected String customerType;
    protected String partnerName;
    protected String customerId;
    
    /**
     * Gets the value of the ipAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the value of the ipAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpAddress(String value) {
        this.ipAddress = value;
    }

    /**
     * Gets the value of the deviceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Sets the value of the deviceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceId(String value) {
        this.deviceId = value;
    }

    /**
     * Gets the value of the applicationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * Sets the value of the applicationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationId(String value) {
        this.applicationId = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModel(String value) {
        this.model = value;
    }

    /**
     * Gets the value of the geoLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeoLocation() {
        return geoLocation;
    }

    /**
     * Sets the value of the geoLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeoLocation(String value) {
        this.geoLocation = value;
    }

    /**
     * Gets the value of the customerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerType() {
        return customerType;
    }

    /**
     * Sets the value of the customerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerType(String value) {
        this.customerType = value;
    }

    /**
     * Gets the value of the partnerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartnerName() {
        return partnerName;
    }

    /**
     * Sets the value of the partnerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartnerName(String value) {
        this.partnerName = value;
    }

    /**
     * Gets the value of the customerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the value of the customerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerId(String value) {
        this.customerId = value;
    }
    
	
	public String serialize() throws UnsupportedEncodingException{
		StringBuilder outString = new StringBuilder();
		boolean isFirst = true;
		if(this.applicationId != null) {
			outString.append(ParameterUtils.createUrlParameter("clientDetails.applicationId", this.applicationId));
			isFirst = false;
		}
		
		
		if(this.ipAddress != null) {
			if(!isFirst)
				outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter("clientDetails.ipAddress",this.ipAddress));
			isFirst = false;
		}
		
		if(this.deviceId != null) {
			if(!isFirst)
				outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter("clientDetails.deviceId", this.deviceId));
			isFirst = false;
		}
		
		if(this.customerId != null) {
			if(!isFirst)
				outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter("clientDetails.customerId", this.customerId));
			isFirst = false;
		}
		
		if(this.customerType != null) {
			if(!isFirst)
				outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter("clientDetails.customerType",this.customerType));
			isFirst = false;
		}
		
		if(this.geoLocation != null) {
			if(!isFirst)
				outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter("clientDetails.geoLocation",this.geoLocation));
			isFirst = false;
		}
		
		if(this.model != null) {
			if(!isFirst)
				outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter("clientDetails.model",this.model));
			isFirst = false;
		}
		
		if(this.partnerName != null) {
			if(!isFirst)
				outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter("clientDetails.partnerName",this.partnerName));
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
}
