package com.paypal.adaptive.api.requests;

import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.EndPointUrl;
import com.paypal.adaptive.core.RequestEnvelope;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.core.accounts.EndPointUrlAdaptiveAccounts;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.RequestFailureException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class PayPalBaseRequest {
    private static final Logger log = Logger.getLogger(PayPalBaseRequest.class.getName());

    /**
     * The sandbox is quite slow and thus long wait times have to be implemented
     * I've found 7 seconds won't work for the sandbox
     */
    public static int HTTP_CONNECTION_TIMEOUT = 15000;
    public static int HTTP_READ_TIMEOUT = 15000;
    public static boolean DISABLE_SSL_CERT_CHECK = false;
    
    protected ServiceEnvironment env;
    protected RequestEnvelope requestEnvelope;
    
    private boolean useAdaptiveAccountsEndPoint = false;

    protected String makeRequest(APICredential credentialObj, String apiMethod, String postData)
            throws MissingParameterException, InvalidResponseDataException,
            RequestFailureException, IOException {
        String responseString = "";
        try {
            URL url = getEndPointUrl(apiMethod);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setConnectTimeout(HTTP_CONNECTION_TIMEOUT);
            connection.setReadTimeout(HTTP_READ_TIMEOUT);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("X-PAYPAL-SECURITY-USERID", credentialObj.getAPIUsername());
            connection.setRequestProperty("X-PAYPAL-SECURITY-PASSWORD", credentialObj.getAPIPassword());
            connection.setRequestProperty("X-PAYPAL-SECURITY-SIGNATURE", credentialObj.getSignature());
            connection.setRequestProperty("X-PAYPAL-REQUEST-DATA-FORMAT", "NV");
            connection.setRequestProperty("X-PAYPAL-RESPONSE-DATA-FORMAT", "NV");
            connection.setRequestProperty("X-PAYPAL-APPLICATION-ID", credentialObj.getAppId());
            connection.setRequestProperty("X-PAYPAL-REQUEST-SOURCE", "GAE-JAVA_Toolkit");

            if (log.isLoggable(Level.INFO))
                log.info("Connection: " + connection);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(postData);
            writer.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String inputLine;
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                while ((inputLine = reader.readLine()) != null) {
                    responseString += inputLine;
                }
                reader.close();
                if (responseString.length() <= 0) {
                    throw new InvalidResponseDataException(responseString);
                }
                if (log.isLoggable(Level.INFO))
                    log.info("Received Response: " + responseString);
            } else {
                throw new RequestFailureException(connection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
        return responseString;
    }

    private URL getEndPointUrl(String apiMethod) throws MalformedURLException {
        URL url = null;
        if (useAdaptiveAccountsEndPoint == true) {
            url = new URL(EndPointUrlAdaptiveAccounts.get(env) + apiMethod);    
        } else {
            url = new URL(EndPointUrl.get(env) + apiMethod);
        }
        return url;
    }
    
    protected void setUseAdaptiveAccountEndpoint(boolean useAdaptiveAccountsEndPoint) {
        this.useAdaptiveAccountsEndPoint = useAdaptiveAccountsEndPoint;
    }
}
