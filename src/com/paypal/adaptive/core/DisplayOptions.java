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
public class DisplayOptions {

	
	protected String emailHeaderImageUrl;
	protected String emailMarketingImageUrl;
	protected String headerImageUrl;
	protected String businessName;
	
	/**
	 * 
	 */
	public DisplayOptions() {
		// TODO Auto-generated constructor stub
	}

	public DisplayOptions(HashMap<String, String> params) {
		if(params.containsKey("displayOptions.emailHeaderImageUrl")){
			this.emailHeaderImageUrl = params.get("displayOptions.emailHeaderImageUrl");
		}
		if(params.containsKey("displayOptions.emailMarketingImageUrl")){
			this.emailMarketingImageUrl = params.get("displayOptions.emailMarketingImageUrl");
		}
		if(params.containsKey("displayOptions.headerImageUrl")){
			this.headerImageUrl = params.get("displayOptions.headerImageUrl");
		}
		if(params.containsKey("displayOptions.businessName")){
			this.businessName = params.get("displayOptions.businessName");
		}
	}

	/**
	 * @return the emailHeaderImageUrl
	 */
	public String getEmailHeaderImageUrl() {
		return emailHeaderImageUrl;
	}

	/**
	 * @param emailHeaderImageUrl the emailHeaderImageUrl to set
	 */
	public void setEmailHeaderImageUrl(String emailHeaderImageUrl) {
		this.emailHeaderImageUrl = emailHeaderImageUrl;
	}

	/**
	 * @return the emailMarketingImageUrl
	 */
	public String getEmailMarketingImageUrl() {
		return emailMarketingImageUrl;
	}

	/**
	 * @param emailMarketingImageUrl the emailMarketingImageUrl to set
	 */
	public void setEmailMarketingImageUrl(String emailMarketingImageUrl) {
		this.emailMarketingImageUrl = emailMarketingImageUrl;
	}

	/**
	 * @return the headerImageUrl
	 */
	public String getHeaderImageUrl() {
		return headerImageUrl;
	}

	/**
	 * @param headerImageUrl the headerImageUrl to set
	 */
	public void setHeaderImageUrl(String headerImageUrl) {
		this.headerImageUrl = headerImageUrl;
	}

	/**
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}

	/**
	 * @param businessName the businessName to set
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String serialize() throws UnsupportedEncodingException{
		StringBuilder outString = new StringBuilder();
		boolean isFirst = true;
		if(this.emailHeaderImageUrl != null && this.emailHeaderImageUrl.length() > 0) {
			outString.append(ParameterUtils.createUrlParameter("displayOptions.emailHeaderImageUrl", this.emailHeaderImageUrl));
			isFirst = false;
		}
		if(this.emailMarketingImageUrl != null && this.emailMarketingImageUrl.length() > 0) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter("displayOptions.emailMarketingImageUrl", this.emailMarketingImageUrl));
			isFirst = false;
		}
		if(this.headerImageUrl != null && this.headerImageUrl.length() > 0) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter("displayOptions.headerImageUrl", this.headerImageUrl));
			isFirst = false;
		}
		if(this.businessName != null && this.businessName.length() > 0) {
			if(!isFirst) outString.append(ParameterUtils.PARAM_SEP);
			outString.append(ParameterUtils.createUrlParameter("displayOptions.businessName", this.businessName));
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
