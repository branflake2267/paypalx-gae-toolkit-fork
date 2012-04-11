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
 * <p>Java class for PreapprovalDetals.
 * 
 */
public class PreapprovalDetails  {

	    protected String cancelUrl;
	    protected CurrencyCodes currencyCode = CurrencyCodes.USD;
	    protected int dateOfMonth;
	    protected DayOfWeek dayOfWeek = DayOfWeek.NO_DAY_SPECIFIED;
	    protected String endingDate;
	    protected double maxAmountPerPayment;
	    protected int maxNumberOfPayments;
	    protected int maxNumberOfPaymentsPerPeriod;
	    protected double maxTotalAmountOfAllPayments;
	    protected PaymentPeriodType paymentPeriod = PaymentPeriodType.NO_PERIOD_SPECIFIED;
	    protected String returnUrl;
	    protected String memo;
	    protected String ipnNotificationUrl;
	    protected String senderEmail;
		protected String startingDate;
	    protected PinType pinType = PinType.NOT_REQUIRED;
	    protected FeesPayerType feesPayer = FeesPayerType.EACHRECEIVER;
	    protected boolean displayMaxTotalAmount = false;
	    // the following fields are set in the PreapprovalResponse - no setters for these
	    protected String status;
	    protected boolean approved;
	    protected long curPayments;
	    protected double curPaymentsAmount;
	    protected long curPeriodAttempts;
	    protected String curPeriodEndingDate;
	    protected List<Address> addressList;
	    
	    
		public PreapprovalDetails(){
			
		}
		
		public String serialize() throws UnsupportedEncodingException{
			// prepare request parameters
	    	StringBuilder postParameters = new StringBuilder();
	    	
		    // add dateOfMonth
		    if(this.dateOfMonth >= 0){
		    	postParameters.append(ParameterUtils.createUrlParameter("dateOfMonth", Integer.toString(this.dateOfMonth)));
		    	postParameters.append(ParameterUtils.PARAM_SEP);
		    }
		    // add dayOfWeek
		    if(this.dayOfWeek != null){
		    	postParameters.append(ParameterUtils.createUrlParameter("dayOfWeek", this.dayOfWeek.toString()));
		    	postParameters.append(ParameterUtils.PARAM_SEP);
		    }
		    // add endingDate
		    if(this.endingDate != null){
		    	postParameters.append(ParameterUtils.createUrlParameter("endingDate", this.endingDate));
		    	postParameters.append(ParameterUtils.PARAM_SEP);
		    }
		    // add startingDate
		    if(this.startingDate != null){
		    	postParameters.append(ParameterUtils.createUrlParameter("startingDate", this.startingDate));
		    	postParameters.append(ParameterUtils.PARAM_SEP);
		    }
	    	// add senderEmail if set
	    	if(this.senderEmail != null) {
	    		postParameters.append(ParameterUtils.createUrlParameter("senderEmail", this.senderEmail));
	    		postParameters.append(ParameterUtils.PARAM_SEP);        	
	    	}
	    	// add maxAmountPerPayment if set
	    	if(this.maxAmountPerPayment > 0) {
	    		postParameters.append(ParameterUtils.createUrlParameter("maxAmountPerPayment", Double.toString(this.maxAmountPerPayment)));
	    		postParameters.append(ParameterUtils.PARAM_SEP);        	
	    	}
	    	// add maxTotalAmountOfAllPayments if set
	    	if(this.maxTotalAmountOfAllPayments > 0) {
	    		postParameters.append(ParameterUtils.createUrlParameter("maxTotalAmountOfAllPayments", Double.toString(this.maxTotalAmountOfAllPayments)));
	    		postParameters.append(ParameterUtils.PARAM_SEP);        	
	    	}
	    	
		    // add maxNumberOfPayments if set
	    	if(this.maxNumberOfPayments > 0) {
	    		postParameters.append(ParameterUtils.createUrlParameter("maxNumberOfPayments", Integer.toString(this.maxNumberOfPayments)));
	    		postParameters.append(ParameterUtils.PARAM_SEP);        	
	    	}
	    	// add maxNumberOfPaymentsPerPeriod if set
	    	if(this.maxNumberOfPaymentsPerPeriod > 0) {
	    		postParameters.append(ParameterUtils.createUrlParameter("maxNumberOfPaymentsPerPeriod", Integer.toString(this.maxNumberOfPaymentsPerPeriod)));
	    		postParameters.append(ParameterUtils.PARAM_SEP);        	
	    	}
		    
	    	// add paymentPeriod
	    	postParameters.append(ParameterUtils.createUrlParameter("paymentPeriod", this.getPaymentPeriod().toString()));
	    	postParameters.append(ParameterUtils.PARAM_SEP);
	    	// add pinType
	    	postParameters.append(ParameterUtils.createUrlParameter("pinType", this.getPinType().toString()));
	    	postParameters.append(ParameterUtils.PARAM_SEP);
	    	
	    	// add currencyCode
	    	postParameters.append(ParameterUtils.createUrlParameter("currencyCode", this.getCurrencyCode().toString()));
	    	postParameters.append(ParameterUtils.PARAM_SEP);
	    	// add memo
	    	if(memo != null) {
	    		postParameters.append(ParameterUtils.createUrlParameter("memo", this.getMemo()));
	    		postParameters.append(ParameterUtils.PARAM_SEP);
	    	}
	    	// add cancel/return urls
	    	postParameters.append(ParameterUtils.createUrlParameter("cancelUrl", this.getCancelUrl()));
	    	postParameters.append(ParameterUtils.PARAM_SEP);
	    	postParameters.append(ParameterUtils.createUrlParameter("returnUrl", this.getReturnUrl()));
	    	postParameters.append(ParameterUtils.PARAM_SEP);
	    	// add ipn url
	    	if(this.ipnNotificationUrl != null) {
	    		postParameters.append(ParameterUtils.createUrlParameter("ipnNotificationUrl", this.getIpnNotificationUrl()));
	    		postParameters.append(ParameterUtils.PARAM_SEP);
	    	}
	    	// add feesPayer
	    	if(this.feesPayer != null) {
		    	postParameters.append(ParameterUtils.createUrlParameter("feesPayer", this.feesPayer.toString()));
		    	postParameters.append(ParameterUtils.PARAM_SEP);
	    	}
	    	// add displayMaxTotalAmount
	    	if(this.displayMaxTotalAmount){
	    		postParameters.append(ParameterUtils.createUrlParameter("displayMaxTotalAmount", Boolean.toString(this.displayMaxTotalAmount)));
		    	postParameters.append(ParameterUtils.PARAM_SEP);
	    	}
	    	return postParameters.toString();
		}
		
		public PreapprovalDetails(HashMap<String, String> preapprovalDetailsResponseParams){
			
			if(preapprovalDetailsResponseParams.containsKey("cancelUrl"))
				this.cancelUrl = preapprovalDetailsResponseParams.get("cancelUrl");
			if(preapprovalDetailsResponseParams.containsKey("returnUrl"))
				this.returnUrl = preapprovalDetailsResponseParams.get("returnUrl");
			if(preapprovalDetailsResponseParams.containsKey("ipnNotificationUrl"))
				this.ipnNotificationUrl = preapprovalDetailsResponseParams.get("ipnNotificationUrl");
			if(preapprovalDetailsResponseParams.containsKey("dateOfMonth"))
				this.dateOfMonth = Integer.parseInt(preapprovalDetailsResponseParams.get("dateOfMonth"));
			if(preapprovalDetailsResponseParams.containsKey("dayOfWeek"))
				this.dayOfWeek = DayOfWeek.valueOf(preapprovalDetailsResponseParams.get("dayOfWeek"));
			if(preapprovalDetailsResponseParams.containsKey("endingDate"))
				this.endingDate = preapprovalDetailsResponseParams.get("endingDate");
			if(preapprovalDetailsResponseParams.containsKey("memo"))
				this.memo = preapprovalDetailsResponseParams.get("memo");
			if(preapprovalDetailsResponseParams.containsKey("senderEmail"))
				this.senderEmail = preapprovalDetailsResponseParams.get("senderEmail");
			if(preapprovalDetailsResponseParams.containsKey("currencyCode"))
				this.currencyCode = CurrencyCodes.valueOf(preapprovalDetailsResponseParams.get("currencyCode"));
			if(preapprovalDetailsResponseParams.containsKey("startingDate"))
				this.startingDate = preapprovalDetailsResponseParams.get("startingDate");
			if(preapprovalDetailsResponseParams.containsKey("status"))
				this.status = preapprovalDetailsResponseParams.get("status");
			if(preapprovalDetailsResponseParams.containsKey("curPeriodEndingDate"))
				this.curPeriodEndingDate = preapprovalDetailsResponseParams.get("curPeriodEndingDate");
			
			if(preapprovalDetailsResponseParams.containsKey("maxAmountPerPayment"))
				this.maxAmountPerPayment = Double.parseDouble(preapprovalDetailsResponseParams.get("maxAmountPerPayment"));
			if(preapprovalDetailsResponseParams.containsKey("maxTotalAmountOfAllPayments"))
				this.maxTotalAmountOfAllPayments = Double.parseDouble(preapprovalDetailsResponseParams.get("maxTotalAmountOfAllPayments"));
			if(preapprovalDetailsResponseParams.containsKey("curPaymentsAmount"))
				this.curPaymentsAmount = Double.parseDouble(preapprovalDetailsResponseParams.get("curPaymentsAmount"));
			if(preapprovalDetailsResponseParams.containsKey("maxNumberOfPayments"))
				this.maxNumberOfPayments = Integer.parseInt(preapprovalDetailsResponseParams.get("maxNumberOfPayments"));
			if(preapprovalDetailsResponseParams.containsKey("maxNumberOfPaymentsPerPeriod"))
				this.maxNumberOfPaymentsPerPeriod = Integer.parseInt(preapprovalDetailsResponseParams.get("maxNumberOfPaymentsPerPeriod"));
			if(preapprovalDetailsResponseParams.containsKey("approved"))
				this.approved = Boolean.parseBoolean(preapprovalDetailsResponseParams.get("approved"));
			if(preapprovalDetailsResponseParams.containsKey("curPayments"))
				this.curPayments = Long.parseLong(preapprovalDetailsResponseParams.get("curPayments"));
			if(preapprovalDetailsResponseParams.containsKey("curPeriodAttempts"))
				this.curPeriodAttempts = Long.parseLong(preapprovalDetailsResponseParams.get("curPeriodAttempts"));

			if(preapprovalDetailsResponseParams.containsKey("pinType"))
				this.pinType = PinType.valueOf(preapprovalDetailsResponseParams.get("pinType"));

			if(preapprovalDetailsResponseParams.containsKey("paymentPeriod"))
				this.paymentPeriod = PaymentPeriodType.valueOf(preapprovalDetailsResponseParams.get("paymentPeriod"));
			
			if(preapprovalDetailsResponseParams.containsKey("displayMaxTotalAmount"))
				this.displayMaxTotalAmount = Boolean.parseBoolean(preapprovalDetailsResponseParams.get("displayMaxTotalAmount"));
			
			if(preapprovalDetailsResponseParams.containsKey("feesPayer"))
				this.feesPayer = FeesPayerType.valueOf(preapprovalDetailsResponseParams.get("feesPayer"));
			//process addressList
			for(int i = 0; i < 10; i++){
	    		if(preapprovalDetailsResponseParams.containsKey("addressList.address("+ i +").addresseeName")
	    				|| preapprovalDetailsResponseParams.containsKey("addressList.address("+ i +").addressId")) {
		    		Address addr = new Address(preapprovalDetailsResponseParams, "addressList.address("+ i +")");
		    		this.addToAddressList(addr);
	    		} else
	    			break;
	    	}
		}
		/**
		 * @return the feesPayer
		 */
		public FeesPayerType getFeesPayer() {
			return feesPayer;
		}
		/**
		 * @param feesPayer the feesPayer to set
		 */
		public void setFeesPayer(FeesPayerType feesPayer) {
			this.feesPayer = feesPayer;
		}
		/**
		 * @return the displayMaxTotalAmount
		 */
		public boolean isDisplayMaxTotalAmount() {
			return displayMaxTotalAmount;
		}
		/**
		 * @param displayMaxTotalAmount the displayMaxTotalAmount to set
		 */
		public void setDisplayMaxTotalAmount(boolean displayMaxTotalAmount) {
			this.displayMaxTotalAmount = displayMaxTotalAmount;
		}

		/**
		 * @return the cancelUrl
		 */
		public String getCancelUrl() {
			return cancelUrl;
		}

		/**
		 * @param cancelUrl the cancelUrl to set
		 */
		public void setCancelUrl(String cancelUrl) {
			this.cancelUrl = cancelUrl;
		}

		/**
		 * @return the currencyCode
		 */
		public CurrencyCodes getCurrencyCode() {
			return currencyCode;
		}

		/**
		 * @param currencyCode the currencyCode to set
		 */
		public void setCurrencyCode(CurrencyCodes currencyCode) {
			this.currencyCode = currencyCode;
		}

		/**
		 * @return the dateOfMonth
		 */
		public int getDateOfMonth() {
			return dateOfMonth;
		}

		/**
		 * @param dateOfMonth the dateOfMonth to set
		 */
		public void setDateOfMonth(int dateOfMonth) {
			this.dateOfMonth = dateOfMonth;
		}

		/**
		 * @return the dayOfWeek
		 */
		public DayOfWeek getDayOfWeek() {
			return dayOfWeek;
		}

		/**
		 * @param dayOfWeek the dayOfWeek to set
		 */
		public void setDayOfWeek(DayOfWeek dayOfWeek) {
			this.dayOfWeek = dayOfWeek;
		}

		/**
		 * @return the endingDate
		 */
		public String getEndingDate() {
			return endingDate;
		}

		/**
		 * @param endingDate the endingDate to set
		 */
		public void setEndingDate(String endingDate) {
			this.endingDate = endingDate;
		}

		/**
		 * @return the maxAmountPerPayment
		 */
		public double getMaxAmountPerPayment() {
			return maxAmountPerPayment;
		}

		/**
		 * @param maxAmountPerPayment the maxAmountPerPayment to set
		 */
		public void setMaxAmountPerPayment(double maxAmountPerPayment) {
			this.maxAmountPerPayment = maxAmountPerPayment;
		}

		/**
		 * @return the maxNumberOfPayments
		 */
		public int getMaxNumberOfPayments() {
			return maxNumberOfPayments;
		}

		/**
		 * @param maxNumberOfPayments the maxNumberOfPayments to set
		 */
		public void setMaxNumberOfPayments(int maxNumberOfPayments) {
			this.maxNumberOfPayments = maxNumberOfPayments;
		}

		/**
		 * @return the maxNumberOfPaymentsPerPeriod
		 */
		public int getMaxNumberOfPaymentsPerPeriod() {
			return maxNumberOfPaymentsPerPeriod;
		}

		/**
		 * @param maxNumberOfPaymentsPerPeriod the maxNumberOfPaymentsPerPeriod to set
		 */
		public void setMaxNumberOfPaymentsPerPeriod(int maxNumberOfPaymentsPerPeriod) {
			this.maxNumberOfPaymentsPerPeriod = maxNumberOfPaymentsPerPeriod;
		}

		/**
		 * @return the maxTotalAmountOfAllPayments
		 */
		public double getMaxTotalAmountOfAllPayments() {
			return maxTotalAmountOfAllPayments;
		}

		/**
		 * @param maxTotalAmountOfAllPayments the maxTotalAmountOfAllPayments to set
		 */
		public void setMaxTotalAmountOfAllPayments(double maxTotalAmountOfAllPayments) {
			this.maxTotalAmountOfAllPayments = maxTotalAmountOfAllPayments;
		}

		/**
		 * @return the paymentPeriod
		 */
		public PaymentPeriodType getPaymentPeriod() {
			return paymentPeriod;
		}

		/**
		 * @param paymentPeriod the paymentPeriod to set
		 */
		public void setPaymentPeriod(PaymentPeriodType paymentPeriod) {
			this.paymentPeriod = paymentPeriod;
		}

		/**
		 * @return the returnUrl
		 */
		public String getReturnUrl() {
			return returnUrl;
		}

		/**
		 * @param returnUrl the returnUrl to set
		 */
		public void setReturnUrl(String returnUrl) {
			this.returnUrl = returnUrl;
		}

		/**
		 * @return the memo
		 */
		public String getMemo() {
			return memo;
		}

		/**
		 * @param memo the memo to set
		 */
		public void setMemo(String memo) {
			this.memo = memo;
		}

		/**
		 * @return the ipnNotificationUrl
		 */
		public String getIpnNotificationUrl() {
			return ipnNotificationUrl;
		}

		/**
		 * @param ipnNotificationUrl the ipnNotificationUrl to set
		 */
		public void setIpnNotificationUrl(String ipnNotificationUrl) {
			this.ipnNotificationUrl = ipnNotificationUrl;
		}

		/**
		 * @return the senderEmail
		 */
		public String getSenderEmail() {
			return senderEmail;
		}

		/**
		 * @param senderEmail the senderEmail to set
		 */
		public void setSenderEmail(String senderEmail) {
			this.senderEmail = senderEmail;
		}

		/**
		 * @return the startingDate
		 */
		public String getStartingDate() {
			return startingDate;
		}

		/**
		 * @param startingDate the startingDate to set
		 */
		public void setStartingDate(String startingDate) {
			this.startingDate = startingDate;
		}

		/**
		 * @return the pinType
		 */
		public PinType getPinType() {
			return pinType;
		}

		/**
		 * @param pinType the pinType to set
		 */
		public void setPinType(PinType pinType) {
			this.pinType = pinType;
		}

		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}

		/**
		 * @return the approved
		 */
		public boolean isApproved() {
			return approved;
		}

		/**
		 * @return the curPayments
		 */
		public long getCurPayments() {
			return curPayments;
		}

		/**
		 * @return the curPaymentsAmount
		 */
		public double getCurPaymentsAmount() {
			return curPaymentsAmount;
		}

		/**
		 * @return the curPeriodAttempts
		 */
		public long getCurPeriodAttempts() {
			return curPeriodAttempts;
		}

		/**
		 * @return the curPeriodEndingDate
		 */
		public String getCurPeriodEndingDate() {
			return curPeriodEndingDate;
		}

		/**
		 * @return the addressList
		 */
		public List<Address> getAddressList() {
			return addressList;
		}
		
		/**
		 * @param receiverList the receiverList to set
		 */
		protected void addToAddressList(Address address) {
			if(this.addressList == null)
				this.addressList = new ArrayList<Address>();
			
			this.addressList.add(address);
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
