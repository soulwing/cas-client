/*
 * ProxyValidateMappingStrategy.java
 *
 * Created on Jul 7, 2007
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details. 
 */
package org.soulwing.cas.client.jdom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.soulwing.cas.client.AbstractResponse;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ProtocolMappingStrategy;
import org.soulwing.cas.client.ProtocolViolationException;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.client.ValidationResponse;

/**
 * A <code>ProtocolMappingStrategy</code> that maps to an instance
 * of <code>ProxyValidateResponse</code>.  The <code>/proxyValidate</code>
 * response generally contains, in addition to all of the
 * content for <code>/serviceResponse</code>, a element that contains a
 * collection of CAS proxy server identities.  Recognizing this similarity
 * in the response, this implementation delegates the mapping of the 
 * content that applies to both <code>/serviceValidate</code> and
 * <code>/proxyValidate</code> to an instance of 
 * <code>ProtocolMappingStrategy</code> configured via the 
 * <code>serviceValidateMappingStrategy</code> property.  
 * 
 * By default, the <code>serviceValidateMappingStrategy</code> is configured
 * with an instance of <code>ServiceValidateMappingStrategy</code>, but if 
 * the CAS server response contains custom content, this property will need to
 * be configured to the <code>ProtocolMappingStrategy</code> that handles the
 * custom content.
 *
 * @author Carl Harris
 */
public class ProxyValidateMappingStrategy 
    extends AbstractValidationResponseMappingStrategy {

  private ProtocolMappingStrategy serviceValidateMappingStrategy =
      new ServiceValidateMappingStrategy();
    
  /**
   * Gets the <code>serviceValidateMappingStrategy</code> property.
   * @return <code>ProtocolMappingStrategy</code> instance that
   *    will be used to map the portion of the <code>/proxyValidate</code>
   *    response that overlaps with the <code>/serviceValidate</code>
   *    response.
   */
  public ProtocolMappingStrategy getServiceValidateMappingStrategy() {
    return serviceValidateMappingStrategy;
  }

  /**
   * Sets the <code>serviceValidateMappingStrategy</code> property.
   * @param serviceValidateMappingStrategy <code>ProtocolMappingStrategy</code>
   *    instance that will be used to map the portion of the 
   *    <code>/proxyValidate</code> response that overlaps with the 
   *    <code>/serviceValidate</code> response.
   */
  public void setServiceValidateMappingStrategy(
      ProtocolMappingStrategy serviceValidateMappingStrategy) {
    this.serviceValidateMappingStrategy = serviceValidateMappingStrategy;
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.jdom.AbstractResponseMappingStrategy#mapSuccessResponse(org.jdom.Element)
   */
  public ValidationResponse mapSuccessResponse(Element element) {
    ProxyValidationSuccessResponse response = 
        new ProxyValidationSuccessResponse((ServiceValidationResponse) 
            serviceValidateMappingStrategy.mapResponse(
               element.getParentElement()));
    response.setProxies(getProxies(
        JdomUtil.getChild(element, ProtocolConstants.PROXIES)));
    return response;
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.jdom.AbstractResponseMappingStrategy#newFailureResponse()
   */
  protected AbstractResponse newFailureResponse() {
    return new ProxyValidationFailureResponse();
  };

  /**
   * Sets the list of proxies for a CAS response from the given
   * <code>element</code>
   * @param element JDOM element which is expected to contain the
   *    proxies element.
   * @throws ProtocolViolationException if <code>element</code> does
   *    not contain the proxies element.
   */
  @SuppressWarnings("unchecked")
  private List getProxies(Element element) {
    List proxies = new ArrayList();
    if (element != null) {
      List proxyElementList = JdomUtil.getChildren(element, 
          ProtocolConstants.PROXY);
      if (proxyElementList.size() == 0) {
        throw new ProtocolViolationException("Expected at least one proxy");
      }
      for (Iterator i = proxyElementList.iterator(); i.hasNext(); ) {
        Element proxy = (Element) i.next();
        proxies.add(proxy.getTextTrim());
      }
    }
    return proxies;
  }


}
