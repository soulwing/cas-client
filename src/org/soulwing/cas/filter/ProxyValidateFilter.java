/*
 * ProxyValidateFilter.java
 *
 * Created on Sep 8, 2006
 */
package org.soulwing.cas.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * A ValidationFilter that uses CAS's <code>/proxyValidate</code>
 * function to perform the authentication.
 * 
 * @author Carl Harris
 */
public class ProxyValidateFilter extends ValidationFilter { 
  
  private ProxyValidateAuthenticator authenticator;
  
  protected FilterAuthenticator getAuthenticator() {
    return authenticator;
  }
  
  public void init(FilterConfig config) throws ServletException {
    super.init(config);
    authenticator = new ProxyValidateAuthenticator( 
        getConfiguration().getTrustedProxies());
  }
  
}
