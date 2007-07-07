/*
 * ValidatorFactory.java
 *
 * Created on Sep 14, 2006
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

import org.soulwing.cas.client.jdom.JdomProtocolHandlerImpl;
import org.soulwing.cas.client.jdom.ProxyValidateMappingStrategy;
import org.soulwing.cas.client.jdom.ServiceValidateMappingStrategy;


/**
 * A factory for creating Validator instances.  The default configuration
 * of this factory uses the JDOM implementations for serviceValidateHandler
 * and proxyValidateHandler.
 *
 * @author Carl Harris
 * 
 */
public class ValidatorFactory {

  private static ProtocolSource protocolSource =
      new UrlProtocolSource();
  
  private static ProtocolHandler protocolHandler =
      new JdomProtocolHandlerImpl();
  
  private static ProtocolMappingStrategy serviceValidateMappingStrategy =
      new ServiceValidateMappingStrategy();

  private static ProtocolMappingStrategy proxyValidateMappingStrategy =
      new ProxyValidateMappingStrategy();


  /**
   * Gets a new validator instance that uses the JDOM implementation
   * of the various ProtocolHandler instances.
   * @return Validator instance.
   */
  public static final Validator getValidator(UrlGenerator generator) {
    DefaultValidatorImpl validator = new DefaultValidatorImpl();
    validator.setProtocolSource(getProtocolSource());
    validator.setProtocolHandler(protocolHandler);
    validator.setServiceValidateMappingStrategy(serviceValidateMappingStrategy);
    validator.setProxyValidateMappingStrategy(proxyValidateMappingStrategy);
    validator.setUrlGenerator(generator);
    return validator;
  }

  /**
   * Gets the <code>protocolHandler</code> property.
   * @return <code>ProtocolHandler}</code> property value
   */
  public static ProtocolHandler getProtocolHandler() {
    return protocolHandler;
  }

  /**
   * Sets the <code>protocolHandler</code> property.
   * @param protocolHandler <code>ProtocolHandler</code> property value
   */
  public static void setProtocolHandler(ProtocolHandler protocolHandler) {
    ValidatorFactory.protocolHandler = protocolHandler;
  }

  /**
   * Gets the <code>protocolSource</code> property.
   * @return <code>ProtocolSource}</code> property value
   */
  public static ProtocolSource getProtocolSource() {
    return protocolSource;
  }

  /**
   * Sets the <code>protocolSource</code> property.
   * @param protocolSource <code>ProtocolSource</code> property value
   */
  public static void setProtocolSource(ProtocolSource protocolSource) {
    ValidatorFactory.protocolSource = protocolSource;
  }

  /**
   * Gets the <code>proxyValidateMappingStrategy</code> property.
   * @return <code>ProtocolMappingStrategy}</code> property value
   */
  public static ProtocolMappingStrategy getProxyValidateMappingStrategy() {
    return proxyValidateMappingStrategy;
  }

  /**
   * Sets the <code>proxyValidateMappingStrategy</code> property.
   * @param proxyValidateMappingStrategy <code>ProtocolMappingStrategy</code>
   *    property value
   */
  public static void setProxyValidateMappingStrategy(
      ProtocolMappingStrategy proxyValidateMappingStrategy) {
    ValidatorFactory.proxyValidateMappingStrategy =
        proxyValidateMappingStrategy;
  }

  /**
   * Gets the <code>serviceValidateMappingStrategy</code> property.
   * @return <code>ProtocolMappingStrategy}</code> property value
   */
  public static ProtocolMappingStrategy getServiceValidateMappingStrategy() {
    return serviceValidateMappingStrategy;
  }

  /**
   * Sets the <code>serviceValidateMappingStrategy</code> property.
   * @param serviceValidateMappingStrategy <code>ProtocolMappingStrategy</code>
   *    property value
   */
  public static void setServiceValidateMappingStrategy(
      ProtocolMappingStrategy serviceValidateMappingStrategy) {
    ValidatorFactory.serviceValidateMappingStrategy =
        serviceValidateMappingStrategy;
  }

}
