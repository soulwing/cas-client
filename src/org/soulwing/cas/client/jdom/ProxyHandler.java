/*
 * ProxyHandler.java
 *
 * Created on Sep 8, 2006
 */
package org.soulwing.cas.client.jdom;

import java.util.List;

import org.jdom.Element;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ProtocolViolationException;
import org.soulwing.cas.client.ProxyResponse;
import org.soulwing.cas.client.Response;
import org.soulwing.cas.client.ProtocolHandler;
import org.xml.sax.InputSource;


/**
 * A ProtocolHandler for the CAS <code>/proxy</code> function.
 *
 * @author Carl Harris
 * 
 */
public final class ProxyHandler extends DefaultProtocolHandler {

  /**
   * @see ProtocolHandler#processResult(InputSource)
   */
  public Response processResult(InputSource result) {

    Element child = super.getResultElement(result);
    String elementName = child.getQualifiedName();
    if (elementName.equals(ProtocolConstants.PROXY_SUCCESS)) {
      return configureSuccessResponse(child);
    }
    else if (elementName.equals(ProtocolConstants.PROXY_FAILURE)) {
      return configureFailureResponse(child);
    }
    else {
      throw new ProtocolViolationException("Invalid proxy result");
    }
  }

  /**
   * Configures the fields of a success response, using the given CAS
   * response <code>element</code>
   * @param element JDOM element containing an authenticationSuccess
   *    element.
   * @return a ProxyResponse instance
   */
  private Response configureSuccessResponse(Element element) {
    ProxyResponse response = new ProxyResponse();
    response.setSuccessful(true);
    List children = element.getChildren();
    if (children == null || children.size() != 1) {
      throw new ProtocolViolationException(
          "Invalid number of response elements");
    }
    
    response.setProxyTicket(getProxyTicket((Element) children.get(0)));
    return response;
  }

  /**
   * Configures the fields of failure response, using the given CAS
   * response <code>element</code>.
   * @param element JDOM element containing an authenticationFailure
   *    element.
   * @return a ProxyResponse instance
   */
  private Response configureFailureResponse(Element element) {
    ProxyResponse response = new ProxyResponse();
    super.configureFailureResponse(response, element);
    return response;
  }
  
  /**
   * Gets the proxy ticket for a CAS proxy response from the given 
   * <code>element</code>.
   * @param element JDOM element which is expected to contain the
   *    proxy ticket element.
   * @throws ProtocolViolationException if <code>element</code> is
   *    not the proxy ticket element.
   */
  private String getProxyTicket(Element element) {
    if (!element.getQualifiedName().equals(ProtocolConstants.PROXY_TICKET)) {
      throw new ProtocolViolationException("Expected proxy ticket");
    }
    return element.getTextTrim();
  }


}
