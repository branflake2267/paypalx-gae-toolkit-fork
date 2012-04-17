package com.paypal.adaptive.api.responses;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;

import com.paypal.adaptive.core.PayError;
import com.paypal.adaptive.core.ResponseEnvelope;
import com.paypal.adaptive.core.accounts.AccountStatus;
import com.paypal.adaptive.core.accounts.CountryCode;
import com.paypal.adaptive.core.accounts.UserInfoType;
import com.paypal.adaptive.exceptions.PayPalErrorException;

/**
 * GetVerifiedStatus Response
 * https://cms.paypal.com/us/cgi-bin/?cmd=_render-content&content_ID=developer/e_howto_api_ACGetVerifiedStatusAPI
 */
public class GetVerifiedStatusResponse {

    private HashMap<String, String> responseParams;
    private ResponseEnvelope responseEnvelope;
    private ArrayList<PayError> errorList;
    
    private AccountStatus accountStatus;
    private CountryCode countryCode;
    private UserInfoType userInfoType;

    public GetVerifiedStatusResponse(String responseString) throws PayPalErrorException {
        responseParams = new HashMap<String, String>();
        
        // parse the string and load into the object
        String[] nmValPairs = responseString.split("&");
        for (String nmVal : nmValPairs) {
            String[] field = nmVal.split("=");
            responseParams.put(field[0], (field.length > 1) ? field[1].trim() : "");
        }

        if (responseParams.containsKey("accountStatus")) {
            accountStatus = AccountStatus.fromValue(responseParams.get("accountStatus"));
        }
        
        if (responseParams.containsKey("countryCode")) {
            countryCode = CountryCode.fromValue(responseParams.get("countryCode"));
        }
        
        if (responseParams.containsKey("accountType")) {
            userInfoType = new UserInfoType(responseParams);
        }
        
        responseEnvelope = new ResponseEnvelope(responseParams);
        
        errorList = new ArrayList<PayError>();
        
        // we will parse 10 errors for now
        for (int i = 0; i < 10; i++) {
            if (responseParams.containsKey("error(" + i + ").errorId")) {
                PayError error = new PayError(responseParams, i);
                errorList.add(error);
            } else {
                break;
            }
        }
    }

    public ArrayList<PayError> getErrorList() {
        return errorList;
    }

    /**
     * Gets the value of the responseEnvelope property.
     * @return possible object is {@link ResponseEnvelope }
     */
    public ResponseEnvelope getResponseEnvelope() {
        return responseEnvelope;
    }
    
    public HashMap<String, String> getResponseParams() {
        return responseParams;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public UserInfoType getUserInfoType() {
        return userInfoType;
    }

    /**
     * TODO remove html?
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
