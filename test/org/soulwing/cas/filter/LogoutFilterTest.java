package org.soulwing.cas.filter;

import javax.servlet.ServletException;

import org.soulwing.servlet.MockFilterChain;
import org.soulwing.servlet.MockFilterConfig;
import org.soulwing.servlet.http.MockHttpServletRequest;
import org.soulwing.servlet.http.MockHttpServletResponse;

import junit.framework.TestCase;

public class LogoutFilterTest extends TestCase {

  private static final String LOGOUT_PATH = "/logout.action";
  private static final String LOGOUT_URL = "https://localhost" + LOGOUT_PATH;
  private static final String OTHER_URL = "https://localhost/some/other/path";
  
  private MockFilterConfig filterConfig;
  private MockFilterChain filterChain;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private LogoutFilter filter;
  
  protected void setUp() throws Exception {
    filterConfig = new MockFilterConfig();
    filterChain = new MockFilterChain();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    filter = new LogoutFilter();
  }

  private void setRequiredConfig() {
    filterConfig.setInitParameter(FilterConstants.LOGOUT_PATH, LOGOUT_PATH);
  }
  
  public void testInitNoLogoutPath() throws Exception {
    try {
      filter.init(filterConfig);
      fail("Expected ServletException");
    }
    catch (ServletException ex) {
      assertTrue(true);
    }
  }
  
  public void testRequestForLogoutPath() throws Exception {
    setRequiredConfig();
    filter.init(filterConfig);
    request.setRequestURL(LOGOUT_URL);
    request.getSession(true).setAttribute(
        FilterConstants.VALIDATION_ATTRIBUTE, new Object());
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNull(request.getSession().getAttribute(
        FilterConstants.VALIDATION_ATTRIBUTE));
  }

  public void testRequestForOtherPath() throws Exception {
    setRequiredConfig();
    filter.init(filterConfig);
    request.setRequestURL(OTHER_URL);
    request.getSession(true).setAttribute(
        FilterConstants.VALIDATION_ATTRIBUTE, new Object());
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNotNull(request.getSession().getAttribute(
        FilterConstants.VALIDATION_ATTRIBUTE));
  }

}
