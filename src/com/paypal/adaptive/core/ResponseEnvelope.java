/**
 * 
 */
package com.paypal.adaptive.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;

/**
 * <p>
 * Java class for ResponseEnvelope.
 * 
 */
public class ResponseEnvelope {

    protected AckCode ack;
    protected String build;
    protected String correlationId;
    protected String timestamp;

    public static String TIMESTAMP_PARAM = "responseEnvelope.timestamp";
    public static String ACK_PARAM = "responseEnvelope.ack";
    public static String CORRELATIONID_PARAM = "responseEnvelope.correlationId";
    public static String BUILD_PARAM = "responseEnvelope.build";

    public ResponseEnvelope(HashMap<String, String> params) {
        if (params.containsKey(TIMESTAMP_PARAM))
            this.timestamp = params.get(TIMESTAMP_PARAM);
        if (params.containsKey(ACK_PARAM))
            this.ack = AckCode.valueOf(params.get(ACK_PARAM));
        if (params.containsKey(CORRELATIONID_PARAM))
            this.correlationId = params.get(CORRELATIONID_PARAM);
        if (params.containsKey(BUILD_PARAM))
            this.build = params.get(BUILD_PARAM);
    }

    /**
     * @param build the build to set
     */
    public void setBuild(String build) {
        this.build = build;
    }

    /**
     * @return the build
     */
    public String getBuild() {
        return build;
    }

    /**
     * @param correlationId the correlationId to set
     */
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    /**
     * @return the correlationId
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @param ack the ack to set
     */
    public void setAck(AckCode ack) {
        this.ack = ack;
    }

    /**
     * @return the ack
     */
    public AckCode getAck() {
        return ack;
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
