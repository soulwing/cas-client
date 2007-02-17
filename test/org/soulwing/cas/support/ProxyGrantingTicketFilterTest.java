package org.soulwing.cas.support;

import org.soulwing.cas.filter.FilterConstants;
import org.soulwing.cas.support.ProxyGrantingTicketFilter;
import org.soulwing.cas.support.ProxyGrantingTicketHolder;
import org.soulwing.servlet.MockFilterChain;
import org.soulwing.servlet.MockFilterConfig;
import org.soulwing.servlet.http.MockHttpServletRequest;
import org.soulwing.servlet.http.MockHttpServletResponse;

import junit.framework.TestCase;


public class ProxyGrantingTicketFilterTest extends TestCase {

  private static final String PROXY_GRANTING_TICKET = "proxyGrantingTicket";
  private MockFilterConfig config;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockFilterChain filterChain;
  private ProxyGrantingTicketFilter filter;
  
  protected void setUp() throws Exception {
    config = new MockFilterConfig();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    filterChain = new MockFilterChain();
    filter = new ProxyGrantingTicketFilter();
    filter.init(config);
    ProxyGrantingTicketHolder.clearTicket();
  }

  public void testSessionHasProxyGrantingTicket() throws Exception {
    request.getSession(true).setAttribute(
        FilterConstants.PROXY_GRANTING_TICKET_ATTRIBUTE,
        PROXY_GRANTING_TICKET);
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertEquals(PROXY_GRANTING_TICKET, ProxyGrantingTicketHolder.getTicket());
  }

  public void testSessionHasNoProxyGrantingTicket() throws Exception {
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNull(ProxyGrantingTicketHolder.getTicket());
  }

}
