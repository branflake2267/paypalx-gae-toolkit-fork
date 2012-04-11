/**
 * 
 */
package com.paypal.adaptive.exceptions;

/**
 * @author palavilli
 *
 */
public class NotEnoughReceivers extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4869642844431448432L;
	/**
	 * 
	 */
	protected int actualNumber;
	/**
	 * 
	 */
	protected int minimumRequired;
	
	public NotEnoughReceivers(int minimumRequired, int actualNumber){
		this.actualNumber = actualNumber;
		this.minimumRequired = minimumRequired;
	}

	/**
	 * @return the actualNumber
	 */
	public int getActualNumber() {
		return actualNumber;
	}

	/**
	 * @param actualNumber the actualNumber to set
	 */
	public void setActualNumber(int actualNumber) {
		this.actualNumber = actualNumber;
	}

	/**
	 * @return the minimumRequired
	 */
	public int getMinimumRequired() {
		return minimumRequired;
	}

	/**
	 * @param minimumRequired the minimumRequired to set
	 */
	public void setMinimumRequired(int minimumRequired) {
		this.minimumRequired = minimumRequired;
	}
}
