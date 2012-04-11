package com.paypal.adaptive.exceptions;

public class MissingParameterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8009855002556970037L;
	
	private String parameterName;
	
	public MissingParameterException(String parameterName){
		this.parameterName = parameterName;
	}

	/**
	 * @param parameterName the parameterName to set
	 */
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	/**
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}
	
}
