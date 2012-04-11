/**
 * 
 */
package com.adaptivesample;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.paypal.adaptive.api.requests.PaymentDetailsRequest;
import com.paypal.adaptive.api.requests.PreapprovalDetailsRequest;
import com.paypal.adaptive.api.requests.fnapi.ChainedPay;
import com.paypal.adaptive.api.requests.fnapi.ConvertCurrency;
import com.paypal.adaptive.api.requests.fnapi.CreatePreapprovalForPeriodicPayments;
import com.paypal.adaptive.api.requests.fnapi.CreateSimplePreapproval;
import com.paypal.adaptive.api.requests.fnapi.ImplicitChainedPay;
import com.paypal.adaptive.api.requests.fnapi.ImplicitParallelPay;
import com.paypal.adaptive.api.requests.fnapi.ImplicitSimplePay;
import com.paypal.adaptive.api.requests.fnapi.ParallelPay;
import com.paypal.adaptive.api.requests.fnapi.PreapprovedChainedPay;
import com.paypal.adaptive.api.requests.fnapi.PreapprovedParallelPay;
import com.paypal.adaptive.api.requests.fnapi.PreapprovedSimplePay;
import com.paypal.adaptive.api.requests.fnapi.RefundCompletePayment;
import com.paypal.adaptive.api.requests.fnapi.RefundPartialPayment;
import com.paypal.adaptive.api.requests.fnapi.RefundTransaction;
import com.paypal.adaptive.api.requests.fnapi.SimplePay;
import com.paypal.adaptive.api.responses.ConvertCurrencyResponse;
import com.paypal.adaptive.api.responses.PayResponse;
import com.paypal.adaptive.api.responses.PaymentDetailsResponse;
import com.paypal.adaptive.api.responses.PreapprovalDetailsResponse;
import com.paypal.adaptive.api.responses.PreapprovalResponse;
import com.paypal.adaptive.api.responses.RefundResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.AckCode;
import com.paypal.adaptive.core.CurrencyCodes;
import com.paypal.adaptive.core.CurrencyType;
import com.paypal.adaptive.core.DayOfWeek;
import com.paypal.adaptive.core.PaymentPeriodType;
import com.paypal.adaptive.core.PaymentType;
import com.paypal.adaptive.core.PinType;
import com.paypal.adaptive.core.Receiver;
import com.paypal.adaptive.core.RefundInfo;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.AuthorizationRequiredException;
import com.paypal.adaptive.exceptions.InvalidAPICredentialsException;
import com.paypal.adaptive.exceptions.InvalidPrimaryReceiverAmountException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingAPICredentialsException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.NotEnoughReceivers;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.PaymentExecException;
import com.paypal.adaptive.exceptions.PaymentInCompleteException;
import com.paypal.adaptive.exceptions.PaymentTypeNotAllowedException;
import com.paypal.adaptive.exceptions.ReceiversCountMismatchException;
import com.paypal.adaptive.exceptions.RequestAlreadyMadeException;
import com.paypal.adaptive.exceptions.RequestFailureException;

/**
 * @author palavilli
 * 
 */
public class AdaptiveRequests {

	
	public static void processSimplePayRequest(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException {

		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Simple Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {

			StringBuilder url = new StringBuilder();
			url.append(req.getRequestURL());
			String returnURL = url.toString() + "?return=1&action=pay&payKey=${payKey}";
			String cancelURL = url.toString() + "?action=pay&cancel=1";
			String ipnURL = url.toString() + "?action=ipn";
			
			SimplePay simplePay = new SimplePay();
			simplePay.setCancelUrl(cancelURL);
			simplePay.setReturnUrl(returnURL);
			simplePay.setCredentialObj(credentialObj);
			simplePay.setUserIp(req.getRemoteAddr());
			simplePay.setApplicationName("Sample app on GAE");
			simplePay.setCurrencyCode(CurrencyCodes.valueOf(req.getParameter("currencyCode")));
			simplePay.setEnv(ServiceEnvironment.SANDBOX);
			simplePay.setIpnURL(ipnURL);
			simplePay.setLanguage("en_US");
			simplePay.setMemo(req.getParameter("memo"));
			Receiver receiver = new Receiver();
			receiver.setAmount(Double.parseDouble(req.getParameter("amount")));
			receiver.setEmail(req.getParameter("receiveremail"));
			receiver.setPaymentType(PaymentType.valueOf(req.getParameter("paymentType")));
			simplePay.setReceiver(receiver);
			simplePay.setSenderEmail(req.getParameter("email"));
			
			PayResponse payResponse = simplePay.makeRequest();
			resp.getWriter().println("PaymentExecStatus:" + payResponse.getPaymentExecStatus().toString());
			
		} catch (IOException e) {
			resp.getWriter().println("Payment Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		} catch (PaymentExecException e) {

			resp.getWriter().println("Failed Payment Request w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());
		}catch (PaymentInCompleteException e){
			resp.getWriter().println("Incomplete Payment w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());			
		} catch (AuthorizationRequiredException e) {
			// redirect the user to PayPal for Authorization
			resp.sendRedirect(e.getAuthorizationUrl(ServiceEnvironment.SANDBOX));
		}

		resp.getWriter().println("</body></html>");
	}
	
	public static void processImplicitSimplePayRequest(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException {

		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Simple Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {

			StringBuilder url = new StringBuilder();
			url.append(req.getRequestURL());
			String ipnURL = url.toString() + "?action=ipn";
			
			ImplicitSimplePay implicitSimplePay = new ImplicitSimplePay();
			implicitSimplePay.setCredentialObj(credentialObj);
			implicitSimplePay.setUserIp(req.getRemoteAddr());
			implicitSimplePay.setApplicationName("Sample app on GAE");
			implicitSimplePay.setCurrencyCode(CurrencyCodes.valueOf(req.getParameter("currencyCode")));
			implicitSimplePay.setEnv(ServiceEnvironment.SANDBOX);
			implicitSimplePay.setIpnURL(ipnURL);
			implicitSimplePay.setLanguage("en_US");
			implicitSimplePay.setMemo(req.getParameter("memo"));
			Receiver receiver = new Receiver();
			receiver.setAmount(Double.parseDouble(req.getParameter("amount")));
			receiver.setEmail(req.getParameter("receiveremail"));
			receiver.setPaymentType(PaymentType.valueOf(req.getParameter("paymentType")));
			implicitSimplePay.setReceiver(receiver);
			
			PayResponse payResponse = implicitSimplePay.makeRequest();

			resp.getWriter().println("PaymentExecStatus:" + payResponse.getPaymentExecStatus().toString());

		} catch (IOException e) {
			resp.getWriter().println("Payment Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		} catch (PaymentExecException e) {

			resp.getWriter().println("Failed Payment Request w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());
		}catch (PaymentInCompleteException e){
			resp.getWriter().println("Incomplete Payment w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());						
		} catch (AuthorizationRequiredException e) {
			// redirect the user to PayPal for Authorization
			resp.sendRedirect(e.getAuthorizationUrl(ServiceEnvironment.SANDBOX));
		}

		resp.getWriter().println("</body></html>");
	}

	public static void processParallelPayRequest(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException  {
		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Simple Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {

			StringBuilder url = new StringBuilder();
			url.append(req.getRequestURL());
			String returnURL = url.toString() + "?return=1&action=pay&payKey=${payKey}";
			String cancelURL = url.toString() + "?action=pay&cancel=1";
			String ipnURL = url.toString() + "?action=ipn";
			
			ParallelPay parallelPay = new ParallelPay(Integer.parseInt(req.getParameter("numberOfReceivers")));
			parallelPay.setCancelUrl(cancelURL);
			parallelPay.setReturnUrl(returnURL);
			parallelPay.setCredentialObj(credentialObj);
			parallelPay.setUserIp(req.getRemoteAddr());
			parallelPay.setApplicationName("Sample app on GAE");
			parallelPay.setCurrencyCode(CurrencyCodes.valueOf(req.getParameter("currencyCode")));
			parallelPay.setEnv(ServiceEnvironment.SANDBOX);
			parallelPay.setIpnURL(ipnURL);
			parallelPay.setLanguage("en_US");
			parallelPay.setMemo(req.getParameter("memo"));
			
			// set the receivers
			String[] receiverEmailItems = req.getParameterValues("receiveremail");
			String[] amountItems = req.getParameterValues("amount");
			for (int i = 0; i < receiverEmailItems.length; i++) {
				String recreceiverEmail = receiverEmailItems[i];
				if (recreceiverEmail != null
						&& recreceiverEmail.length() != 0) {
					Receiver rec1 = new Receiver();
					rec1.setAmount(Double.parseDouble(amountItems[i]));
					rec1.setEmail(receiverEmailItems[i]);
					rec1.setPaymentType(PaymentType.valueOf(req.getParameter("paymentType")));
					parallelPay.addToReceivers(rec1);
				}
			}
			
			parallelPay.setSenderEmail(req.getParameter("email"));
			
			PayResponse payResponse = parallelPay.makeRequest();

			resp.getWriter().println("PaymentExecStatus:" + payResponse.getPaymentExecStatus().toString());

		} catch (IOException e) {
			resp.getWriter().println("Payment Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch(ReceiversCountMismatchException e){
			// missing receiver - log  error
			e.printStackTrace();
			resp.getWriter().println("Receiver count did not match - expected: " 
					+ e.getExpectedNumberOfReceivers() 
					+ " - actual:" + e.getActualNumberOfReceivers());			
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		} catch (PaymentExecException e) {

			resp.getWriter().println("Failed Payment Request w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());
		}catch (PaymentInCompleteException e){
			resp.getWriter().println("Incomplete Payment w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());			
		} catch (NumberFormatException e) {
			// invalid number passed
			e.printStackTrace();
			resp.getWriter().println("Invalid number of receivers sent");

		} catch (NotEnoughReceivers e) {
			// not enough receivers - min requirements for Parallel pay not met
			e.printStackTrace();
			resp.getWriter().println("Min number of receivers not met - Min Required:"
					+ e.getMinimumRequired() + " - actual set:" + e.getActualNumber());
		} catch (AuthorizationRequiredException e) {
			// redirect the user to PayPal for Authorization
			resp.sendRedirect(e.getAuthorizationUrl(ServiceEnvironment.SANDBOX));
		}

		resp.getWriter().println("</body></html>");
		
	}
	public static void processImplicitParallelPayRequest(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException  {
		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Simple Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {

			StringBuilder url = new StringBuilder();
			url.append(req.getRequestURL());
			String ipnURL = url.toString() + "?action=ipn";
			
			ImplicitParallelPay implicitParallelPay = new ImplicitParallelPay(Integer.parseInt(req.getParameter("numberOfReceivers")));
			
			implicitParallelPay.setCredentialObj(credentialObj);
			implicitParallelPay.setClientIp(req.getRemoteAddr());
			implicitParallelPay.setApplicationName("Sample app on GAE");
			implicitParallelPay.setCurrencyCode(CurrencyCodes.valueOf(req.getParameter("currencyCode")));
			implicitParallelPay.setEnv(ServiceEnvironment.SANDBOX);
			implicitParallelPay.setIpnURL(ipnURL);
			implicitParallelPay.setLanguage("en_US");
			implicitParallelPay.setMemo(req.getParameter("memo"));
			
			// set the receivers
			String[] receiverEmailItems = req.getParameterValues("receiveremail");
			String[] amountItems = req.getParameterValues("amount");
			for (int i = 0; i < receiverEmailItems.length; i++) {
				String recreceiverEmail = receiverEmailItems[i];
				if (recreceiverEmail != null
						&& recreceiverEmail.length() != 0) {
					Receiver rec1 = new Receiver();
					rec1.setAmount(Double.parseDouble(amountItems[i]));
					rec1.setEmail(receiverEmailItems[i]);
					rec1.setPaymentType(PaymentType.valueOf(req.getParameter("paymentType")));
					implicitParallelPay.addToReceivers(rec1);
				}
			}
						
			PayResponse payResponse = implicitParallelPay.makeRequest();

			resp.getWriter().println("PaymentExecStatus:" + payResponse.getPaymentExecStatus().toString());

		} catch (IOException e) {
			resp.getWriter().println("Payment Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch(ReceiversCountMismatchException e){
			// missing receiver - log  error
			e.printStackTrace();
			resp.getWriter().println("Receiver count did not match - expected: " 
					+ e.getExpectedNumberOfReceivers() 
					+ " - actual:" + e.getActualNumberOfReceivers());			
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		} catch (PaymentExecException e) {

			resp.getWriter().println("Failed Payment Request w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());
		}catch (PaymentInCompleteException e){
			resp.getWriter().println("Incomplete Payment w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());	
		} catch (NumberFormatException e) {
			// invalid number passed
			e.printStackTrace();
			resp.getWriter().println("Invalid number of receivers sent");

		} catch (NotEnoughReceivers e) {
			// not enough receivers - min requirements for Parallel pay not met
			e.printStackTrace();
			resp.getWriter().println("Min number of receivers not met - Min Required:"
					+ e.getMinimumRequired() + " - actual set:" + e.getActualNumber());
		} catch (AuthorizationRequiredException e) {
			// redirect the user to PayPal for Authorization
			resp.sendRedirect(e.getAuthorizationUrl(ServiceEnvironment.SANDBOX));
		}

		resp.getWriter().println("</body></html>");
		
	}
	public static void processChainedPayRequest(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException  {
		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Simple Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {

			StringBuilder url = new StringBuilder();
			url.append(req.getRequestURL());
			String returnURL = url.toString() + "?return=1&action=pay&payKey=${payKey}";
			String cancelURL = url.toString() + "?action=pay&cancel=1";
			String ipnURL = url.toString() + "?action=ipn";
			
			ChainedPay chainedPay = new ChainedPay(Integer.parseInt(req.getParameter("numberOfSecondaryReceivers")));
			chainedPay.setCancelUrl(cancelURL);
			chainedPay.setReturnUrl(returnURL);
			chainedPay.setCredentialObj(credentialObj);
			chainedPay.setUserIp(req.getRemoteAddr());
			chainedPay.setApplicationName("Sample app on GAE");
			chainedPay.setCurrencyCode(CurrencyCodes.valueOf(req.getParameter("currencyCode")));
			chainedPay.setEnv(ServiceEnvironment.SANDBOX);
			chainedPay.setIpnURL(ipnURL);
			chainedPay.setLanguage("en_US");
			chainedPay.setMemo(req.getParameter("memo"));
			
			// set primary Receiver
			Receiver primaryReceiver = new Receiver();
			primaryReceiver.setAmount(Double.parseDouble(req.getParameter("primaryReceiverAmount")));
			primaryReceiver.setEmail(req.getParameter("primaryReceiverEmail"));
			primaryReceiver.setPaymentType(PaymentType.valueOf(req.getParameter("paymentType")));
			chainedPay.setPrimaryReceiver(primaryReceiver);
			
			// set the secondary receivers
			String[] receiverEmailItems = req.getParameterValues("receiveremail");
			String[] amountItems = req.getParameterValues("amount");
			for (int i = 0; i < receiverEmailItems.length; i++) {
				String recreceiverEmail = receiverEmailItems[i];
				if (recreceiverEmail != null
						&& recreceiverEmail.length() != 0) {
					Receiver rec1 = new Receiver();
					rec1.setAmount(Double.parseDouble(amountItems[i]));
					rec1.setEmail(receiverEmailItems[i]);
					rec1.setPaymentType(PaymentType.valueOf(req.getParameter("paymentType")));
					chainedPay.addToSecondaryReceivers(rec1);
				}
			}
			
			chainedPay.setSenderEmail(req.getParameter("email"));
			
			PayResponse payResponse = chainedPay.makeRequest();

			resp.getWriter().println("PaymentExecStatus:" + payResponse.getPaymentExecStatus().toString());

		} catch (IOException e) {
			resp.getWriter().println("Payment Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch(ReceiversCountMismatchException e){
			// missing receiver - log  error
			e.printStackTrace();
			resp.getWriter().println("Receiver count did not match - expected: " 
					+ e.getExpectedNumberOfReceivers() 
					+ " - actual:" + e.getActualNumberOfReceivers());			
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		} catch (PaymentExecException e) {

			resp.getWriter().println("Failed Payment Request w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());
		}catch (PaymentInCompleteException e){
			resp.getWriter().println("Incomplete Payment w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());			
		} catch (NumberFormatException e) {
			// invalid number passed
			e.printStackTrace();
			resp.getWriter().println("Invalid number of receivers sent");

		} catch (NotEnoughReceivers e) {
			// not enough receivers - min requirements for Parallel pay not met
			e.printStackTrace();
			resp.getWriter().println("Min number of receivers not met - Min Required:"
					+ e.getMinimumRequired() + " - actual set:" + e.getActualNumber());
		} catch (PaymentTypeNotAllowedException e) {
			// Personal and cashadvance not allowed for chained payments
			e.printStackTrace();
			resp.getWriter().println("PaymentType not allowed for chained pay:" + e.getPaymentType().toString());
		} catch (InvalidPrimaryReceiverAmountException e) {
			// In chained pay, primary receiver's amount shold be equal to or greater than the sum
			// of the amounts received by the secondary receivers
			e.printStackTrace();
			resp.getWriter().println("Amount received by Primary Receiver " + e.getPrimaryReceiverAmount()
					+ " has to be greater"
					+ " than sum of the amounts received by the secondary receivers (" + e.getSumOfSecondaryReceiversAmount()
					+ ")");

		} catch (AuthorizationRequiredException e) {
			// redirect the user to PayPal for Authorization
			resp.sendRedirect(e.getAuthorizationUrl(ServiceEnvironment.SANDBOX));
		}

		resp.getWriter().println("</body></html>");
		
	}
	
	public static void processImplicitChainedPayRequest(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException  {
		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Simple Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {

			StringBuilder url = new StringBuilder();
			url.append(req.getRequestURL());
			String ipnURL = url.toString() + "?action=ipn";
			
			ImplicitChainedPay implicitChainedPay = new ImplicitChainedPay(Integer.parseInt(req.getParameter("numberOfSecondaryReceivers")));
			implicitChainedPay.setCredentialObj(credentialObj);
			implicitChainedPay.setClientIp(req.getRemoteAddr());
			implicitChainedPay.setApplicationName("Sample app on GAE");
			implicitChainedPay.setCurrencyCode(CurrencyCodes.valueOf(req.getParameter("currencyCode")));
			implicitChainedPay.setEnv(ServiceEnvironment.SANDBOX);
			implicitChainedPay.setIpnURL(ipnURL);
			implicitChainedPay.setLanguage("en_US");
			implicitChainedPay.setMemo(req.getParameter("memo"));
			
			// set primary Receiver
			Receiver primaryReceiver = new Receiver();
			primaryReceiver.setAmount(Double.parseDouble(req.getParameter("primaryReceiverAmount")));
			primaryReceiver.setEmail(req.getParameter("primaryReceiverEmail"));
			primaryReceiver.setPaymentType(PaymentType.valueOf(req.getParameter("paymentType")));
			implicitChainedPay.setPrimaryReceiver(primaryReceiver);
			
			// set the secondary receivers
			String[] receiverEmailItems = req.getParameterValues("receiveremail");
			String[] amountItems = req.getParameterValues("amount");
			for (int i = 0; i < receiverEmailItems.length; i++) {
				String recreceiverEmail = receiverEmailItems[i];
				if (recreceiverEmail != null
						&& recreceiverEmail.length() != 0) {
					Receiver rec1 = new Receiver();
					rec1.setAmount(Double.parseDouble(amountItems[i]));
					rec1.setEmail(receiverEmailItems[i]);
					rec1.setPaymentType(PaymentType.valueOf(req.getParameter("paymentType")));
					implicitChainedPay.addToSecondaryReceivers(rec1);
				}
			}
						
			PayResponse payResponse = implicitChainedPay.makeRequest();

			resp.getWriter().println("PaymentExecStatus:" + payResponse.getPaymentExecStatus().toString());

		} catch (IOException e) {
			resp.getWriter().println("Payment Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch(ReceiversCountMismatchException e){
			// missing receiver - log  error
			e.printStackTrace();
			resp.getWriter().println("Receiver count did not match - expected: " 
					+ e.getExpectedNumberOfReceivers() 
					+ " - actual:" + e.getActualNumberOfReceivers());			
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		} catch (PaymentExecException e) {

			resp.getWriter().println("Failed Payment Request w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());
		}catch (PaymentInCompleteException e){
			resp.getWriter().println("Incomplete Payment w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());			
		} catch (NumberFormatException e) {
			// invalid number passed
			e.printStackTrace();
			resp.getWriter().println("Invalid number of receivers sent");

		} catch (NotEnoughReceivers e) {
			// not enough receivers - min requirements for Parallel pay not met
			e.printStackTrace();
			resp.getWriter().println("Min number of receivers not met - Min Required:"
					+ e.getMinimumRequired() + " - actual set:" + e.getActualNumber());
		} catch (PaymentTypeNotAllowedException e) {
			// Personal and cashadvance not allowed for chained payments
			e.printStackTrace();
			resp.getWriter().println("PaymentType not allowed for chained pay:" + e.getPaymentType().toString());
		} catch (InvalidPrimaryReceiverAmountException e) {
			// In chained pay, primary receiver's amount shold be equal to or greater than the sum
			// of the amounts received by the secondary receivers
			e.printStackTrace();
			resp.getWriter().println("Amount received by Primary Receiver " + e.getPrimaryReceiverAmount()
					+ " has to be greater"
					+ " than sum of the amounts received by the secondary receivers (" + e.getSumOfSecondaryReceiversAmount()
					+ ")");

		} 

		resp.getWriter().println("</body></html>");
		
	}
	
	public static void processCreateSimplePreapprovalRequest(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException {

		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Simple Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {

			StringBuilder url = new StringBuilder();
			url.append(req.getRequestURL());
			String returnURL = url.toString()
			+ "?return=1&action=preapproval&preapprovalKey=${preapprovalKey}";
			String cancelURL = url.toString() + "?action=preapproval&cancel=1";
			String ipnURL = url.toString() + "?action=ipn";
			CreateSimplePreapproval simplePreapproval = new CreateSimplePreapproval();
			
			simplePreapproval.setStartingDate(req.getParameter("startingDate"));
			simplePreapproval.setEndingDate(req.getParameter("endingDate"));
			simplePreapproval.setMaxTotalAmountOfAllPayments(
					Double.parseDouble(req.getParameter("maxTotalAmountOfAllPayments")));
			simplePreapproval.setMaxAmountPerPayment(Double.parseDouble(req.getParameter("maxAmountPerPayment")));
			simplePreapproval.setMaxNumberOfPayments(Integer.parseInt(req.getParameter("maxNumberOfPayments")));
			simplePreapproval.setCancelUrl(cancelURL);
			simplePreapproval.setReturnUrl(returnURL);
			simplePreapproval.setCredentialObj(credentialObj);
			simplePreapproval.setUserIp(req.getRemoteAddr());
			simplePreapproval.setApplicationName("Sample app on GAE");
			simplePreapproval.setCurrencyCode(CurrencyCodes.valueOf(req.getParameter("currencyCode")));
			simplePreapproval.setEnv(ServiceEnvironment.SANDBOX);
			simplePreapproval.setIpnURL(ipnURL);
			simplePreapproval.setLanguage("en_US");
			simplePreapproval.setMemo(req.getParameter("memo"));
			
			simplePreapproval.setSenderEmail(req.getParameter("email"));
			
			PreapprovalResponse preapprovalResponse = simplePreapproval.makeRequest();

			resp.getWriter().println("Preapproval Key:" + preapprovalResponse.getPreapprovalKey());

		} catch (IOException e) {
			resp.getWriter().println("Payment Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		} catch (PaymentExecException e) {

			resp.getWriter().println("Failed Payment Request w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());
		}catch (PaymentInCompleteException e){
			resp.getWriter().println("Incomplete Payment w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());			
		} catch (NumberFormatException e) {
			// invalid number passed
			e.printStackTrace();
			resp.getWriter().println("Invalid number of receivers sent");

		} catch (AuthorizationRequiredException e) {
			// redirect the user to PayPal for Authorization
			resp.sendRedirect(e.getAuthorizationUrl(ServiceEnvironment.SANDBOX));
		}

		resp.getWriter().println("</body></html>");
		
		

	}

	public static void processCreatePreapprovalForPeriodicPaymentsRequest(
			HttpServletRequest req, HttpServletResponse resp,
			APICredential credentialObj) throws IOException {

		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Simple Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {

			StringBuilder url = new StringBuilder();
			url.append(req.getRequestURL());
			String returnURL = url.toString()
			+ "?return=1&action=preapproval&preapprovalKey=${preapprovalKey}";
			String cancelURL = url.toString() + "?action=preapproval&cancel=1";
			String ipnURL = url.toString() + "?action=ipn";
			PaymentPeriodType pptype = PaymentPeriodType.valueOf(req.getParameter("paymentPeriod"));
			
			CreatePreapprovalForPeriodicPayments preapprovalForPeriodicPayments = new CreatePreapprovalForPeriodicPayments(pptype);
			
			preapprovalForPeriodicPayments.setStartingDate(req.getParameter("startingDate"));
			preapprovalForPeriodicPayments.setEndingDate(req.getParameter("endingDate"));
			preapprovalForPeriodicPayments.setMaxTotalAmountOfAllPayments(
					Double.parseDouble(req.getParameter("maxTotalAmountOfAllPayments")));
			preapprovalForPeriodicPayments.setMaxAmountPerPayment(Double.parseDouble(req.getParameter("maxAmountPerPayment")));
			preapprovalForPeriodicPayments.setMaxNumberOfPaymentsPerPeriod(Integer.parseInt(
					req.getParameter("maxNumberOfPaymentsPerPeriod")));
			String dateOfMonth = req.getParameter("dateOfMonth");
			if(dateOfMonth != null && dateOfMonth.length() > 0)
				preapprovalForPeriodicPayments.setDateOfMonth(Integer.parseInt(dateOfMonth));
			String dayOfWeek = req.getParameter("dayOfWeek");
			if( dayOfWeek != null && dayOfWeek.length() > 0) 
				preapprovalForPeriodicPayments.setDayOfWeek(DayOfWeek.valueOf(req.getParameter("dayOfWeek")));
			
			preapprovalForPeriodicPayments.setCancelUrl(cancelURL);
			preapprovalForPeriodicPayments.setReturnUrl(returnURL);
			preapprovalForPeriodicPayments.setCredentialObj(credentialObj);
			preapprovalForPeriodicPayments.setUserIp(req.getRemoteAddr());
			preapprovalForPeriodicPayments.setApplicationName("Sample app on GAE");
			preapprovalForPeriodicPayments.setCurrencyCode(CurrencyCodes.valueOf(req.getParameter("currencyCode")));
			preapprovalForPeriodicPayments.setEnv(ServiceEnvironment.SANDBOX);
			preapprovalForPeriodicPayments.setIpnURL(ipnURL);
			preapprovalForPeriodicPayments.setLanguage("en_US");
			preapprovalForPeriodicPayments.setMemo(req.getParameter("memo"));
			String pinType = req.getParameter("pinType");
			if(pinType != null && pinType.length() > 0){
				preapprovalForPeriodicPayments.setPinType(PinType.valueOf(pinType));
			}
			preapprovalForPeriodicPayments.setSenderEmail(req.getParameter("email"));
			
			PreapprovalResponse preapprovalResponse = preapprovalForPeriodicPayments.makeRequest();

			resp.getWriter().println("Preapproval Key:" + preapprovalResponse.getPreapprovalKey());

		} catch (IOException e) {
			resp.getWriter().println("Payment Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		} catch (PaymentExecException e) {

			resp.getWriter().println("Failed Payment Request w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());
		}catch (PaymentInCompleteException e){
			resp.getWriter().println("Incomplete Payment w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());			
		} catch (NumberFormatException e) {
			// invalid number passed
			e.printStackTrace();
			resp.getWriter().println("Invalid number of receivers sent");

		} catch (AuthorizationRequiredException e) {
			// redirect the user to PayPal for Authorization
			resp.sendRedirect(e.getAuthorizationUrl(ServiceEnvironment.SANDBOX));
		}

		resp.getWriter().println("</body></html>");
		
		
	}

	public static void processPreapprovedSimplePay(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException {
		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Simple Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {

			StringBuilder url = new StringBuilder();
			url.append(req.getRequestURL());
			String ipnURL = url.toString() + "?action=ipn";
			
			PreapprovedSimplePay preapprovalSimplePay = new PreapprovedSimplePay(req.getParameter("preapprovalKey"));
			preapprovalSimplePay.setCredentialObj(credentialObj);
			preapprovalSimplePay.setUserIp(req.getRemoteAddr());
			preapprovalSimplePay.setApplicationName("Sample app on GAE");
			preapprovalSimplePay.setCurrencyCode(CurrencyCodes.valueOf(req.getParameter("currencyCode")));
			preapprovalSimplePay.setEnv(ServiceEnvironment.SANDBOX);
			preapprovalSimplePay.setIpnURL(ipnURL);
			preapprovalSimplePay.setLanguage("en_US");
			preapprovalSimplePay.setMemo(req.getParameter("memo"));
			preapprovalSimplePay.setSenderEmail(req.getParameter("email"));
			Receiver receiver = new Receiver();
			receiver.setAmount(Double.parseDouble(req.getParameter("amount")));
			receiver.setEmail(req.getParameter("receiveremail"));
			receiver.setPaymentType(PaymentType.valueOf(req.getParameter("paymentType")));
			preapprovalSimplePay.setReceiver(receiver);
			
			PayResponse payResponse = preapprovalSimplePay.makeRequest();

			resp.getWriter().println("PaymentExecStatus:" + payResponse.getPaymentExecStatus().toString());

		} catch (IOException e) {
			resp.getWriter().println("Payment Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		} catch (PaymentExecException e) {

			resp.getWriter().println("Failed Payment Request w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());
		}catch (PaymentInCompleteException e){
			resp.getWriter().println("Incomplete Payment w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());						
		} catch (AuthorizationRequiredException e) {
			// redirect the user to PayPal for Authorization
			resp.sendRedirect(e.getAuthorizationUrl(ServiceEnvironment.SANDBOX));
		}

		resp.getWriter().println("</body></html>");
				
	}

	public static void processPreapprovedParallelPay(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException {
		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Simple Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {

			StringBuilder url = new StringBuilder();
			url.append(req.getRequestURL());
			String ipnURL = url.toString() + "?action=ipn";
			
			PreapprovedParallelPay preapprovedParallelPay = new PreapprovedParallelPay(
					req.getParameter("preapprovalKey"),Integer.parseInt(req.getParameter("numberOfReceivers")));
			
			preapprovedParallelPay.setCredentialObj(credentialObj);
			preapprovedParallelPay.setClientIp(req.getRemoteAddr());
			preapprovedParallelPay.setApplicationName("Sample app on GAE");
			preapprovedParallelPay.setCurrencyCode(CurrencyCodes.valueOf(req.getParameter("currencyCode")));
			preapprovedParallelPay.setEnv(ServiceEnvironment.SANDBOX);
			preapprovedParallelPay.setIpnURL(ipnURL);
			preapprovedParallelPay.setLanguage("en_US");
			preapprovedParallelPay.setMemo(req.getParameter("memo"));
			preapprovedParallelPay.setSenderEmail(req.getParameter("email"));

			
			// set the receivers
			String[] receiverEmailItems = req.getParameterValues("receiveremail");
			String[] amountItems = req.getParameterValues("amount");
			for (int i = 0; i < receiverEmailItems.length; i++) {
				String recreceiverEmail = receiverEmailItems[i];
				if (recreceiverEmail != null
						&& recreceiverEmail.length() != 0) {
					Receiver rec1 = new Receiver();
					rec1.setAmount(Double.parseDouble(amountItems[i]));
					rec1.setEmail(receiverEmailItems[i]);
					rec1.setPaymentType(PaymentType.valueOf(req.getParameter("paymentType")));
					preapprovedParallelPay.addToReceivers(rec1);
				}
			}
						
			PayResponse payResponse = preapprovedParallelPay.makeRequest();

			resp.getWriter().println("PaymentExecStatus:" + payResponse.getPaymentExecStatus().toString());

		} catch (IOException e) {
			resp.getWriter().println("Payment Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch(ReceiversCountMismatchException e){
			// missing receiver - log  error
			e.printStackTrace();
			resp.getWriter().println("Receiver count did not match - expected: " 
					+ e.getExpectedNumberOfReceivers() 
					+ " - actual:" + e.getActualNumberOfReceivers());			
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		} catch (PaymentExecException e) {

			resp.getWriter().println("Failed Payment Request w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());
		}catch (PaymentInCompleteException e){
			resp.getWriter().println("Incomplete Payment w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());	
		} catch (NumberFormatException e) {
			// invalid number passed
			e.printStackTrace();
			resp.getWriter().println("Invalid number of receivers sent");

		} catch (NotEnoughReceivers e) {
			// not enough receivers - min requirements for Parallel pay not met
			e.printStackTrace();
			resp.getWriter().println("Min number of receivers not met - Min Required:"
					+ e.getMinimumRequired() + " - actual set:" + e.getActualNumber());
		} catch (AuthorizationRequiredException e) {
			// redirect the user to PayPal for Authorization
			resp.sendRedirect(e.getAuthorizationUrl(ServiceEnvironment.SANDBOX));
		}

		resp.getWriter().println("</body></html>");		
	}

	public static void processPreapprovedChainedPay(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException {
		
		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Chained Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {

			StringBuilder url = new StringBuilder();
			url.append(req.getRequestURL());
			String ipnURL = url.toString() + "?action=ipn";
			
			PreapprovedChainedPay preapprovedChainedPay = new PreapprovedChainedPay(
					req.getParameter("preapprovalKey"), Integer.parseInt(req.getParameter("numberOfSecondaryReceivers")));
			preapprovedChainedPay.setCredentialObj(credentialObj);
			preapprovedChainedPay.setClientIp(req.getRemoteAddr());
			preapprovedChainedPay.setApplicationName("Sample app on GAE");
			preapprovedChainedPay.setCurrencyCode(CurrencyCodes.valueOf(req.getParameter("currencyCode")));
			preapprovedChainedPay.setEnv(ServiceEnvironment.SANDBOX);
			preapprovedChainedPay.setIpnURL(ipnURL);
			preapprovedChainedPay.setLanguage("en_US");
			preapprovedChainedPay.setMemo(req.getParameter("memo"));
			preapprovedChainedPay.setSenderEmail(req.getParameter("email"));
			
			// set primary Receiver
			Receiver primaryReceiver = new Receiver();
			primaryReceiver.setAmount(Double.parseDouble(req.getParameter("primaryReceiverAmount")));
			primaryReceiver.setEmail(req.getParameter("primaryReceiverEmail"));
			primaryReceiver.setPaymentType(PaymentType.valueOf(req.getParameter("paymentType")));
			preapprovedChainedPay.setPrimaryReceiver(primaryReceiver);
			
			// set the secondary receivers
			String[] receiverEmailItems = req.getParameterValues("receiveremail");
			String[] amountItems = req.getParameterValues("amount");
			for (int i = 0; i < receiverEmailItems.length; i++) {
				String recreceiverEmail = receiverEmailItems[i];
				if (recreceiverEmail != null
						&& recreceiverEmail.length() != 0) {
					Receiver rec1 = new Receiver();
					rec1.setAmount(Double.parseDouble(amountItems[i]));
					rec1.setEmail(receiverEmailItems[i]);
					rec1.setPaymentType(PaymentType.valueOf(req.getParameter("paymentType")));
					preapprovedChainedPay.addToSecondaryReceivers(rec1);
				}
			}
						
			PayResponse payResponse = preapprovedChainedPay.makeRequest();

			resp.getWriter().println("PaymentExecStatus:" + payResponse.getPaymentExecStatus().toString());

		} catch (IOException e) {
			resp.getWriter().println("Payment Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch(ReceiversCountMismatchException e){
			// missing receiver - log  error
			e.printStackTrace();
			resp.getWriter().println("Receiver count did not match - expected: " 
					+ e.getExpectedNumberOfReceivers() 
					+ " - actual:" + e.getActualNumberOfReceivers());			
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		} catch (PaymentExecException e) {

			resp.getWriter().println("Failed Payment Request w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());
		}catch (PaymentInCompleteException e){
			resp.getWriter().println("Incomplete Payment w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());			
		} catch (NumberFormatException e) {
			// invalid number passed
			e.printStackTrace();
			resp.getWriter().println("Invalid number of receivers sent");

		} catch (NotEnoughReceivers e) {
			// not enough receivers - min requirements for Parallel pay not met
			e.printStackTrace();
			resp.getWriter().println("Min number of receivers not met - Min Required:"
					+ e.getMinimumRequired() + " - actual set:" + e.getActualNumber());
		} catch (PaymentTypeNotAllowedException e) {
			// Personal and cashadvance not allowed for chained payments
			e.printStackTrace();
			resp.getWriter().println("PaymentType not allowed for chained pay:" + e.getPaymentType().toString());
		} catch (InvalidPrimaryReceiverAmountException e) {
			// In chained pay, primary receiver's amount shold be equal to or greater than the sum
			// of the amounts received by the secondary receivers
			e.printStackTrace();
			resp.getWriter().println("Amount received by Primary Receiver " + e.getPrimaryReceiverAmount()
					+ " has to be greater"
					+ " than sum of the amounts received by the secondary receivers (" + e.getSumOfSecondaryReceiversAmount()
					+ ")");

		} catch (AuthorizationRequiredException e) {
			// redirect the user to PayPal for Authorization
			resp.sendRedirect(e.getAuthorizationUrl(ServiceEnvironment.SANDBOX));
		}

		resp.getWriter().println("</body></html>");		
	}

	public static void processRefundCompletePayment(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj)  throws IOException {

		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Simple Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {
			
			RefundCompletePayment refundCompletePayment = new RefundCompletePayment();
			String payKey = req.getParameter("payKey");
			String trackingId = req.getParameter("trackingId");
			if(payKey != null && payKey.length() > 0)
				refundCompletePayment.setPayKey(payKey);
			else if(trackingId != null && trackingId.length() > 0)
				refundCompletePayment.setTrackingId(trackingId);
			
			refundCompletePayment.setCredentialObj(credentialObj);
			refundCompletePayment.setEnv(ServiceEnvironment.SANDBOX);
			refundCompletePayment.setLanguage("en_US");
			RefundResponse refundResponse = refundCompletePayment.makeRequest();
			
			for( RefundInfo refInfo: refundResponse.getRefundInfoList()) {
				resp.getWriter().println("Receiver:" + refInfo.getReceiver().getEmail() 
						+ "- Refund Status:" + refInfo.getRefundStatus()
						+ " - RefundTransactionStatus:" + refInfo.getRefundTransactionStatus());
			}
			
		} catch (IOException e) {
			resp.getWriter().println("Refund Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		}

		resp.getWriter().println("</body></html>");
		
	}
	public static void processRefundTransaction(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj)  throws IOException {

		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Simple Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {
			
			RefundTransaction refundTransaction = new RefundTransaction(req.getParameter("transactionId"));
			
			refundTransaction.setCredentialObj(credentialObj);
			refundTransaction.setEnv(ServiceEnvironment.SANDBOX);
			refundTransaction.setLanguage("en_US");
			RefundResponse refundResponse = refundTransaction.makeRequest();
			
			for( RefundInfo refInfo: refundResponse.getRefundInfoList()) {
				resp.getWriter().println("Receiver:" + refInfo.getReceiver().getEmail() 
						+ "- Refund Status:" + refInfo.getRefundStatus()
						+ " - RefundTransactionStatus:" + refInfo.getRefundTransactionStatus());
			}
			
		} catch (IOException e) {
			resp.getWriter().println("Refund Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		}

		resp.getWriter().println("</body></html>");
		
	}
	
	public static void processRefundPartialPayment(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj)  throws IOException {

		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Simple Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {
			
			RefundPartialPayment refundCompletePayment = new RefundPartialPayment();
			String payKey = req.getParameter("payKey");
			String trackingId = req.getParameter("trackingId");
			if(payKey != null && payKey.length() > 0)
				refundCompletePayment.setPayKey(payKey);
			else if(trackingId != null && trackingId.length() > 0)
				refundCompletePayment.setTrackingId(trackingId);
			
			refundCompletePayment.setCredentialObj(credentialObj);
			refundCompletePayment.setEnv(ServiceEnvironment.SANDBOX);
			refundCompletePayment.setLanguage("en_US");
			refundCompletePayment.setCurrencyCode(CurrencyCodes.valueOf(req.getParameter("currencyCode")));

			// set the receivers
			String[] receiverEmailItems = req.getParameterValues("receiveremail");
			String[] amountItems = req.getParameterValues("amount");
			for (int i = 0; i < receiverEmailItems.length; i++) {
				String recreceiverEmail = receiverEmailItems[i];
				if (recreceiverEmail != null
						&& recreceiverEmail.length() != 0) {
					Receiver rec1 = new Receiver();
					rec1.setAmount(Double.parseDouble(amountItems[i]));
					rec1.setEmail(receiverEmailItems[i]);
					refundCompletePayment.addToReceivers(rec1);
				}
			}
			
			RefundResponse refundResponse = refundCompletePayment.makeRequest();
			
			for( RefundInfo refInfo: refundResponse.getRefundInfoList()) {
				resp.getWriter().println("Receiver:" + refInfo.getReceiver().getEmail() 
						+ "- Refund Status:" + refInfo.getRefundStatus()
						+ " - RefundTransactionStatus:" + refInfo.getRefundTransactionStatus());
			}
			
		} catch (IOException e) {
			resp.getWriter().println("Refund Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		}

		resp.getWriter().println("</body></html>");
		
	}

	public static void processConvertCurrency(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException {
	
		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Simple Pay Response & Payment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
		
		try {
			ConvertCurrency convCurrency = new ConvertCurrency();
			
			String[] baseamountItems = req.getParameterValues("baseamount");
			String[] fromcodeItems = req.getParameterValues("fromcode");
			for (int i = 0; i < baseamountItems.length; i++) {
				String baseamount = baseamountItems[i];
				if (baseamount != null && baseamount.length() != 0) {
					CurrencyType currType = new CurrencyType(CurrencyCodes.valueOf(fromcodeItems[i]), 
							Double.parseDouble(baseamount));
					convCurrency.addToBaseAmountList(currType);
				}
			}
			String[] tocodeItems = req.getParameterValues("tocode");
			for (int i = 0; i < tocodeItems.length; i++) {
				String tocode = tocodeItems[i];
				if (tocode != null && tocode.length() != 0) {
					convCurrency.addToConvertToCurrencyList(CurrencyCodes.valueOf(tocode));
				}
			}
			ConvertCurrencyResponse currResp = convCurrency.makeRequest();
			
			resp.getWriter().println("Convert Currency ack:" + currResp.getResponseEnvelope().getAck().toString());
		} catch (IOException e) {
			resp.getWriter().println("Convert Currency Failed w/ IOException");
		} catch (MissingAPICredentialsException e) {
			// No API Credential Object provided - log error
			e.printStackTrace();
			resp.getWriter().println("No APICredential object provided");
		} catch (InvalidAPICredentialsException e) {
			// invalid API Credentials provided - application error - log error
			e.printStackTrace();
			resp.getWriter().println("Invalid API Credentials " + e.getMissingCredentials());
		} catch (MissingParameterException e) {
			// missing parameter - log  error
			e.printStackTrace();
			resp.getWriter().println("Missing Parameter error: " + e.getParameterName());
		} catch (RequestFailureException e) {
			// HTTP Error - some connection issues ?
			e.printStackTrace();
			resp.getWriter().println("Request HTTP Error: " + e.getHTTP_RESPONSE_CODE());
		} catch (InvalidResponseDataException e) {
			// PayPal service error 
			// log error
			e.printStackTrace();
			resp.getWriter().println("Invalid Response Data from PayPal: \"" + e.getResponseData() + "\"");
		} catch (PayPalErrorException e) {
			// Request failed due to a Service/Application error
			e.printStackTrace();
			if(e.getResponseEnvelope().getAck() == AckCode.Failure){
				// log the error
				resp.getWriter().println("Received Failure from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
				if(e.getPaymentExecStatus() != null){
					resp.getWriter().println("PaymentExecStatus: " + e.getPaymentExecStatus());
				}
			} else if(e.getResponseEnvelope().getAck() == AckCode.FailureWithWarning){
				// there is a warning - log it!
				resp.getWriter().println("Received Failure with Warning from PayPal (ack)");
				resp.getWriter().println("ErrorData provided:");
				resp.getWriter().println(e.getPayErrorList().toString());
			}
		} catch (RequestAlreadyMadeException e) {
			// shouldn't occur - log the error
			e.printStackTrace();
			resp.getWriter().println("Request to send a request that has already been sent!");
		}

		resp.getWriter().println("</body></html>");		
	}
	public static PaymentDetailsResponse processPaymentDetails(HttpServletResponse resp, 
			String payKey, String transactionId, String trackingId,
			APICredential credentialObj) throws IOException {

		try {


			PaymentDetailsRequest paymentDetailsRequest = new PaymentDetailsRequest("en_US",
					ServiceEnvironment.SANDBOX);

			if(payKey != null)
				paymentDetailsRequest.setPayKey(payKey);
			if(transactionId != null)
				paymentDetailsRequest.setTransactionId(transactionId);
			if(trackingId != null)
				paymentDetailsRequest.setTrackingId(trackingId);
			
			resp.getWriter().println( paymentDetailsRequest.toString());
			
			PaymentDetailsResponse response = paymentDetailsRequest.execute(credentialObj);
			
			return response;
			
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			return null;
		}

	}
	public static PreapprovalDetailsResponse processPreapprovalDetails(HttpServletResponse resp, 
			String preapprovalKey, boolean getBillingAddress,
			APICredential credentialObj) throws IOException {

		try {


			PreapprovalDetailsRequest preapprovalDetailsRequest = new PreapprovalDetailsRequest("en_US",
					ServiceEnvironment.SANDBOX);

			if(preapprovalKey != null)
				preapprovalDetailsRequest.setPreapprovalKey(preapprovalKey);
			
			preapprovalDetailsRequest.setGetBillingAddress(getBillingAddress);
			
			
			resp.getWriter().println( preapprovalDetailsRequest.toString());
			
			PreapprovalDetailsResponse response = preapprovalDetailsRequest.execute(credentialObj);
			
			return response;
			
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			return null;
		}

	}
}
