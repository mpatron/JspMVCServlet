package org.jobjects.mvc.event;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;
import org.mortbay.log.Log;

public class ControlerServletTest {
  private final static Logger logger = Logger
      .getLogger("org.jobjects.mvc.event.ControlerServletTest");

  private static ServletTester tester;
  private static String baseUrl;

  /**
   * This kicks off an instance of the Jetty servlet container so that we can
   * hit it. We register an echo service that simply returns the parameters
   * passed to it.
   */
  @BeforeClass
  public static void initServletContainer() throws Exception {
    tester = new ServletTester();
    tester.setContextPath("/myapp");
    ServletHolder sh = tester.addServlet(ControlerServlet.class, "/echo");
    sh.setInitParameter("ACTION1", "org.jobjects.mvc.event.eventest.MonEvent1");
    sh.setInitParameter("ACTION2", "org.jobjects.mvc.event.eventest.MonEvent2");
    baseUrl = tester.createSocketConnector(true);
    Log.debug("baseUrl=" + baseUrl);
    tester.start();
  }

  /**
   * Stops the Jetty container.
   */
  @AfterClass
  public static void cleanupServletContainer() throws Exception {
    tester.stop();
  }

  @Test
  public void testControlerServlet() {
    try {
      ControlerServlet cs = new ControlerServlet();
      assertNotNull(cs);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erreur interne non prévue.", e);
      fail();
    }

  }

  // public String sendHttpPost(String url, String queryString) throws Exception
  // {
  // String result = null;
  // try {
  // HttpClient client = new HttpClient();
  // PostMethod post = new PostMethod(url);
  // post.setQueryString(queryString);
  // client.executeMethod(post);
  //
  // result = post.getResponseBodyAsString();
  // post.releaseConnection();
  // } catch (Exception e) {
  // throw new Exception("post failed", e);
  // }
  // return result;
  // }

  public String sendHttpPost(final String url, final String actionName) throws Exception {
    String result = null;
    final HttpClient client = new HttpClient();
    final PostMethod post = new PostMethod(url);
    post.addParameter(Constants.ACTION.name(), actionName);
    client.executeMethod(post);

    result = post.getResponseBodyAsString();
    post.releaseConnection();
    return result;
  }

  @Test
  public void testDoGetHttpServletRequestHttpServletResponse() {
    try {
      logger.log(Level.FINE, baseUrl + "/myapp/echo");

      String res = sendHttpPost(baseUrl + "/myapp/echo", "ACTION1");
      assertTrue("Test de la redirection", StringUtils.contains(res,
          "/myapp/index1.jsp"));

      HttpTester request = new HttpTester();
      HttpTester response = new HttpTester();
      request.setMethod("GET");
      request.setHeader("Host", "tester");
      request.setVersion("HTTP/1.0");
      request.setURI("/myapp/echo");
      response.parse(tester.getResponses(request.generate()));

      assertTrue("Erreur sur la servlet car pas de paramétre", response
          .getStatus() == 500);

    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erreur interne non prévue.", e);
      fail();
    }
  }

  @Test
  public void testDoPostHttpServletRequestHttpServletResponse() {
    // assertNotNull(new String());
    testDoGetHttpServletRequestHttpServletResponse();
  }

}
