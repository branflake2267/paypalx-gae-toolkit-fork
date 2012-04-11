
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;

/**
 * 
 * 						Amount to be charged to a particular funding
 * 						source.
 * 					
 * 
 * <p>Java class for FundingPlanCharge complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FundingPlanCharge">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="charge" type="{http://svcs.paypal.com/types/common}CurrencyType"/>
 *         &lt;element name="fundingSource" type="{http://svcs.paypal.com/types/ap}FundingSource"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

public class FundingPlanCharge {

    protected CurrencyType charge;
    protected FundingSource fundingSource;

    public FundingPlanCharge(HashMap<String, String> params, String prefix) {
		// TODO Auto-generated constructor stub
    	if(params.containsKey( prefix + ".charge.code") 
    			&& params.containsKey( prefix + ".charge.amount") ) {
    		this.charge = new CurrencyType(
    				CurrencyCodes.valueOf(params.get(prefix + ".charge.code")),
    				Double.parseDouble(params.get(prefix + ".charge.amount")));
    	}
    	if(params.containsKey( prefix + ".fundingSource.fundingSourceId")) {
    		this.fundingSource = new FundingSource(params, prefix + ".fundingSource" );
    	}
	}

	/**
     * Gets the value of the charge property.
     * 
     * @return
     *     possible object is
     *     {@link CurrencyType }
     *     
     */
    public CurrencyType getCharge() {
        return charge;
    }

    /**
     * Sets the value of the charge property.
     * 
     * @param value
     *     allowed object is
     *     {@link CurrencyType }
     *     
     */
    public void setCharge(CurrencyType value) {
        this.charge = value;
    }

    /**
     * Gets the value of the fundingSource property.
     * 
     * @return
     *     possible object is
     *     {@link FundingSource }
     *     
     */
    public FundingSource getFundingSource() {
        return fundingSource;
    }

    /**
     * Sets the value of the fundingSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link FundingSource }
     *     
     */
    public void setFundingSource(FundingSource value) {
        this.fundingSource = value;
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
