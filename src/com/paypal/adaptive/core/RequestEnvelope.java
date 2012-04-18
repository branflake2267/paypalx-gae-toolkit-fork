/**
 * 
 */
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;

/**
 * Java class for RequestEnvelop.
 */
public class RequestEnvelope {

    public static final String DEFAULT_DETAIL_LEVEL_CODE = "ReturnAll";
    protected String errorLanguage;
    protected String detailLevel = DEFAULT_DETAIL_LEVEL_CODE;

    /**
     * @param errorLanguage the errorLanguage to set
     */
    public void setErrorLanguage(String errorLanguage) {
        this.errorLanguage = errorLanguage;
    }

    /**
     * @return the errorLanguage
     */
    public String getErrorLanguage() {
        return errorLanguage;
    }

    /**
     * @param detailLevel the detailLevel to set
     */
    public void setDetailLevel(String detailLevel) {
        this.detailLevel = detailLevel;
    }

    /**
     * @return the detailLevel
     */
    public String getDetailLevel() {
        return detailLevel;
    }

    public String serialize() throws UnsupportedEncodingException {
        return ParameterUtils.createUrlParameter("requestEnvelope.errorLanguage", this.getErrorLanguage());
    }

    public String toString() {
        StringBuilder outStr = new StringBuilder();

        outStr.append("<table>");
        outStr.append("<tr><th>");
        outStr.append(this.getClass().getSimpleName());
        outStr.append("</th><td></td></tr>");
        BeanInfo info;
        try {
            info = Introspector.getBeanInfo(this.getClass(), Object.class);
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                try {
                    String name = pd.getName();
                    Object value = this.getClass().getDeclaredField(name)
                            .get(this);
                    if (value != null) {
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
