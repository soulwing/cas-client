/*
 * ProtocolConfigurationListener.java
 *
 * Created on Mar 2, 2007 
 */
package org.soulwing.cas.filter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ProtocolConfigurationHolder;

/**
 * A ServletContextListener that uses context init parameters to set the
 * properties on a ProtocolConfiguration bean and place it into the
 * ProtocolConfigurationHolder.  For applications that do not use a
 * dependency injection framework, this is the means of making the
 * ProtocolConfiguration bean available to all of the filters that need
 * it.
 *
 * @author Carl Harris
 */
public class ProtocolConfigurationListener implements ServletContextListener {

  private static final boolean GATEWAY_DEFAULT = false;
  private static final boolean RENEW_DEFAULT = false;

  public void contextInitialized(ServletContextEvent sce) {
    Configurator configurator = new Configurator(sce.getServletContext());
    ProtocolConfiguration config = new ProtocolConfiguration();
    try {
      config.setServerUrl(
          configurator.getRequiredParameter(FilterConstants.SERVER_URL));
      config.setServiceUrl(
          configurator.getParameter(FilterConstants.SERVICE_URL));
      config.setGatewayFlag(
          Boolean.valueOf(configurator.getParameter(FilterConstants.GATEWAY, 
              Boolean.toString(GATEWAY_DEFAULT))).booleanValue());
      config.setRenewFlag(
          Boolean.valueOf(configurator.getParameter(FilterConstants.RENEW, 
              Boolean.toString(RENEW_DEFAULT))).booleanValue()); 
      config.setProxyCallbackUrl(
          configurator.getParameter(FilterConstants.PROXY_CALLBACK_URL));
      ProtocolConfigurationHolder.setConfiguration(config);
    }
    catch (FilterParameterException ex) {
      throw new IllegalStateException(ex);
    }
  }

  public void contextDestroyed(ServletContextEvent sce) {
    // not needed
  }

}
