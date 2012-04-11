
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>Java class for CurrencyConversion List 
 */
public class CurrencyConversionList{

    protected CurrencyType baseAmount;
    protected ArrayList<CurrencyType> currencyList;

    /**
     * Gets the value of the baseAmount property.
     * 
     * @return
     *     possible object is
     *     {@link CurrencyType }
     *     
     */
    public CurrencyType getBaseAmount() {
        return baseAmount;
    }

    /**
     * Sets the value of the baseAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link CurrencyType }
     *     
     */
    public void setBaseAmount(CurrencyType value) {
        this.baseAmount = value;
    }

    /**
     * Gets the value of the currencyList property.
     * 
     * @return
     *     possible object is
     *     {@link CurrencyList }
     *     
     */
    public ArrayList<CurrencyType> getCurrencyList() {
        return currencyList;
    }

    /**
     * Sets the value of the currencyList property.
     * 
     * @param value
     *     allowed object is
     *     {@link CurrencyList }
     *     
     */
    public void setCurrencyList(ArrayList<CurrencyType> value) {
        this.currencyList = value;
    }

    public void addToCurrencyList(CurrencyType currType){
		if(this.currencyList == null){
			this.currencyList = new ArrayList<CurrencyType>();
		}
		this.currencyList.add(currType);
	}
    
    /*
	 *  &estimatedAmountTable.currencyConversionList(0).baseAmount.code=GBP
	 * &estimatedAmountTable.currencyConversionList(0).baseAmount.amount=1.0
	 * &estimatedAmountTable.currencyConversionList(0).currencyList.currency(0).code=JPY
	 * &estimatedAmountTable.currencyConversionList(0).currencyList.currency(0).amount=231
	 * &estimatedAmountTable.currencyConversionList(1).baseAmount.code=EUR
	 * &estimatedAmountTable.currencyConversionList(1).baseAmount.amount=100.0
	 * &estimatedAmountTable.currencyConversionList(1).currencyList.currency(0).code=JPY
	 * &estimatedAmountTable.currencyConversionList(1).currencyList.currency(0).amount=15764
   	 */
    public CurrencyConversionList(HashMap<String, String> params,int index) {
    	if(params.containsKey("estimatedAmountTable.currencyConversionList(" + index + ").baseAmount.code")){
    		this.baseAmount = new CurrencyType(
    				CurrencyCodes.valueOf(params.get("estimatedAmountTable.currencyConversionList(" + index + ").baseAmount.code")),
    						Double.parseDouble(params.get("estimatedAmountTable.currencyConversionList(" + index + ").baseAmount.amount"))
    						);
    	}
    	//parse the currencyList
    	for(int i = 0; i < 10; i++){
    		if(params.containsKey("estimatedAmountTable.currencyConversionList(" + index + ").currencyList.currency(" + i + ").code")){
    			CurrencyType currType = new CurrencyType(
        				CurrencyCodes.valueOf(params.get("estimatedAmountTable.currencyConversionList(" + index + ").currencyList.currency(" + i + ").code")),
        						Double.parseDouble(params.get("estimatedAmountTable.currencyConversionList(" + index + ").currencyList.currency(" + i + ").amount"))
        						);
    			this.addToCurrencyList(currType);
    		} else {
    			break;
    		}
    	}
    }
    	
public String toString(){
		
		StringBuilder outStr = new StringBuilder();
		
		outStr.append("<table border=1 cellspacing=0 cellpadding=1>");
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
