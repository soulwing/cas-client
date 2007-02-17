package org.soulwing.cas.filter;

import javax.servlet.ServletException;

import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.servlet.MockFilterChain;
import org.soulwing.servlet.MockFilterConfig;
import org.soulwing.servlet.http.MockHttpServletRequest;
import org.soulwing.servlet.http.MockHttpServletResponse;

import junit.framework.TestCase;

public class ProxyCallbackFilterTest extends TestCase {

  private static final String FILTER_PATH = "/proxyCallback.action";
  private static final String PROXY_CALLBACK_URL = "https:" + FILTER_PATH;
  private static final String OTHER_URL = "https:/other.action";
  private static final String TICKET = "ticket";
  private static final String TICKET_IOU = "ticket IOU";
  private static final String OTHER_TICKET_IOU = "other ticket IOU";
  
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockFilterChain filterChain;
  private MockFilterConfig filterConfig;
  private ProxyCallbackFilter filter;
  
  protected void setUp() throws Exception {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    filterChain = new MockFilterChain();
    filterConfig = new MockFilterConfig();
    filter = new ProxyCallbackFilter();
  }

  private void initFilter() throws Exception {
    filterConfig.setInitParameter(FilterConstants.FILTER_PATH, FILTER_PATH);
    filter.init(filterConfig);   
  }
  
  public void testInitNoCallbackUrl() throws Exception {
    try {
      filter.init(filterConfig);
      fail("Expected ServletException");
    }
    catch (ServletException ex) {
      assertTrue(true);
    }
  }

  public void testNormalCallbackRequest() throws Exception {
    initFilter();
    request.setRequestURL(PROXY_CALLBACK_URL);
    request.setParameter(ProtocolConstants.PROXY_TICKET_PARAM, TICKET);
    request.setParameter(ProtocolConstants.PROXY_TICKET_IOU_PARAM, TICKET_IOU);
    filter.doFilter(request, response, filterChain);
    assertTrue(!filterChain.isChainInvoked());
    assertTrue(filter.getTicketMap().containsKey(TICKET_IOU));
  }
  
  public void testCallbackRequestWithNoTicket() throws Exception {
    initFilter();
    request.setRequestURL(PROXY_CALLBACK_URL);
    request.setParameter(ProtocolConstants.PROXY_TICKET_IOU_PARAM, TICKET_IOU);
    filter.doFilter(request, response, filterChain);
    assertTrue(!filterChain.isChainInvoked());
    assertTrue(filter.getTicketMap().keySet().isEmpty());
  }

  public void testCallbackRequestWithNoTicketIou() throws Exception {
    initFilter();
    request.setRequestURL(PROXY_CALLBACK_URL);
    request.setParameter(ProtocolConstants.PROXY_TICKET_PARAM, TICKET);
    filter.doFilter(request, response, filterChain);
    assertTrue(!filterChain.isChainInvoked());
    assertTrue(filter.getTicketMap().keySet().isEmpty());
  }

  public void testValidatedRequestMatchingTicket() throws Exception {
    initFilter();
    ServiceValidationResponse validation = new ServiceValidationResponse();
    validation.setProxyGrantingTicketIou(TICKET_IOU);
    request.getSession().setAttribute(FilterConstants.VALIDATION_ATTRIBUTE, 
        validation);
    request.setRequestURL(OTHER_URL);
    filter.getTicketMap().put(TICKET_IOU, TICKET);
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNotNull(request.getSession().getAttribute(
        FilterConstants.PROXY_GRANTING_TICKET_ATTRIBUTE));
  }
  
  public void testValidatedRequestDifferentTicket() throws Exception {
    initFilter();
    ServiceValidationResponse validation = new ServiceValidationResponse();
    validation.setProxyGrantingTicketIou(OTHER_TICKET_IOU);
    request.getSession().setAttribute(FilterConstants.VALIDATION_ATTRIBUTE, 
        validation);
    request.setRequestURL(OTHER_URL);
    filter.getTicketMap().put(TICKET_IOU, TICKET);
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNull(request.getSession().getAttribute(
        FilterConstants.PROXY_GRANTING_TICKET_ATTRIBUTE));
  }

  public void testRequestWithSessionNoValidation() throws Exception {
    initFilter();
    request.getSession();  // create the session 
    request.setRequestURL(OTHER_URL);
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNull(request.getSession().getAttribute(
        FilterConstants.PROXY_GRANTING_TICKET_ATTRIBUTE));
  }

  public void testRequestWithoutSession() throws Exception {
    initFilter();
    request.setRequestURL(OTHER_URL);
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNull(request.getSession(false));
  }

}

