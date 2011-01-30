package org.jobjects.mvc.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;
import org.mortbay.log.Log;

public class ControlerServletTest {
  private final static Logger logger = Logger.getLogger("org.jobjects.mvc.event.ControlerServletTest");

  private static ServletTester servletTester;
  private static String baseUrl;

  // private static ServletHolder servletHolder;

  /**
   * This kicks off an instance of the Jetty servlet container so that we can
   * hit it. We register an echo service that simply returns the parameters
   * passed to it.
   */
  @BeforeClass
  public static void initServletContainer() throws Exception {
    servletTester = new ServletTester();
    servletTester.setContextPath("/myapp");
    ServletHolder shProduction = servletTester.addServlet(ControlerServlet.class, "/maservletproduction");
    shProduction.setInitParameter("ACTION1", "org.jobjects.mvc.event.eventest.MonEvent1");
    shProduction.setInitParameter("ACTION2", "org.jobjects.mvc.event.eventest.MonEvent2");
    shProduction.setInitParameter("onWorkingMode", "false");

    ServletHolder shOnWorkingMode = servletTester.addServlet(ControlerServlet.class, "/maservletworking");
    shOnWorkingMode.setInitParameter("ACTION1", "org.jobjects.mvc.event.eventest.MonEvent1");
    shOnWorkingMode.setInitParameter("ACTION2", "org.jobjects.mvc.event.eventest.MonEvent2");
    shOnWorkingMode.setInitParameter("onWorkingMode", "true");

    ServletHolder shQuiPlante = servletTester.addServlet(ControlerServlet.class, "/maservletquiplante");
    shQuiPlante.setInitParameter("ACTION1", "org.jobjects.mvc.event.eventest.QuiNexistePas");
    shQuiPlante.setInitParameter("onWorkingMode", "false");

    FilterHolder filterHolder = servletTester.addFilter(LogHttlFilter.class, "/*", 1);
    filterHolder.setInitParameter("active", "true");

    baseUrl = servletTester.createSocketConnector(true);
    Log.debug("baseUrl=" + baseUrl);
    servletTester.start();
  }

  /**
   * Stops the Jetty container.
   */
  @AfterClass
  public static void cleanupServletContainer() throws Exception {
    servletTester.stop();
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
    post.addParameter(Constants.action.name(), actionName);
    client.executeMethod(post);

    result = post.getResponseBodyAsString();
    post.releaseConnection();
    return result;
  }

  @Test
  public void testDoGetHttpServletRequestHttpServletResponseProductionMode() {
    try {

      logger.log(Level.FINE, baseUrl + "/myapp/maservletproduction");

      String res = sendHttpPost(baseUrl + "/myapp/maservletproduction", "ACTION1");
      assertTrue("Test de la redirection", StringUtils.contains(res, "/myapp/index1.jsp"));

      HttpTester request = new HttpTester();
      HttpTester response = new HttpTester();
      request.setMethod("GET");
      request.setHeader("Host", "tester");
      request.setVersion("HTTP/1.0");
      request.setURI("/myapp/maservletproduction");
      response.parse(servletTester.getResponses(request.generate()));
      assertTrue("Il doit avoir 500 dans le message d'erreur.", StringUtils.contains(response.getContent(), "500"));
      assertEquals("Erreur sur la servlet car pas de paramétre", 500, response.getStatus());

    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erreur interne non prévue.", e);
      fail();
    }
  }

  @Test
  public void testDoGetHttpServletRequestHttpServletResponseWorkingMode() {
    try {

      logger.log(Level.FINE, baseUrl + "/myapp/maservletworking");

      String res = sendHttpPost(baseUrl + "/myapp/maservletworking", "ACTION1");
      assertTrue("Test de la redirection", StringUtils.contains(res, "/myapp/index1.jsp"));

      HttpTester request = new HttpTester();
      HttpTester response = new HttpTester();
      request.setMethod("GET");
      request.setHeader("Host", "tester");
      request.setVersion("HTTP/1.0");
      request.setURI("/myapp/maservletworking");
      response.parse(servletTester.getResponses(request.generate()));

      String responseContent = response.getContent();
      assertTrue("Il doit avoir Informations diverses dans le message d'erreur.",
          StringUtils.contains(responseContent, "Informations diverses"));
      assertTrue("Il doit avoir HEADERS dans le message d'erreur.", StringUtils.contains(responseContent, "HEADERS"));
      assertTrue("Il doit avoir ATTRIBUTES dans le message d'erreur.", StringUtils.contains(responseContent, "ATTRIBUTES"));
      assertTrue("Il doit avoir PARAMETERS dans le message d'erreur.", StringUtils.contains(responseContent, "PARAMETERS"));
      assertEquals("Il n'y aura pas d'erreur sur la servlet car le canal est redirigé sur un page d'erreur spécifique.", 200,
          response.getStatus());

    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erreur interne non prévue.", e);
      fail();
    }
  }

  @Test
  public void testDoGetHttpServletRequestHttpServletResponseQuiPlante() {
    try {

      logger.log(Level.FINE, baseUrl + "/myapp/maservletquiplante");

      try {
        sendHttpPost(baseUrl + "/myapp/maservletquiplante", "ACTION1");
      } catch (ClassNotFoundException e) {
        assertTrue("Remonte le nom de la classe.",
            StringUtils.contains(ExceptionUtils.getStackTrace(e), "org.jobjects.mvc.event.eventest.QuiNexistePas"));
      }

    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erreur interne non prévue.", e);
      fail();
    }
  }

  @Test
  public void testDoPostHttpServletRequestHttpServletResponse() {
    // assertNotNull(new String());
    testDoGetHttpServletRequestHttpServletResponseProductionMode();
  }

}
