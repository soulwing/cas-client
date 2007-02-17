/*
 * ServiceValidateFilter.java
 *
 * Created on Sep 8, 2006
 */
package org.soulwing.cas.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.NoTicketException;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.client.ValidationRequest;
import org.soulwing.cas.client.ValidatorFactory;

/**
 * A ValidationFilter that uses CAS's <code>/serviceValidate</code>
 * function to perform the authentication.
 *
 * @author Carl Harris
 * 
 */
public class ServiceValidateFilter extends ValidationFilter {

  private FilterAuthenticator authenticator;
  
  protected FilterAuthenticator getAuthenticator() {
    return authenticator;
  }

  public void init(FilterConfig filterConfig) throws ServletException {
    super.init(filterConfig);
    authenticator = new ServiceValidateAuthenticator();
  }
  
  private class ServiceValidateAuthenticator implements FilterAuthenticator {
    
    public ServiceValidationResponse authenticate(
        final HttpServletRequest request) 
        throws NoTicketException {
      return ValidatorFactory.getValidator(
              UrlGeneratorFactory.getUrlGenerator(request))
          .serviceValidate(new ValidationRequest() {
              public String getTicket() {
                return request.getParameter(ProtocolConstants.TICKET_PARAM);
              }
            });
    }
    
  }

}
