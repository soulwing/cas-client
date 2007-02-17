/*
 * DefaultProtocolHandler.java
 *
 * Created on Sep 14, 2006
 */
package org.soulwing.cas.client.jdom;

import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ProtocolViolationException;
import org.soulwing.cas.client.Response;
import org.soulwing.cas.client.ServiceAccessException;
import org.soulwing.cas.client.ProtocolHandler;
import org.xml.sax.InputSource;


/**
 * A base implementation of ProtocolHandler that uses JDOM.
 *
 * @author Carl Harris
 * 
 */
public abstract class DefaultProtocolHandler 
    implements ProtocolHandler {

  /** 
   * @see org.soulwing.cas.client.ProtocolHandler#processResult(
   *    org.xml.sax.InputSource)
   */
  public abstract Response processResult(InputSource result);

  /**
   * Gets the validation result element (child of the cas:serviceResponse
   * element) from <code>result</code>
   * @param result the response from the CAS server
   * @return the child of the cas:serviceResponse element.
   * @throws ServiceAccessException if an I/O error occurs while 
   *    accessing <code>result</code>
   * @throws ProtocolViolationException if the root element of 
   *    <code>result</code> is not a CAS service response or does not 
   *    contain exactly one child element, or if a JDOMException occurs.
   */
  protected Element getResultElement(InputSource result) { 
 
    SAXBuilder builder = new SAXBuilder();
//    builder.setFeature("http://xml.org/sax/features/namespaces", false);

    Document document = null;
    try {
      document = builder.build(result);
    }
    catch (JDOMException ex) {
      throw new ProtocolViolationException(ex);
    }
    catch (IOException ex) {
      throw new ServiceAccessException(ex);
    }
    
    Element rootElement = document.getRootElement();
    if (!rootElement.getQualifiedName().equals(ProtocolConstants.SERVICE_RESPONSE)) {
      throw new ProtocolViolationException("Invalid root element");
    }
    
    List children = rootElement.getChildren();
    if (children == null || children.size() != 1) {
      throw new ProtocolViolationException("Expected validation response");
    }
    
    return (Element) children.get(0);
  }

  /**
   * Configures a CAS failure response from the given <code>element</code>.
   * @param response the response to configure
   * @param element JDOM element that is assumed to contain a JDOM
   *    validation failure response.
   */
  protected void configureFailureResponse(Response response, 
      Element element) {
    response.setSuccessful(false);
    response.setResultCode(element.getAttributeValue(ProtocolConstants.CODE));
    response.setResultMessage(element.getTextTrim());
  }
  
}
