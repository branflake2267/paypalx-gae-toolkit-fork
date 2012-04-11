package com.paypal.adaptive.exceptions;

import java.util.ArrayList;
import java.util.List;

public class InvalidAPICredentialsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4024583723601102783L;

	private List<String> missingCredentials;
	
	public InvalidAPICredentialsException(){
	}

	/**
	 * @return the missingCredentials
	 */
	public List<String> getMissingCredentials() {
		return missingCredentials;
	}
	
	public void addToMissingCredentials(String missingCredential){
		this.missingCredentials.add(missingCredential);
	}

	public void setMissingCredentials(List<String> missingCredentials) {
		if(this.missingCredentials == null) {
			this.missingCredentials = new ArrayList<String>();
		}
		this.missingCredentials = missingCredentials;
	}
	
}
