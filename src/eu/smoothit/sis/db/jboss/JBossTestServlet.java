/**
 * 
 */
package eu.smoothit.sis.db.jboss;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Basic Servlet Using XDoclet.
 * 
 * to run the test it is necessary to invoke to following url:
 * http://localhost:8080/sis/jbosstest
 * 
 * @author Christian Gross
 * @version 1.0
 * 
 * @web.servlet name = "JBossTest" display-name = "JBossTest"
 * @web.servlet-mapping url-pattern = "/jbosstest"
 */
public class JBossTestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8412282520994344477L;


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	/**
	 * this method invokes the JBossDBTest class and prints the result of the test.
	 * Note: JUnit must be present in order to run this test properly!
	 * @param req the request object - not used yet
	 * @param resp the response of request, containing the test results
	 * @throws IOException 
	 */
	protected void processRequest(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		System.out.println("Das hier ist ein JBossTest!!");
		try {
			JBossDBTest dbtest = new JBossDBTest();
			dbtest.runtest();
			resp.getWriter().write(dbtest.getLogMessages());
		} catch (NoClassDefFoundError e) {
			resp.getWriter().write(e.getMessage() + "\n");
			resp
					.getWriter()
					.write(
							"JUnit may not be present! Please make sure, that JUnit.jar is present in the server lib directory!");
		}

	}

}
