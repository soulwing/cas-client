/*
 * ProxyTicketService.java
 *
 * Created on Feb 12, 2007
 */
package org.soulwing.cas.support;

import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.Proxy;
import org.soulwing.cas.client.ProxyFactory;
import org.soulwing.cas.client.ProxyResponse;
import org.soulwing.cas.client.SimpleProxyRequest;
import org.soulwing.cas.client.SimpleUrlGenerator;


/**
 * A service-layer bean that collaborates with ProxyGrantingTicketFilter 
 * (via ProxyGrantingTicketHolder) providing a simple means of obtaining
 * tickets to access CAS-protected services via proxy.
 *
 * @author Carl Harris
 */
public class ProxyTicketService {

  private ProtocolConfiguration configuration;
  private Proxy proxy;

  public ProtocolConfiguration getConfiguration() {
    return configuration;
  }
  
  public void setConfiguration(ProtocolConfiguration configuration) {
    this.configuration = configuration;
  }
  
  public void init() {
    if (configuration == null) {
      throw new IllegalStateException("Must provide configuration");
    }
    proxy = ProxyFactory.getProxy(new SimpleUrlGenerator(configuration));
  }
  
  public String getTicket(String targetService) {
    ProxyResponse response = proxy.proxy(
        new SimpleProxyRequest(getProxyGrantingTicket(), targetService));
    if (!response.isSuccessful()) {
      throw new ProxyTicketException(response);
    }
    return response.getProxyTicket();
  }
  
  private String getProxyGrantingTicket() {
    String proxyGrantingTicket = ProxyGrantingTicketHolder.getTicket();
    if (proxyGrantingTicket == null) {
      throw new IllegalStateException("Proxy granting ticket not available");
    }
    return proxyGrantingTicket;
  }
}
