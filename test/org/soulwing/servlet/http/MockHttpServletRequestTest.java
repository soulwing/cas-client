package org.soulwing.servlet.http;


import junit.framework.TestCase;


public class MockHttpServletRequestTest extends TestCase {

  private static final String SCHEME = "http";
  private static final String SERVER_NAME = "localhost";
  private static final int SERVER_PORT = 8080;
  private static final String SERVLET_PATH = "/testPath";
  private static final String QUERY_STRING = "param1=value1&param2=value2";
  
  public void testFullUrl() {
    final String url = SCHEME + "://" + SERVER_NAME + ":" + SERVER_PORT
        + SERVLET_PATH;
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURL(url + "?" + QUERY_STRING);
    assertEquals(url, request.getRequestURL().toString());
    assertEquals(SCHEME, request.getScheme());
    assertEquals(SERVER_NAME, request.getServerName());
    assertEquals("/", request.getContextPath());
    assertEquals(SERVLET_PATH, request.getRequestURI());
    assertEquals(SERVER_PORT, request.getServerPort());
    assertEquals(SERVLET_PATH, request.getServletPath());
    assertEquals(QUERY_STRING, request.getQueryString());
  }

  public void testUrlWithNoServerPort() {
    final String url = SCHEME + "://" + SERVER_NAME + SERVLET_PATH;
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURL(url + "?" + QUERY_STRING);
    assertEquals(url, request.getRequestURL().toString());
    assertEquals(SCHEME, request.getScheme());
    assertEquals(SERVER_NAME, request.getServerName());
    assertEquals(-1, request.getServerPort());
    assertEquals(SERVLET_PATH, request.getServletPath());
    assertEquals(QUERY_STRING, request.getQueryString());
  }

  public void testUrlWithNoServer() {
    final String url = SCHEME + ":" + SERVLET_PATH;
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURL(url + "?" + QUERY_STRING);
    assertEquals(url, request.getRequestURL().toString());
    assertEquals(SCHEME, request.getScheme());
    assertEquals(0, request.getServerName().length());
    assertEquals(-1, request.getServerPort());
    assertEquals(SERVLET_PATH, request.getServletPath());
    assertEquals(QUERY_STRING, request.getQueryString());
  }

  public void testUrlWithNoScheme() throws Exception {
    try {
      new MockHttpServletRequest(SERVLET_PATH + "?" + QUERY_STRING);
    }
    catch (IllegalArgumentException ex) {
      assertTrue(true);
    }
  }

  public void testSetParameter() {
    MockHttpServletRequest request = new MockHttpServletRequest(
        SCHEME + ":" + SERVLET_PATH);
    request.setParameter("param1", "value1");
    assertEquals("value1", request.getParameter("param1"));
  }

}
