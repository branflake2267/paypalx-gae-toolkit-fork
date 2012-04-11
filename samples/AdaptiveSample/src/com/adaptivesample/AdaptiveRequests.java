/**
 * 
 */
package com.adaptivesample;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.paypal.adaptive.api.requests.CancelPreapprovalRequest;
import com.paypal.adaptive.api.requests.ConvertCurrencyRequest;
import com.paypal.adaptive.api.requests.ExecutePaymentRequest;
import com.paypal.adaptive.api.requests.GetFundingPlansRequest;
import com.paypal.adaptive.api.requests.GetPaymentOptionsRequest;
import com.paypal.adaptive.api.requests.GetShippingAddressesRequest;
import com.paypal.adaptive.api.requests.PayRequest;
import com.paypal.adaptive.api.requests.PaymentDetailsRequest;
import com.paypal.adaptive.api.requests.PreapprovalDetailsRequest;
import com.paypal.adaptive.api.requests.PreapprovalRequest;
import com.paypal.adaptive.api.requests.RefundRequest;
import com.paypal.adaptive.api.requests.SetPaymentOptionsRequest;
import com.paypal.adaptive.api.responses.CancelPreapprovalResponse;
import com.paypal.adaptive.api.responses.ConvertCurrencyResponse;
import com.paypal.adaptive.api.responses.ExecutePaymentResponse;
import com.paypal.adaptive.api.responses.GetFundingPlansResponse;
import com.paypal.adaptive.api.responses.GetPaymentOptionsResponse;
import com.paypal.adaptive.api.responses.GetShippingAddressesResponse;
import com.paypal.adaptive.api.responses.PayResponse;
import com.paypal.adaptive.api.responses.PaymentDetailsResponse;
import com.paypal.adaptive.api.responses.PreapprovalDetailsResponse;
import com.paypal.adaptive.api.responses.PreapprovalResponse;
import com.paypal.adaptive.api.responses.RefundResponse;
import com.paypal.adaptive.api.responses.SetPaymentOptionsResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.AckCode;
import com.paypal.adaptive.core.ActionType;
import com.paypal.adaptive.core.ClientDetails;
import com.paypal.adaptive.core.CurrencyCodes;
import com.paypal.adaptive.core.CurrencyType;
import com.paypal.adaptive.core.DisplayOptions;
import com.paypal.adaptive.core.EndPointUrl;
import com.paypal.adaptive.core.FeesPayerType;
import com.paypal.adaptive.core.InvoiceData;
import com.paypal.adaptive.core.InvoiceItem;
import com.paypal.adaptive.core.PaymentDetails;
import com.paypal.adaptive.core.PaymentExecStatus;
import com.paypal.adaptive.core.PaymentOptions;
import com.paypal.adaptive.core.PreapprovalDetails;
import com.paypal.adaptive.core.Receiver;
import com.paypal.adaptive.core.ReceiverIdentifier;
import com.paypal.adaptive.core.ReceiverOptions;
import com.paypal.adaptive.core.SenderOptions;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.InvalidAPICredentialsException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingAPICredentialsException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.PaymentExecException;
import com.paypal.adaptive.exceptions.RequestFailureException;

/**
 * @author palavilli
 * 
 */
public class AdaptiveRequests {

	public static ServiceEnvironment PPEnv = ServiceEnvironment.SANDBOX;
	
	public static void processPayRequest(ServletContext servletContext, HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException, ServletException {

		try {

			StringBuilder url = new StringBuilder();
			url.append(req.getRequestURL());
			
			String returnURL = url.toString() + "?action=paymentSuccess&payKey=${payKey}";
			String cancelURL = url.toString() + "?action=paymentCancel";
			
			String ipnURL = "http://pp-pymt-reconciler.appspot.com/paymentreconciler";
			String[] receiverEmailItems = req
			.getParameterValues("receiveremail");
			String[] amountItems = req.getParameterValues("amount");
			String[] primaryItems = req.getParameterValues("primary");
			String actionTypeStr = req.getParameter("actionType");
			ActionType actionType = ActionType.valueOf(actionTypeStr);
			
			PaymentDetails paymentDetails = new PaymentDetails(actionType);
			
			
			PayRequest payRequest = new PayRequest("en_US",
					PPEnv);
			
			for (int i = 0; i < receiverEmailItems.length; i++) {
				String recreceiverEmail = receiverEmailItems[i];
				if (recreceiverEmail != null
						&& recreceiverEmail.length() != 0) {
					Receiver rec1 = new Receiver();
					rec1.setAmount((new Double(amountItems[i])
					.doubleValue()));
					rec1.setEmail(receiverEmailItems[i]);
					rec1.setPrimary(Boolean.parseBoolean(primaryItems[i]));
					paymentDetails.addToReceiverList(rec1);
				}
			}

			ClientDetails cl = new ClientDetails();
			cl.setIpAddress(req.getRemoteAddr());
			cl.setApplicationId("Praveen's GAE Sample");
			paymentDetails.setCancelUrl(cancelURL);
			paymentDetails.setReturnUrl(returnURL);
			paymentDetails.setIpnNotificationUrl(ipnURL);
			if(req.getParameter("email") != null && req.getParameter("email").length() > 0)
				paymentDetails.setSenderEmail((String) req.getParameter("email"));
			paymentDetails.setCurrencyCode((String) req
					.getParameter("currencyCode"));
			// set feespayer
			String feesPayer = req.getParameter("feesPayer");
			if(feesPayer != null && feesPayer.length() > 0)
				paymentDetails.setFeesPayer(FeesPayerType.valueOf(feesPayer));
			payRequest.setClientDetails(cl);

			String pkey = req.getParameter("preapprovalKey");
			if (pkey != null && pkey.length() >= 20) {
				paymentDetails.setPreapprovalKey(pkey);
			}
			String memo = req.getParameter("memo");
			if(memo != null & memo.length() > 0) {
				paymentDetails.setMemo(memo);
			}
			
			payRequest.setPaymentDetails(paymentDetails);
			
			resp.getWriter().println( payRequest.toString());
			
			PayResponse payResp = payRequest.execute(credentialObj);
			
			resp.getWriter().println( payResp.toString());

			if(payResp != null) {
				// set the authorization url if it needs to be authorized
		    	if(payResp.getPaymentExecStatus()!= null 
		    			&& payResp.getPaymentExecStatus() == PaymentExecStatus.CREATED){
			    	req.setAttribute("payKey",payResp.getPayKey());
			    	req.setAttribute("stdUrl", EndPointUrl.getStdAuthorizationUrl(PPEnv, false));
			    	req.setAttribute("epUrl", EndPointUrl.getEmbeddedAuthorizationUrl(PPEnv, null));
			    	req.setAttribute("payRespStr", payResp.toString());
			    	req.setAttribute("payReqStr", payRequest.toString());
			    	
			    	// include the redirectHandler.jsp
			    	servletContext.getRequestDispatcher("/redirectHandler.jsp").forward(req, resp);
		    	}
			} 

			
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
		}
	}
	
	
	public static void processExecutePaymentRequest(HttpServletResponse resp, 
			String payKey, String transactionId, String trackingId,
			APICredential credentialObj) throws IOException {

		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>ExecutePayment Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesample\">Home</a> <br/>");

		try {


			ExecutePaymentRequest execPaymentRequest = new ExecutePaymentRequest("en_US",
					PPEnv);

			if(payKey != null)
				execPaymentRequest.setPayKey(payKey);
			
			resp.getWriter().println( execPaymentRequest.toString());
			
			ExecutePaymentResponse response = execPaymentRequest.execute(credentialObj);
			
			resp.getWriter().println( response.toString());

			resp.getWriter().println("</body></html>");
			
		} catch (IOException e) {
		} catch (Exception e) {
		}

	}
	
	public static void processPaymentDetails(HttpServletResponse resp, 
			String payKey, String transactionId, String trackingId,
			APICredential credentialObj) throws IOException {

		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Preapproval Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesample\">Home</a> <br/>");

		try {


			PaymentDetailsRequest paymentDetailsRequest = new PaymentDetailsRequest("en_US",
					PPEnv);

			if(payKey != null)
				paymentDetailsRequest.setPayKey(payKey);
			if(transactionId != null)
				paymentDetailsRequest.setTransactionId(transactionId);
			if(trackingId != null)
				paymentDetailsRequest.setTrackingId(trackingId);
			
			resp.getWriter().println( paymentDetailsRequest.toString());
			
			PaymentDetailsResponse response = paymentDetailsRequest.execute(credentialObj);
			
			resp.getWriter().println( response.toString());
			resp.getWriter().println("</body></html>");
			
		} catch (IOException e) {
		} catch (Exception e) {
		}

	}
	
	public static String generateAuthorizeUrl(String paykey, ServiceEnvironment env) 
	throws UnsupportedEncodingException{
		StringBuilder outStr = new StringBuilder();
		outStr.append(EndPointUrl.getAuthorizationUrl(env));
		outStr.append("?cmd=_ap-payment&paykey=");
		outStr.append(java.net.URLEncoder.encode(paykey, "UTF-8"));
		return outStr.toString();
	}
	
	public static void processPreapprovalRequest(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException {

		resp.setContentType("text/html");
		resp.getWriter().println("<html><head><title>Preapproval Response & Preapproval Details</title></head><body>");
		resp.getWriter().println("<a href=\"/adaptivesample\">Home</a> <br/>");

		try {

			StringBuilder url = new StringBuilder();
			url.append(req.getRequestURL());
			String returnURL = url.toString()
			+ "?action=preapprovalDetails&preapprovalKey=${preapprovalKey}";
			String cancelURL = url.toString() + "?action=preapproval&cancel=1";
			String ipnURL = "http://pp-pymt-reconciler.appspot.com/paymentreconciler";
			PreapprovalDetails preapprovalDetails = new PreapprovalDetails();
			
			PreapprovalRequest preapprovalRequest = new PreapprovalRequest("en_US",
					PPEnv);
			
			ClientDetails cl = new ClientDetails();
			cl.setIpAddress(req.getRemoteAddr());
			cl.setApplicationId("PraveenPreApprovalSample");
			preapprovalRequest.setClientDetails(cl);
			
			preapprovalDetails.setCancelUrl(cancelURL);
			preapprovalDetails.setReturnUrl(returnURL);
			preapprovalDetails.setIpnNotificationUrl(ipnURL);
			preapprovalDetails.setSenderEmail((String) req.getParameter("senderEmail"));
			preapprovalDetails.setCurrencyCode(CurrencyCodes.valueOf(req.getParameter("currencyCode")));	
			preapprovalDetails.setEndingDate((String)req.getParameter("endingDate"));
			
			if(req.getParameter("maxAmountPerPayment") != null && req.getParameter("maxAmountPerPayment").length() > 0)
				preapprovalDetails.setMaxAmountPerPayment(Double.parseDouble(req.getParameter("maxAmountPerPayment")));
			preapprovalDetails.setStartingDate((String)req.getParameter("startingDate"));
			if(req.getParameter("maxNumberOfPayments") != null && req.getParameter("maxNumberOfPayments").length() > 0)
				preapprovalDetails.setMaxNumberOfPayments(Integer.parseInt(req.getParameter("maxNumberOfPayments")));
			if(req.getParameter("maxTotalAmountOfAllPayments") != null && req.getParameter("maxTotalAmountOfAllPayments").length() > 0)
				preapprovalDetails.setMaxTotalAmountOfAllPayments(Double.parseDouble(req.getParameter("maxTotalAmountOfAllPayments")));	
			if(req.getParameter("maxNumberOfPaymentsPerPeriod") != null && req.getParameter("maxNumberOfPaymentsPerPeriod").length() > 0)
				preapprovalDetails.setMaxNumberOfPaymentsPerPeriod(Integer.parseInt(req.getParameter("maxNumberOfPaymentsPerPeriod")));
			
			// set preapproval details
			preapprovalRequest.setPreapprovalDetails(preapprovalDetails);
			
			resp.getWriter().println( preapprovalRequest.toString());
			
			PreapprovalResponse preapprovalResp = preapprovalRequest.execute(credentialObj);
			

			if(preapprovalResp != null) {
				resp.getWriter().println( preapprovalResp.toString());

				if(preapprovalResp.getErrorList() != null && preapprovalResp.getErrorList().size() > 0) {
					// error occured

				} else {
					// generate authurization url
					if (preapprovalResp.getResponseEnvelope().getAck() == AckCode.Success) {

						resp.getWriter().println("Preapproval Success.");
					
						resp.getWriter().println("<a href=\""
								+ AdaptiveRequests.generatePreApprovalAuthorizeUrl(preapprovalResp.getPreapprovalKey(), AdaptiveRequests.PPEnv)
								+ "\">Click here to authorize</a>");
					}


				}
			}
			resp.getWriter().println("</body></html>");
			
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
		
		} catch (PaymentExecException e) {

			resp.getWriter().println("Failed Payment Request w/ PaymentExecStatus: " + e.getPaymentExecStatus().toString());
			resp.getWriter().println("ErrorData provided:");

			resp.getWriter().println(e.getPayErrorList().toString());		
		} catch (NumberFormatException e) {
			// invalid number passed
			e.printStackTrace();
			resp.getWriter().println("Invalid number of receivers sent");

		}

	}
	
	public static void processPreapprovalDetails(HttpServletResponse resp, 
			String preapprovalKey, boolean getBillingAddress,
			APICredential credentialObj) throws IOException {

		try {
			resp.setContentType("text/html");
			resp.getWriter().println("<html><head><title>Preapproval Details</title></head><body>");
			resp.getWriter().println("<a href=\"/adaptivesample\">Home</a> <br/>");

			PreapprovalDetailsRequest preapprovalDetailsRequest = new PreapprovalDetailsRequest("en_US",
					PPEnv);

			if(preapprovalKey != null)
				preapprovalDetailsRequest.setPreapprovalKey(preapprovalKey);
			
			preapprovalDetailsRequest.setGetBillingAddress(getBillingAddress);
			
			
			resp.getWriter().println( preapprovalDetailsRequest.toString());
			
			PreapprovalDetailsResponse response = preapprovalDetailsRequest.execute(credentialObj);
			
			resp.getWriter().println( response.toString());
			resp.getWriter().println("</body></html>");			
		} catch (IOException e) {
		} catch (Exception e) {
		}

	}
	
	public static String generatePreApprovalAuthorizeUrl(String preapprovalKey, ServiceEnvironment env) 
	throws UnsupportedEncodingException{
		StringBuilder outStr = new StringBuilder();
		outStr.append(EndPointUrl.getStdAuthorizationUrl(env, true));
		outStr.append("&preapprovalkey=");
		outStr.append(java.net.URLEncoder.encode(preapprovalKey, "UTF-8"));
		return outStr.toString();
	}
	
	public static void processCancelPreapproval(HttpServletResponse resp, 
			String preapprovalKey, APICredential credentialObj) throws IOException {

		try {
			resp.setContentType("text/html");
			resp.getWriter().println("<html><head><title>Cancel Preapproval</title></head><body>");
			resp.getWriter().println("<a href=\"/adaptivesample\">Home</a> <br/>");


			CancelPreapprovalRequest cancelPreapprovalRequest = new CancelPreapprovalRequest("en_US",
					PPEnv);

			if(preapprovalKey != null)
				cancelPreapprovalRequest.setPreapprovalKey(preapprovalKey);
			
			
			
			resp.getWriter().println( cancelPreapprovalRequest.toString());
			
			CancelPreapprovalResponse response = cancelPreapprovalRequest.execute(credentialObj);
						
			resp.getWriter().println( response.toString());

			resp.getWriter().println("</body></html>");
			
		} catch (IOException e) {
		} catch (Exception e) {
		}
	}
	public static void processRefund(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException {

		try {
			resp.setContentType("text/html");
			resp.getWriter().println("<html><head><title>Refund</title></head><body>");
			resp.getWriter().println("<a href=\"/adaptivesample\">Home</a> <br/>");
			
			RefundRequest refundReq = new RefundRequest("en_US", PPEnv);

			ClientDetails cl = new ClientDetails();
			cl.setIpAddress(req.getRemoteAddr());
			cl.setApplicationId("Praveen's GAE Sample");
			refundReq.setPayKey(req.getParameter("payKey"));
			refundReq.setCurrencyCode(CurrencyCodes.valueOf(req.getParameter("currencyCode")));
			refundReq.setTrackingId(req.getParameter("trackingId"));
			refundReq.setTransactionId(req.getParameter("transactionId"));
			String[] receiverEmailItems = req
			.getParameterValues("receiveremail");
			String[] amountItems = req.getParameterValues("amount");
			for (int i = 0; i < receiverEmailItems.length; i++) {
				String recreceiverEmail = receiverEmailItems[i];
				if (recreceiverEmail != null
						&& recreceiverEmail.length() != 0) {
					Receiver rec1 = new Receiver();
					rec1.setAmount((new Double(amountItems[i])
					.doubleValue()));
					rec1.setEmail(receiverEmailItems[i]);
					refundReq.addToReceiverList(rec1);
				}
			}
			
			resp.getWriter().println( refundReq.toString());
			
			RefundResponse response = refundReq.execute(credentialObj);
			
			resp.getWriter().println( response.toString());

			resp.getWriter().println("</body></html>");

			
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
		} 
	}
	
	public static void processCurrencyConversion(HttpServletRequest req,
			HttpServletResponse resp, APICredential credentialObj) throws IOException {

		try {

			resp.setContentType("text/html");
			resp.getWriter().println("<html><head><title>Convert Currency</title></head><body>");
			resp.getWriter().println("<a href=\"/adaptivesample\">Home</a> <br/>");
			
			ConvertCurrencyRequest currReq = new ConvertCurrencyRequest("en_US", PPEnv);
			String[] baseamountItems = req.getParameterValues("baseamount");
			String[] fromcodeItems = req.getParameterValues("fromcode");
			for (int i = 0; i < baseamountItems.length; i++) {
				String baseamount = baseamountItems[i];
				if (baseamount != null && baseamount.length() != 0) {
					CurrencyType currType = new CurrencyType(CurrencyCodes.valueOf(fromcodeItems[i]), 
							Double.parseDouble(baseamount));
					currReq.addToBaseAmountList(currType);
				}
			}
			String[] tocodeItems = req.getParameterValues("tocode");
			for (int i = 0; i < tocodeItems.length; i++) {
				String tocode = tocodeItems[i];
				if (tocode != null && tocode.length() != 0) {
					currReq.addToConvertToCurrencyList(CurrencyCodes.valueOf(tocode));
				}
			}
			
			resp.getWriter().println( currReq.toString());
			
			ConvertCurrencyResponse response = currReq.execute(credentialObj);
			
			resp.getWriter().println( response.toString());
			resp.getWriter().println("</body></html>");

			
			
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
		}
		
	}


	public static void processSetPaymentOptionsRequest(
			HttpServletRequest req, HttpServletResponse resp,
			APICredential credentialObj) {
		
		
		try {
			resp.setContentType("text/html");
			resp.getWriter().println("<html><head><title>setPaymentOptions Response </title></head><body>");
			resp.getWriter().println("<a href=\"/adaptivesample\">Home</a> <br/>");


			SetPaymentOptionsRequest setPaymentOptionsReq = new SetPaymentOptionsRequest("en_US", PPEnv);
			String pkey = req.getParameter("payKey");
			if (pkey != null && pkey.length() > 0) {
				setPaymentOptionsReq.setPayKey(pkey);
			}
			
			PaymentOptions paymentOptions = new PaymentOptions();
			
			String headerImageUrl = req.getParameter("headerImageUrl");
			if (headerImageUrl != null && headerImageUrl.length() > 0) {
				DisplayOptions displayOptions = new DisplayOptions();
				displayOptions.setHeaderImageUrl(headerImageUrl);				
				paymentOptions.setDisplayOptions(displayOptions);
			}
			String requireShippingAddressSelection = req.getParameter("requireShippingAddressSelection");
			
			if (requireShippingAddressSelection != null && requireShippingAddressSelection.length() > 0) {
				SenderOptions senderOptions = new SenderOptions();
				senderOptions.setRequireShippingAddressSelection("true".equals(requireShippingAddressSelection));
				paymentOptions.setSenderOptions(senderOptions);
			}
			
			ReceiverOptions receiverOptions = new ReceiverOptions();
			if(req.getParameter("itemname") != null) {
				InvoiceItem invoiceItem1 = new InvoiceItem();
				invoiceItem1.setName(req.getParameter("itemname"));
				invoiceItem1.setPrice(req.getParameter("itemprice"));
				InvoiceData invoiceData = new InvoiceData();
				invoiceData.addToItem(invoiceItem1);
				receiverOptions.setInvoiceData(invoiceData);
			}
			if(req.getParameter("receiveremail") != null) {
				ReceiverIdentifier receiver = new ReceiverIdentifier();
				receiver.setEmail(req.getParameter("receiveremail"));
				receiverOptions.setReceiver(receiver);
			}
			
			paymentOptions.addToReceiverOptions(receiverOptions);
			
			setPaymentOptionsReq.setPaymentOptions(paymentOptions);
			
			resp.getWriter().println( setPaymentOptionsReq.toString());
			
			SetPaymentOptionsResponse response = setPaymentOptionsReq.execute(credentialObj);
			
			resp.getWriter().println( response.toString());
			resp.getWriter().println("</body></html>");

		} catch (IOException e) {
		} catch (Exception e) {
		}
	}
	
	public static void processGetPaymentOptionsRequest(
			HttpServletRequest req, HttpServletResponse resp,
			APICredential credentialObj) {
		
		
		try {
			resp.setContentType("text/html");
			resp.getWriter().println("<html><head><title>getPaymentOptions Response </title></head><body>");
			resp.getWriter().println("<a href=\"/adaptivesample\">Home</a> <br/>");

			GetPaymentOptionsRequest getPaymentOptionsReq = new GetPaymentOptionsRequest("en_US", PPEnv);
			String pkey = req.getParameter("payKey");
			if (pkey != null && pkey.length() > 0) {
				getPaymentOptionsReq.setPayKey(pkey);
			}
			
			resp.getWriter().println( getPaymentOptionsReq.toString());
			
			GetPaymentOptionsResponse response = getPaymentOptionsReq.execute(credentialObj);
			
			resp.getWriter().println( response.toString());

			resp.getWriter().println("</body></html>");
			
		} catch (IOException e) {
		} catch (Exception e) {
		}
	}


	public static void processGetFundingPlansRequest(
			HttpServletRequest req, HttpServletResponse resp,
			APICredential credentialObj) {
		try {
			resp.setContentType("text/html");
			resp.getWriter().println("<html><head><title>getFundingPlans Response </title></head><body>");
			resp.getWriter().println("<a href=\"/adaptivesample\">Home</a> <br/>");


			GetFundingPlansRequest request = new GetFundingPlansRequest("en_US", PPEnv);
			String pkey = req.getParameter("payKey");
			if (pkey != null && pkey.length() > 0) {
				request.setPayKey(pkey);
			}
			
			resp.getWriter().println( request.toString());
			
			GetFundingPlansResponse response = request.execute(credentialObj);
			
			resp.getWriter().println( response.toString());

			resp.getWriter().println("</body></html>");
			
		} catch (IOException e) {
		} catch (Exception e) {
		}
	}
	
	public static void processGetShippingAddressesRequest(
			HttpServletRequest req, HttpServletResponse resp,
			APICredential credentialObj) {
		try {
			resp.setContentType("text/html");
			resp.getWriter().println("<html><head><title>getShippingAddresses Response </title></head><body>");
			resp.getWriter().println("<a href=\"/adaptivesample\">Home</a> <br/>");

			


			GetShippingAddressesRequest request = new GetShippingAddressesRequest("en_US", PPEnv);
			String key = req.getParameter("key");
			if (key != null && key.length() > 0) {
				request.setKey(key);
			}
			
			resp.getWriter().println( request.toString());
			
			GetShippingAddressesResponse response = request.execute(credentialObj);
			
			resp.getWriter().println( response.toString());

			resp.getWriter().println("</body></html>");
			
		} catch (IOException e) {
		} catch (Exception e) {
		}
	}
	
}
