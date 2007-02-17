package org.soulwing.cas.filter;

import javax.servlet.ServletException;

import org.soulwing.servlet.MockFilterChain;
import org.soulwing.servlet.MockFilterConfig;
import org.soulwing.servlet.http.MockHttpServletRequest;
import org.soulwing.servlet.http.MockHttpServletResponse;

import junit.framework.TestCase;

public class SimpleBypassFilterTest extends TestCase {

  private static final String LOGOUT_PATH = "/logout.action";
  private static final String BYPASS_PATHS = "/other.action,/login.action";
  private static final String BYPASS_URL = "https://localhost/login.action";
  private static final String OTHER_URL = "https://localhost/some/other/path";
  
  private MockFilterConfig filterConfig;
  private MockFilterChain filterChain;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private SimpleBypassFilter filter;
  
  protected void setUp() throws Exception {
    filterConfig = new MockFilterConfig();
    filterChain = new MockFilterChain();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    filter = new SimpleBypassFilter();
  }

  private void setRequiredConfig() {
    filterConfig.setInitParameter("logoutPath", LOGOUT_PATH);
    filterConfig.setInitParameter("bypassPaths", BYPASS_PATHS);
  }
  
  public void testInitNoBypassPath() throws Exception {
    try {
      filter.init(filterConfig);
      fail("Expected ServletException");
    }
    catch (ServletException ex) {
      assertTrue(true);
    }
  }
  
  public void testRequestForBypassPath() throws Exception {
    setRequiredConfig();
    filter.init(filterConfig);
    request.setRequestURL(BYPASS_URL);
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNotNull(request.getSession().getAttribute(
        FilterConstants.BYPASS_ATTRIBUTE));
  }

  public void testRequestForOtherPath() throws Exception {
    setRequiredConfig();
    filter.init(filterConfig);
    request.setRequestURL(OTHER_URL);
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNull(request.getSession().getAttribute(
        FilterConstants.BYPASS_ATTRIBUTE));
  }

}
