
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;


public class FundingSource {

    protected String lastFourOfAccountNumber;
    protected String type;
    protected String displayName;
    protected String fundingSourceId;
    protected boolean allowed;


    /**
     * Gets the value of the lastFourOfAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastFourOfAccountNumber() {
        return lastFourOfAccountNumber;
    }

    /**
     * Sets the value of the lastFourOfAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastFourOfAccountNumber(String value) {
        this.lastFourOfAccountNumber = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the fundingSourceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFundingSourceId() {
        return fundingSourceId;
    }

    /**
     * Sets the value of the fundingSourceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFundingSourceId(String value) {
        this.fundingSourceId = value;
    }

    /**
     * Gets the value of the allowed property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isAllowed() {
        return allowed;
    }

    /**
     * Sets the value of the allowed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAllowed(boolean value) {
        this.allowed = value;
    }
    
    public FundingSource(HashMap<String, String> params, String prefix){
    	if(params.containsKey( prefix + ".allowed")){
    		this.allowed = Boolean.parseBoolean(params.get( prefix + ".allowed" ));
    	}
    	if(params.containsKey( prefix + ".displayName")){
    		this.displayName = params.get( prefix + ".displayName" );
    	}
    	if(params.containsKey( prefix + ".fundingSourceId")){
    		this.fundingSourceId = params.get( prefix + ".fundingSourceId" );
    	}
    	if(params.containsKey( prefix + ".lastFourOfAccountNumber")){
    		this.lastFourOfAccountNumber = params.get( prefix + ".lastFourOfAccountNumber" );
    	}
    	if(params.containsKey( prefix + ".type")){
    		this.type = params.get( prefix + ".type" );
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
