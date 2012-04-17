package com.paypal.adaptive.api.requests.fnapi;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.paypal.adaptive.api.responses.GetVerifiedStatusResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.core.accounts.AccountStatus;
import com.paypal.adaptive.core.accounts.CountryCode;
import com.paypal.adaptive.core.accounts.MatchCriteria;
import com.paypal.adaptive.core.accounts.UserInfo;
import com.paypal.adaptive.exceptions.InvalidAPICredentialsException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingAPICredentialsException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.RequestAlreadyMadeException;
import com.paypal.adaptive.exceptions.RequestFailureException;

public class GetVerifiedStatusTest {

    private PropertiesConfiguration config;

    @Before
    public void setUp() throws Exception {
        URL url = GetVerifiedStatusTest.class.getProtectionDomain().getCodeSource().getLocation();
        String execPath = url.getPath();
        String pathConfig = execPath.replace("target/classes/", "properties/credentials.properties");
        
        File configurationFile = new File(pathConfig);
        config = new PropertiesConfiguration(configurationFile);
        
        String pathLog = execPath.replace("target/classes/", "properties/log4j.properties");
        PropertyConfigurator.configure(pathLog);
    }

    @After
    public void tearDown() throws Exception {
        config.clear();
        config = null;
    }

    @Test
    public void testMakeRequest() {        
        String emailAddress = "brando_1334696635_per@arcbees.com";
        String firstName = "Brandon";
        String lastName = "Donnelson";
        MatchCriteria matchCriteria = MatchCriteria.NAME;
        
        GetVerifiedStatus verifiedStatus = null;
        try {
            verifiedStatus = new GetVerifiedStatus(emailAddress, firstName, lastName, matchCriteria);
        } catch (MissingParameterException e) {
            e.printStackTrace();
        }
        
        verifiedStatus.setServiceEnviroment(ServiceEnvironment.SANDBOX);
        verifiedStatus.setCredentialObj(getCredentials());
        verifiedStatus.setLanguage("en_US");
        
        GetVerifiedStatusResponse response = null;
        try {
            response = verifiedStatus.makeRequest();
        } catch (MissingParameterException e) {
            e.printStackTrace();
            fail();
        } catch (RequestAlreadyMadeException e) {
            e.printStackTrace();
            fail();
        } catch (MissingAPICredentialsException e) {
            e.printStackTrace();
            fail();
        } catch (InvalidAPICredentialsException e) {
            e.printStackTrace();
            fail();
        } catch (PayPalErrorException e) {
            e.printStackTrace();
            fail();
        } catch (RequestFailureException e) {
            e.printStackTrace();
            fail();
        } catch (InvalidResponseDataException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        
        assertNotNull(response);
        
        AccountStatus status = response.getAccountStatus();
        assertEquals(AccountStatus.VERIFIED, status);
        
        UserInfo userInfo = response.getUserInfoType();
        
        assertEquals("Brandon", userInfo.getFirstName());
        assertEquals("Donnelson", userInfo.getLastName());
        
        System.out.println("finished");
    }

    private APICredential getCredentials() {
        String accountEmail = config.getString("accountEmail");
        String apiPassword = config.getString("apiPassword");
        String apiUsername = config.getString("apiUsername");
        String appId = config.getString("appId");
        String signature = config.getString("signature");
        
        APICredential cred = new APICredential();
        cred.setAccountEmail(accountEmail);
        cred.setAPIPassword(apiPassword);
        cred.setAPIUsername(apiUsername);
        cred.setAppId(appId);
        cred.setSignature(signature);
        return cred;
    }

}
