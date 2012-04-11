
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;

/**
 * 
 * 						Describes the conversion between 2 currencies.
 * 					
 * 
 * <p>Java class for CurrencyConversion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CurrencyConversion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="from" type="{http://svcs.paypal.com/types/common}CurrencyType"/>
 *         &lt;element name="to" type="{http://svcs.paypal.com/types/common}CurrencyType"/>
 *         &lt;element name="exchangeRate" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

public class CurrencyConversion {

    protected CurrencyType from;
    protected CurrencyType to;
    protected double exchangeRate;
   
    /**
     * Gets the value of the from property.
     * 
     * @return
     *     possible object is
     *     {@link CurrencyType }
     *     
     */
    public CurrencyType getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     * 
     * @param value
     *     allowed object is
     *     {@link CurrencyType }
     *     
     */
    public void setFrom(CurrencyType value) {
        this.from = value;
    }

    /**
     * Gets the value of the to property.
     * 
     * @return
     *     possible object is
     *     {@link CurrencyType }
     *     
     */
    public CurrencyType getTo() {
        return to;
    }

    /**
     * Sets the value of the to property.
     * 
     * @param value
     *     allowed object is
     *     {@link CurrencyType }
     *     
     */
    public void setTo(CurrencyType value) {
        this.to = value;
    }

    /**
     * Gets the value of the exchangeRate property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public double getExchangeRate() {
        return exchangeRate;
    }

    /**
     * Sets the value of the exchangeRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setExchangeRate(double value) {
        this.exchangeRate = value;
    }

    public CurrencyConversion(HashMap<String, String> params, String prefix){
    	if(params.containsKey( prefix + ".from.code") 
    			&& params.containsKey( prefix + ".from.amount") ){
    		this.from = new CurrencyType(
    				CurrencyCodes.valueOf(params.get(prefix + ".from.amount")),
    				Double.parseDouble(params.get(prefix + ".from.amount")));
    	}
    	if(params.containsKey( prefix + ".to.code") 
    			&& params.containsKey( prefix + ".to.amount") ){
    		this.to = new CurrencyType(
    				CurrencyCodes.valueOf(params.get(prefix + ".to.amount")),
    				Double.parseDouble(params.get(prefix + ".to.amount")));
    	}
    	if(params.containsKey( prefix + ".exchangeRate")){
    		this.exchangeRate = Double.parseDouble(params.get( prefix + ".exchangeRate" ));
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
