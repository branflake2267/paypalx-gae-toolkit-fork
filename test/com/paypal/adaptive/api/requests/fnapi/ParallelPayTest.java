package com.paypal.adaptive.api.requests.fnapi;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.CurrencyCodes;
import com.paypal.adaptive.core.PaymentType;
import com.paypal.adaptive.core.Receiver;
import com.paypal.adaptive.exceptions.NotEnoughReceivers;

public class ParallelPayTest {

    private PropertiesConfiguration config;

    @Before
    public void setUp() throws Exception {
        URL url = ParallelPayTest.class.getProtectionDomain().getCodeSource().getLocation();
        String execPath = url.getPath();
        String pathConfig = execPath.replace("target/test-classes/", "properties/credentials.properties");
        
        File configurationFile = new File(pathConfig);
        config = new PropertiesConfiguration(configurationFile);
        
        String pathLog = execPath.replace("target/test-classes/", "properties/log4j.properties");
        PropertyConfigurator.configure(pathLog);
    }

    @After
    public void tearDown() throws Exception {
        config.clear();
        config = null;
    }

    @Test
    public void testMakeRequest() {        
        ParallelPay pp = null;
        try {
            pp = new ParallelPay(2);
        } catch (NotEnoughReceivers e) {
            e.printStackTrace();
            assertTrue(false);
            return;
        }
        pp.setCancelUrl("http://arcbees.com");
        pp.setReturnUrl("http://arcbees.com");
        pp.setUserIp("0.0.0.0");
        pp.setCredentialObj(getCredentials());
        pp.setCurrencyCode(CurrencyCodes.USD);
        pp.setApplicationName("Arcbees Testing");
        pp.setLanguage("en_US");
        pp.setMemo("Testing parallel pay");
        pp.setSenderEmail(config.getString("senderEmail"));
        
        Receiver receiver1 = new Receiver();
        receiver1.setAmount(10);
        receiver1.setEmail(config.getString("receiver1"));
        //receiver1.setInvoiceId(invoiceId);
        receiver1.setPaymentType(PaymentType.GOODS);
        pp.addToReceivers(receiver1);
        
        Receiver receiver2 = new Receiver();
        receiver2.setAmount(10);
        receiver2.setEmail(config.getString("receiver2"));
        //receiver1.setInvoiceId(invoiceId);
        receiver2.setPaymentType(PaymentType.GOODS);
        pp.addToReceivers(receiver2);
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
