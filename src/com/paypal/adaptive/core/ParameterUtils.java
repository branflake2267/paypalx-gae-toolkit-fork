package com.paypal.adaptive.core;

import java.io.UnsupportedEncodingException;

/**
 * Java class that helps in create NVP parameters on the wire..
 */
public class ParameterUtils {

	public static final String PARAM_SEP = "&";
	public static final String NAMVAL_SEP = "=";
	
	public static String createUrlParameter(String name, String value) throws UnsupportedEncodingException{
		return name + NAMVAL_SEP + java.net.URLEncoder.encode(value, "UTF-8");
	}
}
