package com.paypal.adaptive.core;

/**
 * <p>Java class for ReasonCodeType.
 * 
 */
public enum ReasonCodeType {
	Chargeback_Settlement("Chargeback Settlement"),
	Admin_reversal("Admin reversal"),
	Refund("Refund"),
	Unknown("Unknown Reason");

	String value;
	
	ReasonCodeType(String value){
		this.value = value;
	}
	
	public static ReasonCodeType getReasonCode(String reason_code){
		if("Chargeback Settlement".equals(reason_code)){
			return Chargeback_Settlement;
		} else if("Admin reversal".equals(reason_code)){
			return Admin_reversal;
		} else if("Refund".equals(reason_code)){
			return Refund;
		} else {
			return Unknown;
		}
	}
}
