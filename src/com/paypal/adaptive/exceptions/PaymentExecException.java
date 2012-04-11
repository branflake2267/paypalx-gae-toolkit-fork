/**
 * 
 */
package com.paypal.adaptive.exceptions;

import java.util.ArrayList;

import com.paypal.adaptive.core.PayError;
import com.paypal.adaptive.core.PaymentExecStatus;
import com.paypal.adaptive.core.ResponseEnvelope;

/**
 * @author palavilli
 *
 */
public class PaymentExecException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -871287110300727557L;

	/**
	 * 
	 */
	protected PaymentExecStatus paymentExecStatus;
	/**
	 * 
	 */
	protected ArrayList<PayError> payErrorList;
	/**
	 * 
	 */
	protected ResponseEnvelope responseEnvelope;
	
	public PaymentExecException(PaymentExecStatus paymentExecStatus){
		this.paymentExecStatus = paymentExecStatus;
	}

	/**
	 * @return the paymentExecStatus
	 */
	public PaymentExecStatus getPaymentExecStatus() {
		return paymentExecStatus;
	}

	/**
	 * @param paymentExecStatus the paymentExecStatus to set
	 */
	public void setPaymentExecStatus(PaymentExecStatus paymentExecStatus) {
		this.paymentExecStatus = paymentExecStatus;
	}

	/**
	 * @return the payErrorList
	 */
	public ArrayList<PayError> getPayErrorList() {
		return payErrorList;
	}

	/**
	 * @param payErrorList the payErrorList to set
	 */
	public void setPayErrorList(ArrayList<PayError> payErrorList) {
		this.payErrorList = payErrorList;
	}

	/**
	 * @return the responseEnvelope
	 */
	public ResponseEnvelope getResponseEnvelope() {
		return responseEnvelope;
	}

	/**
	 * @param responseEnvelope the responseEnvelope to set
	 */
	public void setResponseEnvelope(ResponseEnvelope responseEnvelope) {
		this.responseEnvelope = responseEnvelope;
	}
	
}
