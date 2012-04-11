package com.adaptivesample;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.paypal.adaptive.api.requests.PayPalBaseRequest;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.ServiceEnvironment;

@SuppressWarnings("serial")
public class AdaptiveSampleServlet extends HttpServlet {
	private static final Logger log = Logger
			.getLogger(AdaptiveSampleServlet.class.getName());

	private static APICredential credentialObj;

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);

		// Get the value of APIUsername
		String APIUsername = getServletConfig().getInitParameter(
				"PPAPIUsername");
		String APIPassword = getServletConfig().getInitParameter(
				"PPAPIPassword");
		String APISignature = getServletConfig().getInitParameter(
				"PPAPISignature");
		String AppID = getServletConfig().getInitParameter("PPAppID");
		String AccountEmail = getServletConfig().getInitParameter(
				"PPAccountEmail");
		String PPEnvironment = getServletConfig().getInitParameter(
				"PPEnvironment");
		String HTTP_CONNECTION_TIMEOUT = getServletConfig().getInitParameter(
				"HTTP_CONNECTION_TIMEOUT");
		String HTTP_READ_TIMEOUT = getServletConfig().getInitParameter(
				"HTTP_READ_TIMEOUT");
		String DISABLE_SSL_CERT_CHECK = getServletConfig().getInitParameter(
				"DISABLE_SSL_CERT_CHECK");


		

		if (APIUsername == null || APIUsername.length() <= 0
				|| APIPassword == null || APIPassword.length() <= 0
				|| APISignature == null || APISignature.length() <= 0
				|| AppID == null || AppID.length() <= 0) {
			// requires API Credentials not set - throw exception
			throw new ServletException("APICredential(s) missing");
		}

		credentialObj = new APICredential();
		credentialObj.setAPIUsername(APIUsername);
		credentialObj.setAPIPassword(APIPassword);
		credentialObj.setSignature(APISignature);
		credentialObj.setAppId(AppID);
		credentialObj.setAccountEmail(AccountEmail);
		// set the HTTP connection configs
		PayPalBaseRequest.DISABLE_SSL_CERT_CHECK = Boolean.parseBoolean(DISABLE_SSL_CERT_CHECK);
		PayPalBaseRequest.HTTP_CONNECTION_TIMEOUT = Integer.parseInt(HTTP_CONNECTION_TIMEOUT);
		PayPalBaseRequest.HTTP_READ_TIMEOUT = Integer.parseInt(HTTP_READ_TIMEOUT);
		
		if (PPEnvironment != null) {
			if (PPEnvironment.equals("BETA_SANDBOX")) {
				AdaptiveRequests.PPEnv = ServiceEnvironment.BETA_SANDBOX;
			} else if (PPEnvironment.equals("PRODUCTION")) {
				AdaptiveRequests.PPEnv = ServiceEnvironment.PRODUCTION;
			} else if (PPEnvironment.equals("SANDBOX")) {
				AdaptiveRequests.PPEnv = ServiceEnvironment.SANDBOX;
			} else if (PPEnvironment.equals("STAGING")) {
				AdaptiveRequests.PPEnv = ServiceEnvironment.STAGING;
			} else {
				AdaptiveRequests.PPEnv = ServiceEnvironment.SANDBOX;
			}
		}
		log.info("Servlet initialized successfully");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String action = req.getParameter("action");

		try {

			log.info("Received Action: " + action);

			if (action != null && action.equals("pay")) {

				getServletConfig().getServletContext()
						.getRequestDispatcher("/pay.jsp").forward(req, resp);
			} else if (action != null && action.equals("paymentSuccess")) {

				getServletConfig().getServletContext()
						.getRequestDispatcher("/paymentsuccess.jsp")
						.forward(req, resp);
			} else if (action != null && action.equals("paymentCancel")) {

				getServletConfig().getServletContext()
						.getRequestDispatcher("/paymentcancel.jsp")
						.forward(req, resp);
			} else if (action != null && action.equals("executePayment")) {

				getServletConfig().getServletContext()
						.getRequestDispatcher("/executePayment.jsp")
						.forward(req, resp);
			} else if (action != null && action.equals("preapproval")) {

				getServletConfig().getServletContext()
						.getRequestDispatcher("/preapproval.jsp")
						.forward(req, resp);
			} else if (action != null && action.equals("refund")) {

				getServletConfig().getServletContext()
						.getRequestDispatcher("/refund.jsp").forward(req, resp);
			} else if (action != null && action.equals("payDetails")) {

				if (req.getParameter("payKey") != null) {
					doPost(req, resp);
				} else {
					getServletConfig().getServletContext()
							.getRequestDispatcher("/payDetails.jsp")
							.forward(req, resp);
				}
			} else if (action != null && action.equals("preapprovalDetails")) {

				if (req.getParameter("preapprovalKey") != null) {
					doPost(req, resp);
				} else {
					getServletConfig().getServletContext()
							.getRequestDispatcher("/preapprovalDetails.jsp")
							.forward(req, resp);
				}
			} else if (action != null && action.equals("cancelPreapproval")) {

				getServletConfig().getServletContext()
						.getRequestDispatcher("/cancelPreapproval.jsp")
						.forward(req, resp);
			} else if (action != null && action.equals("currencyConversion")) {

				getServletConfig().getServletContext()
						.getRequestDispatcher("/convertCurrency.jsp")
						.forward(req, resp);
			} else if (action != null && action.equals("setPaymentOptions")) {

				getServletConfig().getServletContext()
						.getRequestDispatcher("/setPaymentOptions.jsp")
						.forward(req, resp);
			} else if (action != null && action.equals("getPaymentOptions")) {

				getServletConfig().getServletContext()
						.getRequestDispatcher("/getPaymentOptions.jsp")
						.forward(req, resp);
			} else if (action != null && action.equals("getFundingPlans")) {

				getServletConfig().getServletContext()
						.getRequestDispatcher("/getFundingPlans.jsp")
						.forward(req, resp);
			} else if (action != null && action.equals("getShippingAddresses")) {

				getServletConfig().getServletContext()
						.getRequestDispatcher("/getShippingAddresses.jsp")
						.forward(req, resp);
			} else {
				getServletConfig().getServletContext()
						.getRequestDispatcher("/index.jsp").forward(req, resp);
			}

		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String action = req.getParameter("action");

		if (action != null && action.equals("pay")) {

			AdaptiveRequests.processPayRequest(getServletConfig()
					.getServletContext(), req, resp, credentialObj);

		} else if (action != null && action.equals("payDetails")) {

			String payKey = req.getParameter("payKey");
			if (payKey != null) {
				AdaptiveRequests.processPaymentDetails(resp, payKey, null,
						null, credentialObj);

			} else {
				resp.getWriter().println("Required field payKey not provided.");
			}

		} else if (action != null && action.equals("executePayment")) {

			String payKey = req.getParameter("payKey");
			if (payKey != null) {
				AdaptiveRequests.processExecutePaymentRequest(resp, payKey,
						null, null, credentialObj);

			} else {
				resp.getWriter().println("ExecurePayment Failed");
			}

		} else if (action != null && action.equals("preapproval")) {

			AdaptiveRequests
					.processPreapprovalRequest(req, resp, credentialObj);

		} else if (action != null && action.equals("setPaymentOptions")) {

			AdaptiveRequests.processSetPaymentOptionsRequest(req, resp,
					credentialObj);

		} else if (action != null && action.equals("getPaymentOptions")) {

			AdaptiveRequests.processGetPaymentOptionsRequest(req, resp,
					credentialObj);

		} else if (action != null && action.equals("getFundingPlans")) {

			AdaptiveRequests.processGetFundingPlansRequest(req, resp,
					credentialObj);

		} else if (action != null && action.equals("getShippingAddresses")) {
			AdaptiveRequests.processGetShippingAddressesRequest(req, resp,
					credentialObj);

		} else if (action != null && action.equals("preapprovalDetails")) {

			String preapprovalKey = req.getParameter("preapprovalKey");
			if (preapprovalKey != null) {
				AdaptiveRequests.processPreapprovalDetails(resp,
						preapprovalKey, true, credentialObj);

			} else {
				resp.getWriter().println(
						"PreapprovalDetails - no approvalKey provided");
			}

		} else if (action != null && action.equals("cancelPreapproval")) {

			String preapprovalKey = req.getParameter("preapprovalKey");
			if (preapprovalKey != null) {
				AdaptiveRequests.processCancelPreapproval(resp, preapprovalKey,
						credentialObj);

			} else {
				resp.getWriter().println("PreapprovalDetails Failed");
			}

		} else if (action != null && action.equals("refund")) {

			AdaptiveRequests.processRefund(req, resp, credentialObj);

		} else if (action != null && action.equals("currencyConversion")) {

			AdaptiveRequests
					.processCurrencyConversion(req, resp, credentialObj);

		} else {
			try {
				getServletConfig().getServletContext()
						.getRequestDispatcher("/index.jsp").forward(req, resp);

			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
