/**
 * 
 */
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * <p>Java class for Payment Details.
 * 
 */
public class PaymentDetails {

	protected ActionType actionType = ActionType.PAY;
	protected String cancelUrl;
	protected CurrencyCodes currencyCode;
    protected String ipnNotificationUrl;
    protected String memo;
    protected ArrayList<PaymentInfo> paymentInfoList;
    protected String returnUrl;
    protected String senderEmail;
    protected SenderIdentifier sender;
    protected String status;
    protected String trackingId;
    protected String payKey;
    protected FeesPayerType feesPayer = FeesPayerType.EACHRECEIVER;
    protected boolean reverseAllParallelPaymentsOnError = false;
    protected String preapprovalKey;
    protected String pin;
    protected List<FundingConstraint> fundingConstraintList;
    protected List<Receiver> receiverList;
    
    public PaymentDetails(ActionType actionType){
    	this.actionType = actionType;
    	receiverList = new ArrayList<Receiver>();
    }
    
	public String getCancelUrl() {
		return cancelUrl;
	}
	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}
	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = CurrencyCodes.valueOf(currencyCode);
	}
	
	public void setCurrencyCode(CurrencyCodes currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	/**
	 * @return the currencyCode
	 */
	public CurrencyCodes getCurrencyCode() {
		return currencyCode;
	}
	public String getIpnNotificationUrl() {
		return ipnNotificationUrl;
	}
	public void setIpnNotificationUrl(String ipnNotificationUrl) {
		this.ipnNotificationUrl = ipnNotificationUrl;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getSenderEmail() {
		return senderEmail;
	}
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTrackingId() {
		return trackingId;
	}
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	public String getPayKey() {
		return payKey;
	}
	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}
	public FeesPayerType getFeesPayer() {
		return feesPayer;
	}
	public void setFeesPayer(FeesPayerType feesPayer) {
		this.feesPayer = feesPayer;
	}
	public boolean getReverseAllParallelPaymentsOnError() {
		return reverseAllParallelPaymentsOnError;
	}
	public void setReverseAllParallelPaymentsOnError(
			boolean reverseAllParallelPaymentsOnError) {
		this.reverseAllParallelPaymentsOnError = reverseAllParallelPaymentsOnError;
	}
	public String getPreapprovalKey() {
		return preapprovalKey;
	}
	public void setPreapprovalKey(String preapprovalKey) {
		this.preapprovalKey = preapprovalKey;
	}
	
	/**
	 * @param receiverList the receiverList to set
	 */
	public void setReceiverList(List<Receiver> receiverList) {
		this.receiverList = receiverList;
	}
	/**
	 * @param receiverList the receiverList to set
	 */
	public void addToReceiverList(Receiver receiver) {
		if(this.receiverList == null)
			this.receiverList = new ArrayList<Receiver>();
		
		this.receiverList.add(receiver);
	}
	/**
	 * @param receiverList the receiverList to set
	 */
	public void addAllToReceiverList(List<Receiver> receivers) {
		if(this.receiverList == null)
			this.receiverList = new ArrayList<Receiver>();
		
		this.receiverList.addAll(receivers);
	}
	/**
	 * @return the receiverList
	 */
	public List<Receiver> getReceiverList() {
		return receiverList;
	}
	/**
	 * @param receiverList the fundingConstraintList to set
	 */
	public void setFundingConstraintList(ArrayList<FundingConstraint> fundingConstraintList) {
		this.fundingConstraintList = fundingConstraintList;
	}
	/**
	 * @param receiverList the fundingConstraintList to set
	 */
	public void addToFundingConstraintList(FundingConstraint fundingConstraint) {
		if(this.fundingConstraintList == null)
			this.fundingConstraintList = new ArrayList<FundingConstraint>();
		
		this.fundingConstraintList.add(fundingConstraint);
	}
	/**
	 * @return the fundingConstraintList
	 */
	public List<FundingConstraint> getFundingConstraintList() {
		return fundingConstraintList;
	}
	
	/**
	 * @param paymentInfoList the receiverList to set
	 */
	public void setPaymentInfoList(ArrayList<PaymentInfo> paymentInfoList) {
		this.paymentInfoList = paymentInfoList;
	}
	/**
	 * @param paymentInfoList the receiverList to set
	 */
	public void addToPaymentInfoList(PaymentInfo receiver) {
		if(this.paymentInfoList == null)
			this.paymentInfoList = new ArrayList<PaymentInfo>();
		
		this.paymentInfoList.add(receiver);
	}
	/**
	 * @return the paymentInfoList
	 */
	public ArrayList<PaymentInfo> getPaymentInfoList() {
		return this.paymentInfoList;
	}
	/**
	 * @param pin the pin to set
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}
	/**
	 * @return the pin
	 */
	public String getPin() {
		return pin;
	}
	
	public String serialize() throws UnsupportedEncodingException{
		// prepare request parameters
    	StringBuilder postParameters = new StringBuilder();
         
    	// add actionType
    	postParameters.append(ParameterUtils.createUrlParameter("actionType", actionType.toString()));
    	postParameters.append(ParameterUtils.PARAM_SEP);
    	
    	// add sender or senderEmail - sender get's first preference
    	if(this.sender != null && this.sender.getEmail() != null){
   			postParameters.append(this.sender.serialize());
       		postParameters.append(ParameterUtils.PARAM_SEP);  
    	} else if(this.senderEmail != null) {
    		postParameters.append(ParameterUtils.createUrlParameter("senderEmail", this.senderEmail));
    		postParameters.append(ParameterUtils.PARAM_SEP);        	
    	}
    	int i = 0;
    	if(receiverList != null) {
	    	for(Receiver recvr: receiverList){
	    		postParameters.append(recvr.serialize(i));
	    		i++;
	    	}
    	}
    	i = 0;
    	if(fundingConstraintList != null) {
	    	for(FundingConstraint fConst: fundingConstraintList){
	    		postParameters.append(fConst.serialize(i));
	    		postParameters.append(ParameterUtils.PARAM_SEP);
	    		i++;
	    	}
    	}
        
    	// add currencyCode
    	postParameters.append(ParameterUtils.createUrlParameter("currencyCode", this.getCurrencyCode().toString()));
    	postParameters.append(ParameterUtils.PARAM_SEP);
    	// add feesPayer
    	postParameters.append(ParameterUtils.createUrlParameter("feesPayer", this.getFeesPayer().toString()));
    	postParameters.append(ParameterUtils.PARAM_SEP);
    	// add memo
    	if(memo != null) {
    		postParameters.append(ParameterUtils.createUrlParameter("memo", this.getMemo()));
    		postParameters.append(ParameterUtils.PARAM_SEP);
    	}
    	// add cancel/return urls
    	if(this.cancelUrl != null) {
	    	postParameters.append(ParameterUtils.createUrlParameter("cancelUrl", this.getCancelUrl()));
	    	postParameters.append(ParameterUtils.PARAM_SEP);
    	}
    	if(this.returnUrl != null) {
	    	postParameters.append(ParameterUtils.createUrlParameter("returnUrl", this.getReturnUrl()));
	    	postParameters.append(ParameterUtils.PARAM_SEP);
    	}
    	// add ipn url
    	if(this.ipnNotificationUrl != null) {
    		postParameters.append(ParameterUtils.createUrlParameter("ipnNotificationUrl", this.getIpnNotificationUrl()));
    		postParameters.append(ParameterUtils.PARAM_SEP);
    	}
    	// set reverseAllParallelPaymentsOnError
    	if(this.reverseAllParallelPaymentsOnError) {
    		postParameters.append(ParameterUtils.createUrlParameter("reverseAllParallelPaymentsOnError",Boolean.toString(this.reverseAllParallelPaymentsOnError)));
    		postParameters.append(ParameterUtils.PARAM_SEP);
    	}
  	    	
    	// set trackingId
    	if(this.trackingId != null) {
    		postParameters.append(ParameterUtils.createUrlParameter("trackingId", this.getTrackingId()));
    		postParameters.append(ParameterUtils.PARAM_SEP);
    	}
    	// set preapprovalKey
    	if(this.preapprovalKey != null) {
    		postParameters.append(ParameterUtils.createUrlParameter("preapprovalKey", this.getPreapprovalKey()));
    		postParameters.append(ParameterUtils.PARAM_SEP);
    	}
    	// set pin
    	if(this.pin != null) {
    		postParameters.append(ParameterUtils.createUrlParameter("pin", this.getPin()));
    		postParameters.append(ParameterUtils.PARAM_SEP);
    	}
    	return postParameters.toString();
	}
	
	public PaymentDetails(HashMap<String, String> payDetailsResponseParams){
		
		/*
		 * &currencyCode=USD
 *
		 */
		
		if(payDetailsResponseParams.containsKey("cancelUrl"))
			this.cancelUrl = payDetailsResponseParams.get("cancelUrl");
		if(payDetailsResponseParams.containsKey("returnUrl"))
			this.returnUrl = payDetailsResponseParams.get("returnUrl");
		if(payDetailsResponseParams.containsKey("ipnNotificationUrl"))
			this.ipnNotificationUrl = payDetailsResponseParams.get("ipnNotificationUrl");
		if(payDetailsResponseParams.containsKey("actionType"))
			this.actionType = ActionType.valueOf(payDetailsResponseParams.get("actionType"));
		if(payDetailsResponseParams.containsKey("status"))
			this.status = payDetailsResponseParams.get("status");
		if(payDetailsResponseParams.containsKey("payKey"))
			this.payKey = payDetailsResponseParams.get("payKey");
		if(payDetailsResponseParams.containsKey("senderEmail"))
			this.senderEmail = payDetailsResponseParams.get("senderEmail");
		if(payDetailsResponseParams.containsKey("sender.email") 
				|| payDetailsResponseParams.containsKey("sender.phone.phoneNumber"))
			this.sender = new SenderIdentifier(payDetailsResponseParams);
		if(payDetailsResponseParams.containsKey("reverseAllParallelPaymentsOnError"))
			this.reverseAllParallelPaymentsOnError = Boolean.parseBoolean(payDetailsResponseParams.get("reverseAllParallelPaymentsOnError"));
		if(payDetailsResponseParams.containsKey("feesPayer"))
			this.feesPayer = FeesPayerType.valueOf(payDetailsResponseParams.get("feesPayer"));
		if(payDetailsResponseParams.containsKey("currencyCode"))
			this.currencyCode = CurrencyCodes.valueOf(payDetailsResponseParams.get("currencyCode"));
		if(payDetailsResponseParams.containsKey("trackingId"))
			this.trackingId = payDetailsResponseParams.get("trackingId");
		
		// load up funding Constraints
		for(int i = 0; i < 3; i++){
			if(payDetailsResponseParams.containsKey("fundingConstraint.allowedFundingType.fundingTypeInfo(" + i + ").fundingType")){
				this.addToFundingConstraintList(new FundingConstraint(payDetailsResponseParams, i));
			} else {
				break;
			}
		}
		
		for(int i=0; i< 10; i++){
			
			if( payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").receiver.amount")
					|| payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").receiver.email")
					|| payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").receiver.primary")
					|| payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").transactionStatus")
					|| payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").transactionId")
					|| payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").senderTransactionId")
					|| payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").senderTransactionStatus")) {
				
				PaymentInfo pymtInfo = new PaymentInfo();
				if(payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").transactionId"))
					pymtInfo.transactionId = payDetailsResponseParams.get("paymentInfoList.paymentInfo(" + i +").transactionId");
				if(payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").transactionStatus"))
					pymtInfo.transactionStatus = payDetailsResponseParams.get("paymentInfoList.paymentInfo(" + i +").transactionStatus");
				if(payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").refundedAmount"))
					pymtInfo.refundedAmount = Double.parseDouble(payDetailsResponseParams.get("paymentInfoList.paymentInfo(" + i +").refundedAmount"));
				if(payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").pendingRefund"))
					pymtInfo.pendingRefund = Boolean.parseBoolean(payDetailsResponseParams.get("paymentInfoList.paymentInfo(" + i +").pendingRefund"));
				if(payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").senderTransactionId"))
					pymtInfo.senderTransactionId = payDetailsResponseParams.get("paymentInfoList.paymentInfo(" + i +").senderTransactionId");
				if(payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").senderTransactionStatus"))
					pymtInfo.senderTransactionStatus = payDetailsResponseParams.get("paymentInfoList.paymentInfo(" + i +").senderTransactionStatus");
			    
			
				Receiver recv = new Receiver();
				if(payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").receiver.amount"))
					recv.setAmount(Double.parseDouble(payDetailsResponseParams.get("paymentInfoList.paymentInfo(" + i +").receiver.amount")));
				if(payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").receiver.email"))
					recv.setEmail(payDetailsResponseParams.get("paymentInfoList.paymentInfo(" + i +").receiver.email"));
				if(payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").receiver.primary"))
					recv.setPrimary(Boolean.parseBoolean(payDetailsResponseParams.get("paymentInfoList.paymentInfo(" + i +").receiver.primary")));
				
				if(payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").receiver.phone.countryCode")
						|| payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").receiver.phone.extension")
						|| payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").receiver.phone.phonenumber"))
					recv.setPhone(payDetailsResponseParams.get("paymentInfoList.paymentInfo(" + i +").receiver.phone.countryCode"),
							payDetailsResponseParams.get("paymentInfoList.paymentInfo(" + i +").receiver.phone.extension"),
							payDetailsResponseParams.get("paymentInfoList.paymentInfo(" + i +").receiver.phone.phonenumber"));
				
				if(payDetailsResponseParams.containsKey("paymentInfoList.paymentInfo(" + i +").receiver.paymentType"))
					recv.setPaymentType(PaymentType.valueOf(payDetailsResponseParams.get("paymentInfoList.paymentInfo(" + i +").receiver.paymentType")));
				
				
				
				pymtInfo.setReceiver(recv);
				
				this.addToPaymentInfoList(pymtInfo);
				
			} else {
				break;
			}
		}
	}

	/**
	 * @return the sender
	 */
	public SenderIdentifier getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(SenderIdentifier sender) {
		this.sender = sender;
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
