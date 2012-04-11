
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;


public class RefundInfo {

   
    protected Receiver receiver;
    protected String refundStatus;
    protected double refundNetAmount;
    protected double refundFeeAmount;
    protected double refundGrossAmount;
    protected double totalOfAllRefunds;
    protected boolean refundHasBecomeFull;
    protected String encryptedRefundTransactionId;
    protected String refundTransactionStatus;
	protected ArrayList<PayError> errorList;

	public RefundInfo(HashMap<String, String> params, int index){
    	
    	if(params.containsKey("refundInfoList.refundInfo(" + index + ").refundStatus")){
    		this.refundStatus = params.get("refundInfoList.refundInfo(" + index + ").refundStatus");
    	}
    	
    	if(params.containsKey("refundInfoList.refundInfo(" + index + ").encryptedRefundTransactionId")){
    		this.encryptedRefundTransactionId = params.get("refundInfoList.refundInfo(" + index + ").encryptedRefundTransactionId");
    	}
    	
    	if(params.containsKey("refundInfoList.refundInfo(" + index + ").refundTransactionStatus")){
    		this.refundTransactionStatus = params.get("refundInfoList.refundInfo(" + index + ").refundTransactionStatus");
    	}
    	
    	if(params.containsKey("refundInfoList.refundInfo(" + index + ").refundNetAmount")){
    		this.refundNetAmount = Double.parseDouble(params.get("refundInfoList.refundInfo(" + index + ").refundNetAmount"));
    	}
    	if(params.containsKey("refundInfoList.refundInfo(" + index + ").refundFeeAmount")){
    		this.refundFeeAmount = Double.parseDouble(params.get("refundInfoList.refundInfo(" + index + ").refundFeeAmount"));
    	}
    	if(params.containsKey("refundInfoList.refundInfo(" + index + ").refundGrossAmount")){
    		this.refundGrossAmount = Double.parseDouble(params.get("refundInfoList.refundInfo(" + index + ").refundGrossAmount"));
    	}
    	if(params.containsKey("refundInfoList.refundInfo(" + index + ").totalOfAllRefunds")){
    		this.totalOfAllRefunds = Double.parseDouble(params.get("refundInfoList.refundInfo(" + index + ").totalOfAllRefunds"));
    	}
    	if(params.containsKey("refundInfoList.refundInfo(" + index + ").refundHasBecomeFull")){
    		this.refundHasBecomeFull = Boolean.parseBoolean(params.get("refundInfoList.refundInfo(" + index + ").refundHasBecomeFull"));
    	}

    	
    	
    	receiver= new Receiver();
		if(params.containsKey("refundInfoList.refundInfo(" + index + ").receiver.amount"))
			receiver.setAmount(Double.parseDouble(params.get("refundInfoList.refundInfo(" + index + ").receiver.amount")));
		if(params.containsKey("refundInfoList.refundInfo(" + index + ").receiver.email"))
			receiver.setEmail(params.get("refundInfoList.refundInfo(" + index + ").receiver.email"));
		if(params.containsKey("refundInfoList.refundInfo(" + index + ").receiver.primary"))
			receiver.setPrimary(Boolean.parseBoolean(params.get("refundInfoList.refundInfo(" + index + ").receiver.primary")));
		if(params.containsKey("refundInfoList.refundInfo(" + index + ").receiver.phone.countryCode")
				|| params.containsKey("refundInfoList.refundInfo(" + index + ").receiver.phone.extension")
				|| params.containsKey("refundInfoList.refundInfo(" + index + ").receiver.phone.phonenumber"))
			receiver.setPhone(params.get("refundInfoList.refundInfo(" + index + ").receiver.phone.countryCode"),
					params.get("refundInfoList.refundInfo(" + index + ").receiver.phone.extension"),
					params.get("refundInfoList.refundInfo(" + index + ").receiver.phone.phonenumber"));
		if(params.containsKey("refundInfoList.refundInfo(" + index + ").receiver.paymentType"))
			receiver.setPaymentType(PaymentType.valueOf(params.get("refundInfoList.refundInfo(" + index + ").receiver.paymentType")));
		
    	
    	errorList = new ArrayList<PayError>();
    	// we will parse 10 errors for now
    	for(int i = 0; i < 10; i++){
    		if(params.containsKey("refundInfoList.refundInfo(" + index + ").errorList.error(" + i +").errorId")){
    			PayError pErr = new PayError( params, i);
    			errorList.add(pErr);
    		} else {
    			break;
    		}
    	}
    	
    }

	public Receiver getReceiver() {
		return receiver;
	}
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public double getRefundNetAmount() {
		return refundNetAmount;
	}
	public void setRefundNetAmount(double refundNetAmount) {
		this.refundNetAmount = refundNetAmount;
	}
	public double getRefundFeeAmount() {
		return refundFeeAmount;
	}
	public void setRefundFeeAmount(double refundFeeAmount) {
		this.refundFeeAmount = refundFeeAmount;
	}
	public double getRefundGrossAmount() {
		return refundGrossAmount;
	}
	public void setRefundGrossAmount(double refundGrossAmount) {
		this.refundGrossAmount = refundGrossAmount;
	}
	public double getTotalOfAllRefunds() {
		return totalOfAllRefunds;
	}
	public void setTotalOfAllRefunds(double totalOfAllRefunds) {
		this.totalOfAllRefunds = totalOfAllRefunds;
	}
	public boolean isRefundHasBecomeFull() {
		return refundHasBecomeFull;
	}
	public void setRefundHasBecomeFull(boolean refundHasBecomeFull) {
		this.refundHasBecomeFull = refundHasBecomeFull;
	}
	public String getEncryptedRefundTransactionId() {
		return encryptedRefundTransactionId;
	}
	public void setEncryptedRefundTransactionId(String encryptedRefundTransactionId) {
		this.encryptedRefundTransactionId = encryptedRefundTransactionId;
	}
	public String getRefundTransactionStatus() {
		return refundTransactionStatus;
	}
	public void setRefundTransactionStatus(String refundTransactionStatus) {
		this.refundTransactionStatus = refundTransactionStatus;
	}
	public ArrayList<PayError> getErrorList() {
		return errorList;
	}
	public void setErrorList(ArrayList<PayError> errorList) {
		this.errorList = errorList;
	}

	/**
	 * @param payErrorList the payErrorList to set
	 */
	public void addToPayErrorList(PayError payError) {
		if(this.errorList == null)
			this.errorList = new ArrayList<PayError>();
		
		this.errorList.add(payError);
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
