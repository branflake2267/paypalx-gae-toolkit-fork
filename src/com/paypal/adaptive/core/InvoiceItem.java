/**
 * 
 */
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author palavilli
 *
 */
public class InvoiceItem {

	protected String name;
	protected String identifier;
	protected String price;
	protected String itemPrice;
    protected int itemCount = -1;
	/**
	 * 
	 */
	public InvoiceItem() {
		// TODO Auto-generated constructor stub
	}
	
	public InvoiceItem(HashMap<String, String> params, String prefix, int index) {
		if(params.containsKey(prefix + "(" + index + ").name")){
			this.name = params.get(prefix + "(" + index + ").name");
		}
		if(params.containsKey(prefix + "(" + index + ").identifier")){
			this.identifier = params.get(prefix + "(" + index + ").identifier");
		}
		if(params.containsKey(prefix + "(" + index + ").price")){
			this.price = params.get(prefix + "(" + index + ").price");
		}
		if(params.containsKey(prefix + "(" + index + ").itemPrice")){
			this.itemPrice = params.get(prefix + "(" + index + ").itemPrice");
		}
		if(params.containsKey(prefix + "(" + index + ").itemCount")){
			this.itemCount = Integer.parseInt(params.get(prefix + "(" + index + ").itemCount"));
		}
	}

	/**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the price property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrice(String value) {
        this.price = value;
    }

    /**
     * Gets the value of the itemPrice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemPrice() {
        return itemPrice;
    }

    /**
     * Sets the value of the itemPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setItemPrice(String value) {
        this.itemPrice = value;
    }

    /**
     * Gets the value of the itemCount property.
     * 
     * @return
     *     possible object is
     *     {@link int }
     *     
     */
    public int getItemCount() {
        return itemCount;
    }

    /**
     * Sets the value of the itemCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link int }
     *     
     */
    public void setItemCount(int value) {
        this.itemCount = value;
    }
    
	public Object serialize(String prefix, int index)  throws UnsupportedEncodingException{
		StringBuilder outString = new StringBuilder();
		boolean isFirst = true;
		if(this.name != null && this.name.length() > 0) {
			outString.append(ParameterUtils.createUrlParameter( prefix + "(" + index + ").name", this.name));
			isFirst = false;
		}
		if(this.identifier != null && this.identifier.length() > 0) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter( prefix + "(" + index + ").identifier", this.identifier));
			isFirst = false;
		}
		
		if(this.price != null && this.price.length() > 0) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter( prefix + "(" + index + ").price", this.price));
			isFirst = false;
		}
		
		if(this.itemPrice != null && this.itemPrice.length() > 0) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter( prefix + "(" + index + ").itemPrice", this.itemPrice));
			isFirst = false;
		}
		
		if(this.itemCount != -1 ) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter( prefix + "(" + index + ").itemCount", Integer.toString(this.itemCount)));
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
