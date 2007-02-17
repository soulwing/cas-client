/*
 * ProxyTicketException.java
 *
 * Created on Feb 12, 2007
 */
package org.soulwing.cas.support;

import org.soulwing.cas.client.ProxyResponse;


/**
 * An exception thrown when a request for a CAS proxy ticket fails.
 *
 * @author Carl Harris
 */
public class ProxyTicketException extends RuntimeException {

  private static final long serialVersionUID = 6323178515814921510L;

  public ProxyTicketException(ProxyResponse response) {
    super(response.getResultMessage() + "(" + response.getResultCode() + ")");
  }

}
