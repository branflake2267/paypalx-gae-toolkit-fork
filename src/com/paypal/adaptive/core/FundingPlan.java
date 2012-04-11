
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * 						FundingPlan describes the funding sources to be
 * 						used for a specific payment.
 * 					
 * 
 * <p>Java class for FundingPlan complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FundingPlan">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fundingPlanId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fundingAmount" type="{http://svcs.paypal.com/types/common}CurrencyType"/>
 *         &lt;element name="backupFundingSource" type="{http://svcs.paypal.com/types/ap}FundingSource" minOccurs="0"/>
 *         &lt;element name="senderFees" type="{http://svcs.paypal.com/types/common}CurrencyType" minOccurs="0"/>
 *         &lt;element name="currencyConversion" type="{http://svcs.paypal.com/types/ap}CurrencyConversion" minOccurs="0"/>
 *         &lt;element name="charge" type="{http://svcs.paypal.com/types/ap}FundingPlanCharge" maxOccurs="unbounded"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

public class FundingPlan {

    protected String fundingPlanId;
    protected CurrencyType fundingAmount;
    protected FundingSource backupFundingSource;
    protected CurrencyType senderFees;
    protected CurrencyConversion currencyConversion;
    protected List<FundingPlanCharge> charge;

    /**
     * Gets the value of the fundingPlanId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFundingPlanId() {
        return fundingPlanId;
    }

    /**
     * Sets the value of the fundingPlanId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFundingPlanId(String value) {
        this.fundingPlanId = value;
    }

    /**
     * Gets the value of the fundingAmount property.
     * 
     * @return
     *     possible object is
     *     {@link CurrencyType }
     *     
     */
    public CurrencyType getFundingAmount() {
        return fundingAmount;
    }

    /**
     * Sets the value of the fundingAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link CurrencyType }
     *     
     */
    public void setFundingAmount(CurrencyType value) {
        this.fundingAmount = value;
    }

    /**
     * Gets the value of the backupFundingSource property.
     * 
     * @return
     *     possible object is
     *     {@link FundingSource }
     *     
     */
    public FundingSource getBackupFundingSource() {
        return backupFundingSource;
    }

    /**
     * Sets the value of the backupFundingSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link FundingSource }
     *     
     */
    public void setBackupFundingSource(FundingSource value) {
        this.backupFundingSource = value;
    }

    /**
     * Gets the value of the senderFees property.
     * 
     * @return
     *     possible object is
     *     {@link CurrencyType }
     *     
     */
    public CurrencyType getSenderFees() {
        return senderFees;
    }

    /**
     * Sets the value of the senderFees property.
     * 
     * @param value
     *     allowed object is
     *     {@link CurrencyType }
     *     
     */
    public void setSenderFees(CurrencyType value) {
        this.senderFees = value;
    }

    /**
     * Gets the value of the currencyConversion property.
     * 
     * @return
     *     possible object is
     *     {@link CurrencyConversion }
     *     
     */
    public CurrencyConversion getCurrencyConversion() {
        return currencyConversion;
    }

    /**
     * Sets the value of the currencyConversion property.
     * 
     * @param value
     *     allowed object is
     *     {@link CurrencyConversion }
     *     
     */
    public void setCurrencyConversion(CurrencyConversion value) {
        this.currencyConversion = value;
    }

    /**
     * Gets the value of the charge property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the charge property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCharge().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FundingPlanCharge }
     * 
     * 
     */
    public List<FundingPlanCharge> getCharge() {
        if (charge == null) {
            charge = new ArrayList<FundingPlanCharge>();
        }
        return this.charge;
    }
    
    /**
	 * @param errorList the payErrorList to set
	 */
	public void addToChargeList(FundingPlanCharge chargeObj) {
		if(this.charge == null)
			this.charge = new ArrayList<FundingPlanCharge>();
		
		this.charge.add(chargeObj);
	}

    public FundingPlan(){
    	// default constructor
    }
    
    public FundingPlan(HashMap<String, String> params, String prefix){
    	if(params.containsKey( prefix + ".fundingPlanId"))
    		this.fundingPlanId = params.get( prefix + ".fundingPlanId");
    	if(params.containsKey( prefix + ".fundingAmount.amount")
    			&& params.containsKey( prefix + ".fundingAmount.code")) {
    		this.fundingAmount = new CurrencyType(
    				CurrencyCodes.valueOf(params.get( prefix + ".fundingAmount.code")),
    				Double.parseDouble(params.get( prefix + ".fundingAmount.amount")));
    	}
    	if(params.containsKey( prefix + ".backupFundingSource.fundingSourceId"))
    		this.backupFundingSource = new FundingSource(params,  prefix + ".backupFundingSource");
    			
    	if(params.containsKey( prefix + ".senderFees.amount")
    			&& params.containsKey( prefix + ".senderFees.code")){
    		this.senderFees = new CurrencyType(
    				CurrencyCodes.valueOf(params.get( prefix + ".senderFees.code")),
    				Double.parseDouble(params.get( prefix + ".senderFees.amount")));
    	}
    	
    	if(params.containsKey( prefix + ".currencyConversion.from.code")){
    		this.currencyConversion = new CurrencyConversion(params,  prefix + ".currencyConversion");
    	}
    			
    	// fundingplancharge
    	// we will parse 10 errors at max for now
    	for(int i = 0; i < 10; i++){
    		if(params.containsKey( prefix + ".charge.(" + i +").charge.code")){
    			FundingPlanCharge chargeObj = new FundingPlanCharge(params,  prefix + ".charge.(" + i +")");
    			this.addToChargeList(chargeObj);    			
    		} else {
    			break;
    		}
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
