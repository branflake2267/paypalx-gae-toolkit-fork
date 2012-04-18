package com.paypal.adaptive.api.requests.fnapi;

import java.io.IOException;

import com.paypal.adaptive.api.requests.GetVerifiedStatusRequest;
import com.paypal.adaptive.api.responses.GetVerifiedStatusResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.core.accounts.MatchCriteria;
import com.paypal.adaptive.exceptions.InvalidAPICredentialsException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingAPICredentialsException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.RequestAlreadyMadeException;
import com.paypal.adaptive.exceptions.RequestFailureException;

/**
 * GetVerifiedStatus API Operation
 * https://cms.paypal.com/us/cgi-bin/?cmd=_render-content&content_ID=developer/e_howto_api_ACGetVerifiedStatusAPI
 */
public class GetVerifiedStatus {
    /**
     * Required APICredential
     */
    private APICredential credentialObj;

    /**
     * Required language for localization
     * (Required) The RFC 3066 language in which error messages are returned; by default it is en_US, which is the only language currently supported
     */
    private String language = "en_US";

    /**
     * Required Environment
     */
    private ServiceEnvironment env;

    /**
     * (Required) The email address of the PayPal account holder.
     */
    private String emailAddress;

    /**
     * (Required) The first name of the PayPal account holder. Required if
     * matchCriteria is NAME.
     */
    private String firstName;

    /**
     * The last name of the PayPal account holder. Required if matchCriteria is
     * NAME.
     */
    private String lastName;

    /**
     * (Required) The criteria that must be matched in addition to emailAddress.
     * Currently, only NAME is supported. NOTE:To use ConfirmationType NONE you
     * must request and be granted advanced permission levels.
     */
    private MatchCriteria matchCriteria;

    private boolean requestProcessed = false;

    /**
     * The GetVerifiedStatus request allows you to verify that a customer is
     * indeed the holder of the PayPal account information that was supplied.
     * 
     * @param emailAddress Is Required
     * @param firstName Is Required
     * @param lastName Is Required
     * @throws MissingParameterException of the parameter which is missing
     */
    public GetVerifiedStatus(String emailAddress, String firstName, String lastName, MatchCriteria matchCriteria)
            throws MissingParameterException {

        if (emailAddress == null || emailAddress.length() <= 0)
            throw new MissingParameterException("emailAddress");

        if (firstName == null || firstName.length() <= 0)
            throw new MissingParameterException("firstName");

        if (lastName == null || lastName.length() <= 0)
            throw new MissingParameterException("lastName");

        if (matchCriteria == null)
            throw new MissingParameterException("matchCriteria");

        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.matchCriteria = matchCriteria;
    }

    public GetVerifiedStatusResponse makeRequest()
            throws MissingParameterException, RequestAlreadyMadeException,
            MissingAPICredentialsException, InvalidAPICredentialsException,
            PayPalErrorException, RequestFailureException,
            InvalidResponseDataException, IOException {

        validate();

        GetVerifiedStatusRequest request = new GetVerifiedStatusRequest(language, env);
        request.setEmailAddress(emailAddress);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setMatchCriteria(matchCriteria);

        GetVerifiedStatusResponse response = request.execute(credentialObj);
        return response;
    }

    public void setCredentialObj(APICredential credentialObj) {
        this.credentialObj = credentialObj;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setServiceEnviroment(ServiceEnvironment env) {
        this.env = env;
    }

    private void validate() throws MissingParameterException,
            RequestAlreadyMadeException {
        if (requestProcessed)
            throw new RequestAlreadyMadeException();

        if (language == null)
            throw new MissingParameterException("language");

        if (env == null)
            throw new MissingParameterException("ServiceEnvironment");
    }
}
