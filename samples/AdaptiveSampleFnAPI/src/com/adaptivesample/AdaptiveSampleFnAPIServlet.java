package com.adaptivesample;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.paypal.adaptive.api.responses.PaymentDetailsResponse;
import com.paypal.adaptive.api.responses.PreapprovalDetailsResponse;
import com.paypal.adaptive.core.APICredential;


@SuppressWarnings("serial")
public class AdaptiveSampleFnAPIServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(AdaptiveSampleFnAPIServlet.class.getName());


	private static APICredential credentialObj;

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
		log.info("Servlet initialized successfully");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		String action = req.getParameter("action");
		String returnParam = req.getParameter("return"); 
		String cancel = req.getParameter("cancel");
		try {
			
			log.info("Received Action: " + action );

			if(cancel != null && cancel.equals("1")) {
				// user canceled the payment
			} 

			if(returnParam != null && returnParam.equals("1")){
				// user returned from PayPal AuthZ url
				resp.setContentType("text/html");
				
				if(action != null && action.equals("pay")){
					resp.getWriter().println("<html><head><title>Payment status</title></head><body>");
					resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
					String payKey = req.getParameter("payKey");
					PaymentDetailsResponse payDetailsResp = 
						AdaptiveRequests.processPaymentDetails(resp, payKey, null, null, credentialObj);
					resp.getWriter().println( payDetailsResp.getPaymentDetails().getStatus());
				} else if(action != null && action.equals("preapproval")){
					resp.getWriter().println("<html><head><title>Preapproval status</title></head><body>");
					resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");
					String preapprovalKey = req.getParameter("preapprovalKey");
					PreapprovalDetailsResponse preapprovalDetailsResp = 						 
						AdaptiveRequests.processPreapprovalDetails(resp, preapprovalKey, true, credentialObj);
					resp.getWriter().println( preapprovalDetailsResp.getPreapprovalDetails().getStatus());
				}
				resp.getWriter().println("</body></html>");
			} else { 
				if (action != null && action.equals("payDetails")) {

					getServletConfig().getServletContext().getRequestDispatcher(
					"/payDetails.jsp").forward(req, resp);
				} else if (action != null && action.equals("preapprovalDetails")) {

					getServletConfig().getServletContext().getRequestDispatcher(
					"/preapprovalDetails.jsp").forward(req, resp);	
				} else if (action != null && action.equals("simplePay")) {

					getServletConfig().getServletContext().getRequestDispatcher(
					"/SimplePay.jsp").forward(req, resp);
				} else if (action != null && action.equals("implicitSimplePay")) {

					getServletConfig().getServletContext().getRequestDispatcher(
					"/ImplicitSimplePay.jsp").forward(req, resp);
				} else if (action != null && action.equals("parallelPay")) {

					getServletConfig().getServletContext().getRequestDispatcher(
					"/ParallelPay.jsp").forward(req, resp);
				} else if (action != null && action.equals("implicitParallelPay")) {

					getServletConfig().getServletContext().getRequestDispatcher(
					"/ImplicitParallelPay.jsp").forward(req, resp);
				} else if (action != null && action.equals("chainedPay")) {

					getServletConfig().getServletContext().getRequestDispatcher(
					"/ChainedPay.jsp").forward(req, resp);
				} else if (action != null && action.equals("implicitChainedPay")) {

					getServletConfig().getServletContext().getRequestDispatcher(
					"/ImplicitChainedPay.jsp").forward(req, resp);
				} else if (action != null && action.equals("createSimplePreapproval")) {

					getServletConfig().getServletContext().getRequestDispatcher(
					"/CreateSimplePreapproval.jsp").forward(req, resp);	
				} else if (action != null && action.equals("createPreapprovalForPeriodicPayments")) {

					getServletConfig().getServletContext().getRequestDispatcher(
					"/CreatePreapprovalForPeriodicPayments.jsp").forward(req, resp);	
				} else if (action != null && action.equals("preapprovedSimplePay")) {

					getServletConfig().getServletContext().getRequestDispatcher(
					"/PreapprovedSimplePay.jsp").forward(req, resp);	
				} else if (action != null && action.equals("preapprovedParallelPay")) {
					getServletConfig().getServletContext().getRequestDispatcher(
					"/PreapprovedParallelPay.jsp").forward(req, resp);	
				} else if (action != null && action.equals("preapprovedChainedPay")) {
					getServletConfig().getServletContext().getRequestDispatcher(
					"/PreapprovedChainedPay.jsp").forward(req, resp);	
				} else if (action != null && action.equals("refundCompletePayment")) {
					getServletConfig().getServletContext().getRequestDispatcher(
					"/RefundCompletePayment.jsp").forward(req, resp);	
				} else if (action != null && action.equals("refundTransaction")) {
					getServletConfig().getServletContext().getRequestDispatcher(
					"/RefundTransaction.jsp").forward(req, resp);	
				} else if (action != null && action.equals("refundPartialPayment")) {
					getServletConfig().getServletContext().getRequestDispatcher(
					"/RefundPartialPayment.jsp").forward(req, resp);	
				} else if (action != null && action.equals("convertCurrency")) {
					getServletConfig().getServletContext().getRequestDispatcher(
					"/ConvertCurrency.jsp").forward(req, resp);	
				} else {
					getServletConfig().getServletContext().getRequestDispatcher(
					"/index.jsp").forward(req, resp);
				}
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
	throws IOException {


		String action = req.getParameter("action");

		if (action != null && action.equals("payDetails")) {
			resp.setContentType("text/html");
			resp.getWriter().println("<html><head><title>Preapproval Details</title></head><body>");
			resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");

			String payKey = req.getParameter("payKey");
			if(payKey != null){
				PaymentDetailsResponse payDetailsResp = 
					AdaptiveRequests.processPaymentDetails(resp, payKey, null, null, credentialObj);

				resp.getWriter().println( payDetailsResp.getPaymentDetails().getStatus());

			}  else {
				resp.getWriter().println("PayDetails Failed");
			}

			resp.getWriter().println("</body></html>");
		
		} else if (action != null && action.equals("preapprovalDetails")) {
			resp.setContentType("text/html");
			resp.getWriter().println("<html><head><title>Preapproval Details</title></head><body>");
			resp.getWriter().println("<a href=\"/adaptivesamplefnapi\">Home</a> <br/>");

			String preapprovalKey = req.getParameter("preapprovalKey");
			if(preapprovalKey != null){
					PreapprovalDetailsResponse payDetailsResp = 
						AdaptiveRequests.processPreapprovalDetails(resp, preapprovalKey, true, credentialObj);

					resp.getWriter().println( payDetailsResp.getPreapprovalDetails().getStatus());

			}  else {
				resp.getWriter().println("PreapprovalDetails Failed");
			}

			resp.getWriter().println("</body></html>");
		
		} else if (action != null && action.equals("simplePay")) {
			AdaptiveRequests.processSimplePayRequest(req, resp, credentialObj);
		} else if (action != null && action.equals("implicitSimplePay")) {
			AdaptiveRequests.processImplicitSimplePayRequest(req, resp, credentialObj);
		} else if (action != null && action.equals("parallelPay")) {
			AdaptiveRequests.processParallelPayRequest(req, resp, credentialObj);
		} else if (action != null && action.equals("implicitParallelPay")) {
			AdaptiveRequests.processImplicitParallelPayRequest(req, resp, credentialObj);
		} else if (action != null && action.equals("chainedPay")) {
			AdaptiveRequests.processChainedPayRequest(req, resp, credentialObj);
		} else if (action != null && action.equals("implicitChainedPay")) {
			AdaptiveRequests.processImplicitChainedPayRequest(req, resp, credentialObj);
		} else if (action != null && action.equals("createSimplePreapproval")) {
			AdaptiveRequests.processCreateSimplePreapprovalRequest(req, resp, credentialObj);
		} else if (action != null && action.equals("createPreapprovalForPeriodicPayments")) {
			AdaptiveRequests.processCreatePreapprovalForPeriodicPaymentsRequest(req, resp, credentialObj);
		} else if (action != null && action.equals("preapprovedSimplePay")) {
			AdaptiveRequests.processPreapprovedSimplePay(req, resp, credentialObj);
		} else if (action != null && action.equals("preapprovedParallelPay")) {
			AdaptiveRequests.processPreapprovedParallelPay(req, resp, credentialObj);
		} else if (action != null && action.equals("preapprovedChainedPay")) {
			AdaptiveRequests.processPreapprovedChainedPay(req, resp, credentialObj);
		} else if (action != null && action.equals("refundCompletePayment")) {
			AdaptiveRequests.processRefundCompletePayment(req, resp, credentialObj);
		} else if (action != null && action.equals("refundTransaction")) {
			AdaptiveRequests.processRefundTransaction(req, resp, credentialObj);
		} else if (action != null && action.equals("refundPartialPayment")) {
			AdaptiveRequests.processRefundPartialPayment(req, resp, credentialObj);
		} else if (action != null && action.equals("convertCurrency")) {
			AdaptiveRequests.processConvertCurrency(req, resp, credentialObj);
		} else {
			try {
				getServletConfig().getServletContext().getRequestDispatcher(
						"/index.jsp").forward(req, resp);

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
