/*
 * ProtocolConfigurationFilter.java
 *
 * Created on Mar 1, 2007
 */
package org.soulwing.cas.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.soulwing.cas.client.ProtocolConfiguration;


/**
 * A filter that serves as a holder for a ProtocolConfiguration instance.
 * This filter can be used in applications that don't use a dependency
 * injection framework (e.g. Spring) to provide the CAS protocol configuration
 * to downstream filters.
 *
 * @author Carl Harris
 */
public class ProtocolConfigurationFilter implements Filter {

  private static final boolean GATEWAY_DEFAULT = false;
  private static final boolean RENEW_DEFAULT = false;
  private static ProtocolConfiguration config; 
  
  public void init(FilterConfig filterConfig) throws ServletException {
    FilterConfigurator fc = new FilterConfigurator(filterConfig);
    ProtocolConfiguration config = new ProtocolConfiguration();
    config.setServerUrl(
        fc.getRequiredParameter(FilterConstants.SERVER_URL));
    config.setServiceUrl(
        fc.getParameter(FilterConstants.SERVICE_URL));
    config.setGatewayFlag(
        Boolean.valueOf(fc.getParameter(FilterConstants.GATEWAY, 
            Boolean.toString(GATEWAY_DEFAULT))).booleanValue());
    config.setRenewFlag(
        Boolean.valueOf(fc.getParameter(FilterConstants.RENEW, 
            Boolean.toString(RENEW_DEFAULT))).booleanValue()); 
    config.setProxyCallbackUrl(
        fc.getParameter(FilterConstants.PROXY_CALLBACK_URL));
  }

  public void destroy() {
  }

  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    filterChain.doFilter(request, response);
  }

  public synchronized static ProtocolConfiguration getConfiguration() {
    return config;
  }

  public synchronized static void setConfiguration(ProtocolConfiguration config) {
    ProtocolConfigurationFilter.config = config;
  }
  
}
