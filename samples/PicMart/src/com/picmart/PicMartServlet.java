package com.picmart;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.PhotoEntry;
import com.paypal.adaptive.api.requests.fnapi.ParallelPay;
import com.paypal.adaptive.api.responses.PayResponse;
import com.paypal.adaptive.core.APICredential;
import com.paypal.adaptive.core.AckCode;
import com.paypal.adaptive.core.CurrencyCodes;
import com.paypal.adaptive.core.PaymentType;
import com.paypal.adaptive.core.Receiver;
import com.paypal.adaptive.core.ServiceEnvironment;
import com.paypal.adaptive.exceptions.AuthorizationRequiredException;
import com.paypal.adaptive.exceptions.InvalidAPICredentialsException;
import com.paypal.adaptive.exceptions.InvalidResponseDataException;
import com.paypal.adaptive.exceptions.MissingAPICredentialsException;
import com.paypal.adaptive.exceptions.MissingParameterException;
import com.paypal.adaptive.exceptions.NotEnoughReceivers;
import com.paypal.adaptive.exceptions.PayPalErrorException;
import com.paypal.adaptive.exceptions.PaymentExecException;
import com.paypal.adaptive.exceptions.PaymentInCompleteException;
import com.paypal.adaptive.exceptions.ReceiversCountMismatchException;
import com.paypal.adaptive.exceptions.RequestAlreadyMadeException;
import com.paypal.adaptive.exceptions.RequestFailureException;

@SuppressWarnings("serial")
public class PicMartServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(PicMartServlet.class.getName());


	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		try {
			String success = req.getParameter("return"); 
			String cancel = req.getParameter("cancel");

			if(cancel != null && cancel.equals("1")) {
				// user canceled the payment
				getServletConfig().getServletContext().getRequestDispatcher("/paymentcancel.jsp").forward(req, resp);

			} else if(success != null && success.equals("1")){
				getServletConfig().getServletContext().getRequestDispatcher("/paymentsuccess.jsp").forward(req, resp);
			} else {
				getServletConfig().getServletContext().getRequestDispatcher("/gallery.jsp").forward(req, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
