/*
 * ValidatorFactory.java
 *
 * Created on Sep 8, 2006
 *
 * Copyright (C) 2006, 2007 Carl E Harris, Jr.
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
package org.soulwing.cas.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Default implementation of the Validator interface.
 * @author Carl Harris
 */
public class DefaultValidatorImpl implements Validator {

  private static final Log logger = 
      LogFactory.getLog(DefaultValidatorImpl.class);
  
  private ProtocolHandler protocolHandler;
  private ProtocolMappingStrategy serviceValidateMappingStrategy;
  private ProtocolMappingStrategy proxyValidateMappingStrategy;
  private UrlGenerator urlGenerator;
  private ProtocolSource protocolSource = new UrlProtocolSource();
  
  /**
   * @see Validator#serviceValidate(ValidationRequest)
   */
  public ServiceValidationResponse serviceValidate(ValidationRequest request) 
      throws NoTicketException {
    String ticket = request.getTicket();
    if (ticket == null) {
      throw new NoTicketException();
    }
    try {
      return (ServiceValidationResponse) 
          this.protocolHandler.processResult(
              getProtocolSource().getSource(
                  getUrlGenerator().getServiceValidateUrl(ticket)),
                  serviceValidateMappingStrategy);
    }
    catch (ServiceAccessException ex) {
      logger.error("error in accessing CAS service: ", ex);
      logger.error("assuming that ticket " + ticket + " is stale");
      throw new NoTicketException();
    }
  }

  /**
   * @see Validator#proxyValidate(ValidationRequest)
   */
  public ProxyValidationResponse proxyValidate(ValidationRequest request) 
      throws NoTicketException {
    String ticket = request.getTicket();
    if (ticket == null) {
      throw new NoTicketException();
    }
    try {
      return (ProxyValidationResponse) 
          this.protocolHandler.processResult(
              getProtocolSource().getSource(
                  getUrlGenerator().getProxyValidateUrl(ticket)),
                  proxyValidateMappingStrategy);
    }
    catch (ServiceAccessException ex) {
      logger.error("error in accessing CAS service: ", ex);
      logger.error("assuming that ticket " + ticket + " is stale");
      throw new NoTicketException();
    }
  }

  /**
   * Gets the <code>protocolHandler</code> property.
   * @return <code>ProtocolHandler}</code> property value
   */
  public ProtocolHandler getProtocolHandler() {
    return protocolHandler;
  }

  /**
   * Sets the <code>protocolHandler</code> property.
   * @param protocolHandler <code>ProtocolHandler</code> property value
   */
  public void setProtocolHandler(ProtocolHandler protocolHandler) {
    this.protocolHandler = protocolHandler;
  }

  /**
   * Gets the <code>protocolSource</code> property.
   * @return <code>ProtocolSource}</code> property value
   */
  public ProtocolSource getProtocolSource() {
    return protocolSource;
  }

  /**
   * Sets the <code>protocolSource</code> property.
   * @param protocolSource <code>ProtocolSource</code> property value
   */
  public void setProtocolSource(ProtocolSource protocolSource) {
    this.protocolSource = protocolSource;
  }

  /**
   * Gets the <code>proxyValidateMappingStrategy</code> property.
   * @return <code>ProtocolMappingStrategy}</code> property value
   */
  public ProtocolMappingStrategy getProxyValidateMappingStrategy() {
    return proxyValidateMappingStrategy;
  }

  /**
   * Sets the <code>proxyValidateMappingStrategy</code> property.
   * @param proxyValidateMappingStrategy <code>ProtocolMappingStrategy</code> property value
   */
  public void setProxyValidateMappingStrategy(
      ProtocolMappingStrategy proxyValidateMappingStrategy) {
    this.proxyValidateMappingStrategy = proxyValidateMappingStrategy;
  }

  /**
   * Gets the <code>serviceValidateMappingStrategy</code> property.
   * @return <code>ProtocolMappingStrategy}</code> property value
   */
  public ProtocolMappingStrategy getServiceValidateMappingStrategy() {
    return serviceValidateMappingStrategy;
  }

  /**
   * Sets the <code>serviceValidateMappingStrategy</code> property.
   * @param serviceValidateMappingStrategy <code>ProtocolMappingStrategy</code> property value
   */
  public void setServiceValidateMappingStrategy(
      ProtocolMappingStrategy serviceValidateMappingStrategy) {
    this.serviceValidateMappingStrategy = serviceValidateMappingStrategy;
  }

  /**
   * Gets the <code>urlGenerator</code> property.
   * @return <code>UrlGenerator}</code> property value
   */
  public UrlGenerator getUrlGenerator() {
    return urlGenerator;
  }

  /**
   * Sets the <code>urlGenerator</code> property.
   * @param urlGenerator <code>UrlGenerator</code> property value
   */
  public void setUrlGenerator(UrlGenerator urlGenerator) {
    this.urlGenerator = urlGenerator;
  }

}
