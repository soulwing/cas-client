/*
 * CasAuthenticator.java
 *
 * Created on Sep 18, 2007 
 */
package org.soulwing.cas.apps.tomcat;

import java.io.IOException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Realm;
import org.apache.catalina.authenticator.AuthenticatorBase;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.deploy.LoginConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.NoTicketException;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ServiceValidationResponse;

/**
 * A Catalina Authenticator for CAS authentication.
 *
 * @author Carl Harris
 */
public class CasAuthenticator extends AuthenticatorBase {

  private static final Log log = LogFactory.getLog(CasAuthenticator.class);
  
  private AuthenticationStrategy authenticationStrategy;
  private ProtocolConfiguration protocolConfiguration;
  
  /* (non-Javadoc)
   * @see org.apache.catalina.authenticator.AuthenticatorBase#authenticate(org.apache.catalina.connector.Request, org.apache.catalina.connector.Response, org.apache.catalina.deploy.LoginConfig)
   */
  protected boolean authenticate(Request request, Response response, 
      LoginConfig loginConfig)
      throws IOException {
    
    if (false /* FIXME: isSessionAuthenticated(request) */) {
      return true;
    }
    else {
      try {
        ServiceValidationResponse validationResponse =
            authenticationStrategy.authenticate(request);
        return validationResponse.isSuccessful();
      }
      catch (NoTicketException ex) {
        response.sendRedirect(UrlGeneratorFactory.getUrlGenerator(request, 
            protocolConfiguration).getLoginUrl());
        return false;
      }
    }
  }

  public void start() throws LifecycleException {
    super.start();
    Realm realm = getContainer().getRealm();
    if (!(realm instanceof CasRealm)) {
      log.error("a CasRealm is required");
      throw new LifecycleException(this.getClass().getSimpleName()
          + " requires a CasRealm");
    }
    CasRealm casRealm = (CasRealm) realm;
    authenticationStrategy = casRealm.getAuthenticationStrategy();
    protocolConfiguration = casRealm.getProtocolConfiguration();
  }
  
}
