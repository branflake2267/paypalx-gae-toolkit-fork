/**
 * 
 */
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * @author palavilli
 *
 */
public class InstitutionCustomer {

	protected String countryCode;
	protected String displayName;
	protected String email;
	protected String firstName;
	protected String institutionCustomerId;
	protected String institutionId;
	protected String lastName;
	
	public InstitutionCustomer(){
		//-
	}

	public InstitutionCustomer(HashMap<String, String> params, String prefix) {
		if(params.containsKey( prefix + ".institutionId")){
			this.institutionId = params.get(prefix + ".institutionId");
		}
		if(params.containsKey(prefix + ".firstName")){
			this.firstName = params.get(prefix + ".firstName");
		}
		if(params.containsKey(prefix + ".lastName")){
			this.lastName = params.get(prefix + ".lastName");
		}
		if(params.containsKey(prefix + ".displayName")){
			this.displayName = params.get(prefix + ".displayName");
		}
		if(params.containsKey(prefix + ".institutionCustomerId")){
			this.institutionCustomerId = params.get(prefix + ".institutionCustomerId");
		}
		if(params.containsKey(prefix + ".countryCode")){
			this.countryCode = params.get(prefix + ".countryCode");
		}
		if(params.containsKey(prefix + ".email")){
			this.email = params.get(prefix + ".email");
		}
		
	}

	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the institutionCustomerId
	 */
	public String getInstitutionCustomerId() {
		return institutionCustomerId;
	}

	/**
	 * @param institutionCustomerId the institutionCustomerId to set
	 */
	public void setInstitutionCustomerId(String institutionCustomerId) {
		this.institutionCustomerId = institutionCustomerId;
	}

	/**
	 * @return the institutionId
	 */
	public String getInstitutionId() {
		return institutionId;
	}

	/**
	 * @param institutionId the institutionId to set
	 */
	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String serialize(String prefix) throws UnsupportedEncodingException{
		StringBuilder outString = new StringBuilder();
		boolean isFirst = true;
		if(this.countryCode != null && this.countryCode.length() > 0) {
			outString.append(ParameterUtils.createUrlParameter(prefix + ".countryCode", this.countryCode));
			isFirst = false;
		}
		if(this.displayName != null && this.displayName.length() > 0) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter(prefix + ".displayName", this.displayName));
			isFirst = false;
		}
		if(this.email != null && this.email.length() > 0) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter(prefix + ".email", this.email));
			isFirst = false;
		}
		if(this.firstName != null && this.firstName.length() > 0) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter(prefix + ".firstName", this.firstName));
			isFirst = false;
		}
		if(this.institutionCustomerId != null && this.institutionCustomerId.length() > 0) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter(prefix + ".institutionCustomerId", this.institutionCustomerId));
			isFirst = false;
		}
		if(this.institutionId != null && this.institutionId.length() > 0) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter(prefix + ".institutionId", this.institutionId));
			isFirst = false;
		}
		if(this.lastName != null && this.lastName.length() > 0) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter(prefix + ".lastName", this.lastName));
		}
		return outString.toString();
	}
	
public String toString(){
		
		StringBuilder outStr = new StringBuilder();
		
		outStr.append("<table>");
		outStr.append("<tr><th>");
		outStr.append(this.getClass().getSimpleName());
		outStr.append("</th><td></td></tr>");
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo( this.getClass(), Object.class );
			for ( PropertyDescriptor pd : info.getPropertyDescriptors() ) {
				try {
					String name = pd.getName();
					Object value = this.getClass().getDeclaredField(name).get(this);
					if(value != null) {
						outStr.append("<tr><td>");
						outStr.append(pd.getName());
						outStr.append("</td><td>");
						outStr.append(value.toString());
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				outStr.append("</td></tr>");
			}
	    } catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outStr.append("</table>");
		return outStr.toString(); 
		
	}
}
