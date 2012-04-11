package com.picmart;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.paypal.adaptive.api.requests.fnapi.SimplePay;
import com.paypal.adaptive.api.responses.PayResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.AckCode;
import com.paypal.adaptive.core.CurrencyCodes;
import com.paypal.adaptive.core.ExpType;
import com.paypal.adaptive.core.PaymentType;
import com.paypal.adaptive.core.Receiver;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.AuthorizationRequiredException;
import com.paypal.adaptive.exceptions.InvalidAPICredentialsException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingAPICredentialsException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.PaymentExecException;
import com.paypal.adaptive.exceptions.PaymentInCompleteException;
import com.paypal.adaptive.exceptions.RequestAlreadyMadeException;
import com.paypal.adaptive.exceptions.RequestFailureException;

@SuppressWarnings("serial")
public class GetPayKeyServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(GetPayKeyServlet.class.getName());


	private static APICredential credentialObj;
	private static ServiceEnvironment PPEnv = ServiceEnvironment.SANDBOX;

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);

		// Get the value of APIUsername
		String APIUsername = getServletConfig().getInitParameter("PPAPIUsername"); 
		String APIPassword = getServletConfig().getInitParameter("PPAPIPassword"); 
		String APISignature = getServletConfig().getInitParameter("PPAPISignature"); 
		String AppID = getServletConfig().getInitParameter("PPAppID"); 
		String AccountEmail = getServletConfig().getInitParameter("PPAccountEmail");
		String PPEnvironment = getServletConfig().getInitParameter("PPEnvironment");

		if(APIUsername == null || APIUsername.length() <= 0
				|| APIPassword == null || APIPassword.length() <=0 
				|| APISignature == null || APISignature.length() <= 0
				|| AppID == null || AppID.length() <=0 ) {
			// requires API Credentials not set - throw exception
			throw new ServletException("APICredential(s) missing");
		}

		credentialObj = new APICredential();
		credentialObj.setAPIUsername(APIUsername);
		credentialObj.setAPIPassword(APIPassword);
		credentialObj.setSignature(APISignature);
		credentialObj.setAppId(AppID);
		credentialObj.setAccountEmail(AccountEmail);
		if(PPEnvironment != null){
			if(PPEnvironment.equals("BETA_SANDBOX")){
				PPEnv = ServiceEnvironment.BETA_SANDBOX;
			} else if(PPEnvironment.equals("PRODUCTION")){
				PPEnv = ServiceEnvironment.PRODUCTION;
			} else if(PPEnvironment.equals("SANDBOX")){
				PPEnv = ServiceEnvironment.SANDBOX;
			} else if(PPEnvironment.equals("STAGING")){
				PPEnv = ServiceEnvironment.STAGING;
			} else {
				PPEnv = ServiceEnvironment.SANDBOX;
			}
		}
		log.info("Servlet initialized successfully");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {


		try {

			resp.setContentType("application/json; charset=utf-8");
			resp.getWriter().println("{ \"PayResponse\": {");

			String ids = req.getParameter("ids");
			if(ids != null && ids.length() > 0){
				// process order

				try {
					
					// split photo ids to calculate amount
					String[] idTokens = ids.split(",");
					
					double amount = idTokens.length * 0.50;

					StringBuilder url = new StringBuilder();
					url.append(req.getRequestURL());
					String returnURL = url.toString() + "?status=success&payKey=${payKey}";
					String cancelURL = url.toString() + "?status=canceled";
					//String ipnURL = url.toString() + "?action=ipn";

					SimplePay simplePayment = new SimplePay();
					simplePayment.setCancelUrl(cancelURL);
					simplePayment.setReturnUrl(returnURL);
					simplePayment.setCredentialObj(credentialObj);
					simplePayment.setUserIp(req.getRemoteAddr());
					simplePayment.setApplicationName("Sample app for inline payments");
					simplePayment.setCurrencyCode(CurrencyCodes.USD);
					simplePayment.setEnv(PPEnv);
					//parallelPay.setIpnURL(ipnURL);
					simplePayment.setLanguage("en_US");
					simplePayment.setMemo(ids);

					// set the receiver
					Receiver primaryReceiver = new Receiver();
					primaryReceiver.setAmount(amount);
					primaryReceiver.setEmail(credentialObj.getAccountEmail());
					primaryReceiver.setPaymentType(PaymentType.DIGITALGOODS);
					simplePayment.setReceiver(primaryReceiver);


					PayResponse payResponse = simplePayment.makeRequest();
					log.info("Payment success - payKey:" + payResponse.getPayKey());
					resp.getWriter().println("\"Status\": \"" + payResponse.getPaymentExecStatus().toString() + "\"");
				} catch (IOException e) {
					//resp.getWriter().println("Payment Failed w/ IOException");

					resp.getWriter().println("\"Status\": \"IOException\"");

				} catch (MissingAPICredentialsException e) {
					// No API Credential Object provided - log error
					e.printStackTrace();
					//resp.getWriter().println("No APICredential object provided");
					resp.getWriter().println("\"Status\": \"MissingAPICredentials\"");
				} catch (InvalidAPICredentialsException e) {
					// invalid API Credentials provided - application error - log error
					e.printStackTrace();
					//resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
					resp.getWriter().println("\"Status\": \"InvalidAPICredentials\"");
				} catch (MissingParameterException e) {
					// missing parameter - log  error
					e.printStackTrace();
					//resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
					resp.getWriter().println("\"Status\": \"MissingParameter\"");
				} catch (RequestFailureException e) {
					// HTTP Error - some connection issues ?
					e.printStackTrace();
					//resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
					resp.getWriter().println("\"Status\": \"RequestFailure\"");
				} catch (InvalidResponseDataException e) {
					// PayPal service error 
					// log error
					e.printStackTrace();
					//resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
					resp.getWriter().println("\"Status\": \"InvalidResponseData\"");
				} catch (PayPalErrorException e) {
					// Request failed due to a Service/Application error
					e.printStackTrace();
					if(e.getResponseEnvelope().getAck() == AckCode.Failure){
						// log the error
						/*resp.getWriter().println("Received Failure from PayPal (ack)");
						resp.getWriter().println("ErrorData provided:");
						resp.getWriter().println(e.getPayErrorList().toString());
						if(e.getPaymentExecStatus() != null){
							resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
						}
						 */
						resp.getWriter().println("\"Status\": \"" + e.getResponseEnvelope().getAck().toString() + "\"");
					} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
						// there is a warning - log it!
						/*resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
						resp.getWriter().println("ErrorData provided:");
						resp.getWriter().println(e.getPayErrorList().toString());
						 */
						resp.getWriter().println("\"Status\": \"" + e.getResponseEnvelope().getAck().toString() + "\"");
					}
				} catch (RequestAlreadyMadeException e) {
					// shouldn't occur - log the error
					e.printStackTrace();
					//resp.getWriter().println("Request to send a request that has already been sent!");
					resp.getWriter().println("\"Status\": \"RequestAlreadyMade\"");

				} catch (PaymentExecException e) {

					//resp.getWriter().println("Failed Payment Request w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
					//resp.getWriter().println("ErrorData provided:");

					//resp.getWriter().println(e.getPayErrorList().toString());
					resp.getWriter().println("\"Status\": \"" + e.getPaymentExecStatus().toString() + "\"");
				}catch (PaymentInCompleteException e){
					//resp.getWriter().println("Incomplete Payment w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
					//resp.getWriter().println("ErrorData provided:");

					//resp.getWriter().println(e.getPayErrorList().toString());	
					resp.getWriter().println("\"Status\": \"" + e.getPayErrorList().toString() + "\"");
				} catch (NumberFormatException e) {
					// invalid number passed
					e.printStackTrace();
					//resp.getWriter().println("Invalid number of receivers sent");
					resp.getWriter().println("\"Status\": \"NumberFormatError\"");
				} catch (AuthorizationRequiredException e) {
					// redirect the user to PayPal for Authorization
					//resp.sendRedirect(e.getAuthorizationUrl(ServiceEnvironment.SANDBOX));

					resp.getWriter().println("\"PPAuthzUrl\": \"" + e.getEmbeddedPaymentsAuthorizationUrl(PPEnv, ExpType.LIGHTBOX) + "\", \"Status\": \"CREATED\"");
				}

			} else {
				// return error
				resp.getWriter().println("\"Status\": \"No pictures selected\"");
			}
			resp.getWriter().println(" }}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		try {
			String status = req.getParameter("status"); 

			if(status != null && status.equals("canceled")) {
				// user canceled the payment
				req.setAttribute("status", "canceled");
				getServletConfig().getServletContext().getRequestDispatcher("/paymentcancel.jsp").forward(req, resp);

			} else if(status != null && status.equals("success")){
				req.setAttribute("status", "success");
				getServletConfig().getServletContext().getRequestDispatcher("/paymentsuccess.jsp").forward(req, resp);
			} else {
				getServletConfig().getServletContext().getRequestDispatcher("/gallery.jsp").forward(req, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
