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
public class PhotoDataServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(PhotoDataServlet.class
			.getName());

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		doPost(req,resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("application/json; charset=utf-8");
		
		try {
			String userid = req.getParameter("userid");
			if(userid == null || userid.length() <=0)
				userid = "ppx.devnet@gmail.com";
			String callbackKey = req.getParameter("callback");
			PicasawebService service = new PicasawebService("Picasa test");
			PicasawebClient picasaClient = new PicasawebClient(service);
			List<AlbumEntry> albums = picasaClient
					.getAlbums(userid);
			if(callbackKey != null && callbackKey.length() > 0){
				resp.getWriter().println(callbackKey + "(");
			}
			resp.getWriter().println("{ \"gallery\":");
			resp.getWriter().println("{ \"photo\": [");
			boolean isFirst = true;
			for (AlbumEntry album : albums) {
				
				List<PhotoEntry> photos = picasaClient.getPhotos(album);
				for(int i = 0 ; i < photos.size(); i++){
					PhotoEntry photo = photos.get(i);
					String id = photo.getId();
					String thumbnail = photo.getMediaThumbnails().get(2).getUrl();
					String desc = photo.getTitle().getPlainText();
					if(!isFirst) resp.getWriter().println(", ");
					resp.getWriter().println("{ \"thumbnail\": \"" + thumbnail + "\", \"desc\": \"" 
							+ desc + "\", \"id\": \"" + id + "\" }");
					isFirst = false;
				}
			}
			resp.getWriter().println("] }}");
			if(callbackKey != null && callbackKey.length() > 0){
				resp.getWriter().println(");");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
