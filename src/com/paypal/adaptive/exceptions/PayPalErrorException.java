package com.paypal.adaptive.exceptions;

import java.util.List;

import com.paypal.adaptive.core.PayError;
import com.paypal.adaptive.core.PaymentExecStatus;
import com.paypal.adaptive.core.ResponseEnvelope;

public class PayPalErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8496267589109532850L;

	protected ResponseEnvelope responseEnvelope;
	protected List<PayError> errorList;
	protected PaymentExecStatus paymentExecStatus;
	
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
	/**
	 * @return the payErrorList
	 */
	public List<PayError> getPayErrorList() {
		return errorList;
	}
	/**
	 * @param payErrorList the payErrorList to set
	 */
	public void setPayErrorList(List<PayError> errorList) {
		this.errorList = errorList;
	}
	
	public PayPalErrorException(ResponseEnvelope responseEnvelope, List<PayError> errorList){
		this.responseEnvelope = responseEnvelope;
		this.errorList = errorList;
	}
	
	public PayPalErrorException(ResponseEnvelope responseEnvelope, List<PayError> errorList, PaymentExecStatus paymentExecStatus){
		this.responseEnvelope = responseEnvelope;
		this.errorList = errorList;
		this.paymentExecStatus = paymentExecStatus;
	}
	
	public PayPalErrorException(){
		
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
	
}
