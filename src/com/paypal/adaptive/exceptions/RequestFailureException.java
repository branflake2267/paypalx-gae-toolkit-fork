package com.paypal.adaptive.exceptions;

public class RequestFailureException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2906964437876762235L;

	private int HTTP_RESPONSE_CODE;

	/**
	 * @param hTTP_RESPONSE_CODE the hTTP_RESPONSE_CODE to set
	 */
	public void setHTTP_RESPONSE_CODE(int hTTP_RESPONSE_CODE) {
		HTTP_RESPONSE_CODE = hTTP_RESPONSE_CODE;
	}

	/**
	 * @return the hTTP_RESPONSE_CODE
	 */
	public int getHTTP_RESPONSE_CODE() {
		return HTTP_RESPONSE_CODE;
	}
	
	public RequestFailureException(int HTTP_RESPONSE_CODE){
		this.HTTP_RESPONSE_CODE = HTTP_RESPONSE_CODE;
	}
}
