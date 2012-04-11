/**
 * 
 */
package com.paypal.adaptive.exceptions;

import com.paypal.adaptive.core.PaymentType;

/**
 * @author palavilli
 *
 */
public class PaymentTypeNotAllowedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8785756271176630016L;

	/**
	 * 
	 */
	protected PaymentType paymentType;
	
	public PaymentTypeNotAllowedException(PaymentType paymentType){
		this.paymentType = paymentType;
	}

	/**
	 * @return the paymentType
	 */
	public PaymentType getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
	
}
