/**
 * 
 */
package com.paypal.adaptive.exceptions;

/**
 * @author palavilli
 *
 */
public class ReceiversCountMismatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5844213597716330118L;
	/*
	 * 
	 */
	protected int expectedNumberOfReceivers;
	/*
	 * 
	 */
	protected int actualNumberOfReceivers;
	
	public ReceiversCountMismatchException(int expected, int actual){
		this.expectedNumberOfReceivers = expected;
		this.actualNumberOfReceivers = actual;
	}

	/**
	 * @return the expectedNumberOfReceivers
	 */
	public int getExpectedNumberOfReceivers() {
		return expectedNumberOfReceivers;
	}

	/**
	 * @param expectedNumberOfReceivers the expectedNumberOfReceivers to set
	 */
	public void setExpectedNumberOfReceivers(int expectedNumberOfReceivers) {
		this.expectedNumberOfReceivers = expectedNumberOfReceivers;
	}

	/**
	 * @return the actualNumberOfReceivers
	 */
	public int getActualNumberOfReceivers() {
		return actualNumberOfReceivers;
	}

	/**
	 * @param actualNumberOfReceivers the actualNumberOfReceivers to set
	 */
	public void setActualNumberOfReceivers(int actualNumberOfReceivers) {
		this.actualNumberOfReceivers = actualNumberOfReceivers;
	}
}
