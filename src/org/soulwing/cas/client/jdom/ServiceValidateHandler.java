/*
 * ServiceValidateHandler.java
 *
 * Created on Sep 8, 2006
 */
package org.soulwing.cas.client.jdom;

import java.util.List;

import org.jdom.Element;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ProtocolViolationException;
import org.soulwing.cas.client.Response;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.client.ProtocolHandler;
import org.xml.sax.InputSource;


/**
 * A ProtocolHandler for the CAS <code>/serviceValidate</code> function.
 *
 * @author Carl Harris
 * 
 */
public class ServiceValidateHandler extends DefaultProtocolHandler {

  /**
   * @see ProtocolHandler#processResult(InputSource)
   */
  public Response processResult(InputSource result) {

    Element child = super.getResultElement(result);
    String elementName = child.getQualifiedName();
    if (elementName.equals(ProtocolConstants.AUTHENTICATION_SUCCESS)) {
      return configureSuccessResponse(child);
    }
    else if (elementName.equals(ProtocolConstants.AUTHENTICATION_FAILURE)) {
      return configureFailureResponse(child);
    }
    else {
      throw new ProtocolViolationException("Invalid validation result");
    }
  }

  /**
   * Configures the fields of a success response, using the given CAS
   * response <code>element</code>
   * @param element JDOM element containing an authenticationSuccess
   *    element.
   * @return a ServiceValidationResponse instance
   */
  private Response configureSuccessResponse(Element element) {
    ServiceValidationResponse response = new ServiceValidationResponse();
    response.setSuccessful(true);
    List children = element.getChildren();
    if (children == null || children.size() < 1 || children.size() > 2) {
      throw new ProtocolViolationException(
          "Invalid number of response elements");
    }
    response.setUserName(getUserName((Element) children.get(0)));
    if (children.size() == 2) {
      response.setProxyGrantingTicketIou(
          getProxyGrantingTicketIou((Element) children.get(1)));
    }
    return response;
  }

  /**
   * Configures the fields of failure response, using the given CAS
   * response <code>element</code>.
   * @param element JDOM element containing an authenticationFailure
   *    element.
   * @return a ServiceValidationResponse instance
   */
  private Response configureFailureResponse(Element element) {
    ServiceValidationResponse response = new ServiceValidationResponse();
    super.configureFailureResponse(response, element);
    return response;
  }
  
  /**
   * Gets the username for a CAS response from the given 
   * <code>element</code>.
   * @param element JDOM element which is expected to contain the
   *    username element.
   * @throws ProtocolViolationException if <code>element</code> is
   *    not the username element.
   */
  protected String getUserName(Element element) {
    if (!element.getQualifiedName().equals(ProtocolConstants.USER)) {
      throw new ProtocolViolationException("Expected username element");
    }
    return element.getTextTrim();
  }

  /**
   * Gets the proxy granting ticket IOU for a CAS response from the
   * given <code>element</code>.
   * @param element JDOM element which is expected to contain the 
   *    proxy granting ticket IOU.
   * @throws ProtocolViolationException if <code>element</code> does
   *    not contain the proxy granting ticket IOU.
   */
  protected String getProxyGrantingTicketIou(Element element) {
    if (!element.getQualifiedName()
        .equals(ProtocolConstants.PROXY_GRANTING_TICKET)) {
      throw new ProtocolViolationException(
          "Expected proxy granting ticket element");
    }
    return element.getTextTrim();
  }

}
