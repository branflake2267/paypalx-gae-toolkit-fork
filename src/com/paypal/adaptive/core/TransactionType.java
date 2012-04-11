package com.paypal.adaptive.core;

public enum TransactionType {
	Adaptive_Payment_PAY("Adaptive Payment PAY"),
	Adaptive_Payment_CREATE("Adaptive Payment CREATE"),
	Adjustment("Adjustment"),
	Adaptive_Payment_PREAPPROVAL("Adaptive Payment PREAPPROVAL"),
	UNEXPECTED("Unexpected Transaction Type");
	
	protected String value;
	
	private TransactionType(String value){
		this.value = value;
	}
	
	public static TransactionType getTransactionType(String str){
		
		if("Adaptive Payment PAY".equals(str)){
			return Adaptive_Payment_PAY;
		} else if("Adaptive Payment CREATE".equals(str)){
			return Adaptive_Payment_CREATE;
		} else if("Adjustment".equals(str)){
			return Adjustment;
		} else if("Adaptive Payment PREAPPROVAL".equals(str)){
			return Adaptive_Payment_PREAPPROVAL;
		} else {
			return UNEXPECTED;
		}
	}
	
	
	
}
