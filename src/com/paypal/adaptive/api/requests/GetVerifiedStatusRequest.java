package com.paypal.adaptive.api.requests;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paypal.adaptive.api.responses.GetVerifiedStatusResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.ParameterUtils;
import com.paypal.adaptive.core.RequestEnvelope;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.core.accounts.MatchCriteria;
import com.paypal.adaptive.exceptions.InvalidAPICredentialsException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingAPICredentialsException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.RequestFailureException;

/**
 * Refund API refunds all or part of a payment.
 * 
 */
public class GetVerifiedStatusRequest extends PayPalBaseRequest {
    private static final Logger log = Logger.getLogger(GetVerifiedStatusRequest.class.getName());

    private String emailAddress;
    private String firstName;
    private String lastName;
    private MatchCriteria matchCriteria;

    public GetVerifiedStatusRequest(String language, ServiceEnvironment env) {
        requestEnvelope = new RequestEnvelope();
        requestEnvelope.setErrorLanguage(language);
        this.env = env;
    }

    public GetVerifiedStatusResponse execute(APICredential credentialObj) throws 
        MissingParameterException, 
        MissingAPICredentialsException,
        InvalidAPICredentialsException, 
        PayPalErrorException,
        RequestFailureException, 
        IOException, 
        InvalidResponseDataException {

        if (credentialObj == null) {
            throw new MissingAPICredentialsException();
            
        } else if (credentialObj != null) {
            InvalidAPICredentialsException ex = new InvalidAPICredentialsException();
            
            if (credentialObj.getAppId() == null || credentialObj.getAppId().length() <= 0) {
                ex.addToMissingCredentials("AppId");
            }
            
            if (credentialObj.getAPIPassword() == null || credentialObj.getAPIPassword().length() <= 0) {
                ex.addToMissingCredentials("APIPassword");
            }

            if (credentialObj.getAPIUsername() == null || credentialObj.getAPIUsername().length() <= 0) {
                ex.addToMissingCredentials("APIUsername");
            }
            
            if (credentialObj.getSignature() == null || credentialObj.getSignature().length() <= 0) {
                ex.addToMissingCredentials("Signature");
            }
            
            if (ex.getMissingCredentials() != null) {
                throw ex;
            } else {
                ex = null;
            }
        }
        
        if (emailAddress == null || emailAddress.length() <= 0) 
            throw new MissingParameterException("emailAddress");

        if (firstName == null || firstName.length() <= 0) 
            throw new MissingParameterException("firstName");

        if (lastName == null || lastName.length() <= 0) 
            throw new MissingParameterException("lastName");
        
        if (matchCriteria == null) 
            throw new MissingParameterException("matchCriteria");
        
        // prepare request parameters
        StringBuilder postParameters = new StringBuilder();
        postParameters.append(requestEnvelope.serialize());
        postParameters.append(ParameterUtils.PARAM_SEP);
        postParameters.append(ParameterUtils.createUrlParameter("emailAddress", emailAddress));
        postParameters.append(ParameterUtils.PARAM_SEP);
        postParameters.append(ParameterUtils.createUrlParameter("firstName", firstName));
        postParameters.append(ParameterUtils.PARAM_SEP);
        postParameters.append(ParameterUtils.createUrlParameter("lastName", lastName));
        postParameters.append(ParameterUtils.PARAM_SEP);
        postParameters.append(ParameterUtils.createUrlParameter("matchCriteria", matchCriteria.toString()));
        postParameters.append(ParameterUtils.PARAM_SEP);

        if (log.isLoggable(Level.INFO))
            log.info("Sending GetVerifiedStatus with: " + postParameters.toString());

        setUseAdaptiveAccountEndpoint(true);
        
        // send request
        String responseString = makeRequest(credentialObj, "GetVerifiedStatus", postParameters.toString());

        // parse response
        GetVerifiedStatusResponse response = new GetVerifiedStatusResponse(responseString);
        return response;
    }

    /**
     * Gets the value of the requestEnvelope property.
     * @return possible object is {@link RequestEnvelope }
     */
    public RequestEnvelope getRequestEnvelope() {
        return requestEnvelope;
    }

    /**
     * Sets the value of the requestEnvelope property.
     * @param value allowed object is {@link RequestEnvelope }
     */
    public void setRequestEnvelope(RequestEnvelope value) {
        this.requestEnvelope = value;
    }
    
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public MatchCriteria getMatchCriteria() {
        return matchCriteria;
    }

    public void setMatchCriteria(MatchCriteria matchCriteria) {
        this.matchCriteria = matchCriteria;
    }
    
    /**
     * TODO get rid of the html?
     */
    public String toString() {

        StringBuilder outStr = new StringBuilder();

        outStr.append("<table border=1>");
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
