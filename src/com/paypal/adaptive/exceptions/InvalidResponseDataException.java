package com.paypal.adaptive.exceptions;

public class InvalidResponseDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5007346902429381814L;

	private String responseData;

	/**
	 * @param responseData the responseData to set
	 */
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	/**
	 * @return the responseData
	 */
	public String getResponseData() {
		return responseData;
	}
	
	public InvalidResponseDataException(){
		
	}
	
	public InvalidResponseDataException(String responseData){
		this.responseData = responseData;
	}
	
}
