
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * <p>Java class for ErrortData.
 */
public class ErrorData {

    protected long errorId;
    protected String domain;
    protected String subdomain;
    protected ErrorSeverity severity;
    protected ErrorCategory category;
    protected String message;
    protected String exceptionId;
    protected List<ErrorParameter> parameter;

    
    public ErrorData(HashMap<String, String> params, int index){
    	if(params.containsKey("error(" + index +").errorId"))
    		this.errorId = Long.parseLong(params.get("error(" + index +").errorId"));
    	if(params.containsKey("error(" + index +").domain"))
    		this.domain = params.get("error(" + index +").domain");
    	if(params.containsKey("error(" + index +").subDomain"))
    		this.subdomain = params.get("error(" + index +").subDomain");
        if(params.containsKey("error(" + index +").severity"))
        	this.severity = ErrorSeverity.valueOf(params.get("error(" + index +").severity"));
        if(params.containsKey("error(" + index +").category"))
        	this.category = ErrorCategory.valueOf(params.get("error(" + index +").category"));
        if(params.containsKey("error(" + index +").message"))
    		this.message = params.get("error(" + index +").message");
        if(params.containsKey("error(" + index +").exceptionId"))
    		this.exceptionId = params.get("error(" + index +").exceptionId");
        
        parameter = new ArrayList<ErrorParameter>();
        
        for(int i = 0; i< 10; i+=2) {
	        if(params.containsKey("error(" + index +").parameter(" + i + ")") 
	        		|| params.containsKey("error(" + index +").parameter(" + (i+1) + ")"))  {
		        ErrorParameter errParam = new ErrorParameter();
		        if(params.containsKey("error(" + index +").parameter(" + i + ")"))
		        	errParam.setName(params.get("error(" + index +").parameter(" + i + ")"));
		        if(params.containsKey("error(" + index +").parameter(" + (i+1) + ")"))
		        	errParam.setValue(params.get("error(" + index +").parameter(" + (i+1) + ")"));
		        
		        parameter.add(errParam);
	        } else {
	        	break;
	        }
        }
        
    }
    /**
     * Gets the value of the errorId property.
     * 
     */
    public long getErrorId() {
        return errorId;
    }

    /**
     * Sets the value of the errorId property.
     * 
     */
    public void setErrorId(long value) {
        this.errorId = value;
    }

    /**
     * Gets the value of the domain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets the value of the domain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomain(String value) {
        this.domain = value;
    }

    /**
     * Gets the value of the subdomain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubdomain() {
        return subdomain;
    }

    /**
     * Sets the value of the subdomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubdomain(String value) {
        this.subdomain = value;
    }

    /**
     * Gets the value of the severity property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorSeverity }
     *     
     */
    public ErrorSeverity getSeverity() {
        return severity;
    }

    /**
     * Sets the value of the severity property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorSeverity }
     *     
     */
    public void setSeverity(ErrorSeverity value) {
        this.severity = value;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorCategory }
     *     
     */
    public ErrorCategory getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorCategory }
     *     
     */
    public void setCategory(ErrorCategory value) {
        this.category = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the exceptionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExceptionId() {
        return exceptionId;
    }

    /**
     * Sets the value of the exceptionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExceptionId(String value) {
        this.exceptionId = value;
    }

    /**
     * Gets the value of the parameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ErrorParameter }
     * 
     * 
     */
    public List<ErrorParameter> getParameter() {
        if (parameter == null) {
            parameter = new ArrayList<ErrorParameter>();
        }
        return this.parameter;
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
