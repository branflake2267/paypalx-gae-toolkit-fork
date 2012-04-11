package com.paypal.adaptive.exceptions;

public class InvalidPrimaryReceiverAmountException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8279767924317034017L;

	/**
	 * 
	 */
	protected double primaryReceiverAmount;
	/*
	 * 
	 */
	protected double sumOfSecondaryReceiversAmount;
	
	public InvalidPrimaryReceiverAmountException(double primaryReceiverAmount,
			double sumOfSecondaryReceiversAmount){
		this.primaryReceiverAmount = primaryReceiverAmount;
		this.sumOfSecondaryReceiversAmount = sumOfSecondaryReceiversAmount;
	}

	/**
	 * @return the primaryReceiverAmount
	 */
	public double getPrimaryReceiverAmount() {
		return primaryReceiverAmount;
	}

	/**
	 * @param primaryReceiverAmount the primaryReceiverAmount to set
	 */
	public void setPrimaryReceiverAmount(double primaryReceiverAmount) {
		this.primaryReceiverAmount = primaryReceiverAmount;
	}

	/**
	 * @return the sumOfSecondaryReceiversAmount
	 */
	public double getSumOfSecondaryReceiversAmount() {
		return sumOfSecondaryReceiversAmount;
	}

	/**
	 * @param sumOfSecondaryReceiversAmount the sumOfSecondaryReceiversAmount to set
	 */
	public void setSumOfSecondaryReceiversAmount(double sumOfSecondaryReceiversAmount) {
		this.sumOfSecondaryReceiversAmount = sumOfSecondaryReceiversAmount;
	}
	
}
