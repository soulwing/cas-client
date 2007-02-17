/*
 * ValidationUtils.java
 *
 * Created on Feb 6, 2007 
 */
package org.soulwing.cas.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.soulwing.cas.client.ProxyValidationResponse;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.filter.FilterConstants;

/**
 * Utility methods for extracting the CAS validation artifacts from
 * an HttpServletRequest.
 *
 * @author Carl Harris
 */
public class ValidationUtils {

  /**
   * Gets the ServiceValidationResponse from an HttpServletRequest.
   * @param request HttpServletRequest from which to obtain the validation
   *    response
   * @return ServiceValidationResponse if it exists, else <code>null</code>
   */
  public static ServiceValidationResponse getServiceValidationResponse(
      HttpServletRequest request) {
    
    HttpSession session = request.getSession(false);
    if (session != null) {
      Object o = session.getAttribute(FilterConstants.VALIDATION_ATTRIBUTE);
      if (o instanceof ServiceValidationResponse) {
        return (ServiceValidationResponse) o;
      }
    }
    return null;
  }
  
  /**
   * Gets the ProxyValidationResponse from an HttpServletRequest.
   * @param request HttpServletRequest from which to obtain the validation
   *    response
   * @return ProxyValidationResponse if it exists, else <code>null</code>
   */
  public static ProxyValidationResponse getProxyValidationResponse(
      HttpServletRequest request) {
    return (ProxyValidationResponse) getServiceValidationResponse(request); 
  }
  
  /**
   * Gets the proxy granting ticket from an HttpServletRequest.  In order
   * to obtain a proxy granting ticket, the ProxyCallbackFilter must be
   * configured and placed in the filter chain <strong>after</strong> the
   * ServiceValidate or ProxyValidate filter.  Additionally, the validation
   * filter and ProxyCallbackFilter must be configured to use the same
   * value for proxyCallbackUrl.
   * 
   * @param request HttpServletRequest from which to obtain the proxy
   *    granting ticket
   * @return proxy granting ticket string from CAS.
   */
  public static String getProxyGrantingTicket(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      Object o = session.getAttribute(
          FilterConstants.PROXY_GRANTING_TICKET_ATTRIBUTE);
      if (o instanceof String) {
        return (String) o;
      }
    }
    return null;
  }
  
}
