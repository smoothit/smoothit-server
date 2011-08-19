package eu.smoothit.sis.monitor;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Basic Servlet for Monitor Using XDoclet.
 * 
 * @author Michael Makidis
 * @version 1.0
 * 
 * @web.servlet 
 * 		name = "MonitorServlet" 
 * 		display-name = "MonitorServlet"
 * @web.servlet-mapping 
 * 		url-pattern = "/monitor"                    
 */
public class MonitorServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3697222565340259334L;
	
	/**
	 * The handler that does the actual work
	 */
	@EJB
	MonitorRequestHandler handler;

	/**
	 * Handles the GET HTTP method.
	 * 
	 * @param req The client's HTTP request
	 * @param resp The server's HTTP response
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	/**
	 * Handles the POST HTTP method.
	 * 
	 * @param req The client's HTTP request
	 * @param resp The server's HTTP response
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}
	
	/**
	 * Handles the client's request and returns "OK" via HTTP if there
	 * are no errors.
	 * 
	 * @param req The client's HTTP request
	 * @param resp The server's HTTP response
	 */
	protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
	{		
		try {
			handler.handleRequest(req.getInputStream(), req.getRemoteAddr());
			
			/*InputStream is = new Base64.InputStream(req.getInputStream());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			int ch = 0;
			while ((ch = is.read()) >= 0)
			{
				baos.write(ch);
			}
			
			System.out.println(baos.toString());*/
			resp.getWriter().println("OK");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
