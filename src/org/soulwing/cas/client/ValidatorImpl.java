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

import org.soulwing.cas.client.jdom.ProxyValidateHandler;
import org.soulwing.cas.client.jdom.ServiceValidateHandler;


/**
 * An implementation of the Validator interface.  This implementation is
 * essentially a facade over the RequestGenerator and ProtocolHandler types.
 *
 * @author Carl Harris
 * 
 */
public class ValidatorImpl implements Validator {

  private final ProtocolHandler serviceValidateHandler;
  private final ProtocolHandler proxyValidateHandler;
  private UrlGenerator generator;
  private ProtocolSource source = new UrlProtocolSource();
  
  public ValidatorImpl(
      ProtocolHandler serviceValidateHandler,
      ProtocolHandler proxyValidateHandler) {
    this.serviceValidateHandler = new ServiceValidateHandler();
    this.proxyValidateHandler = new ProxyValidateHandler();
  }

  /**
   * Gets the ProtocolSource used by this Validator.
   * @return configured ProtocolSource.
   */
  public ProtocolSource getProtocolSource() {
    return this.source;
  }
  
  /**
   * Sets the ProtocolSource that will be used by this Validator.
   * @param source ProtocolSource to configure.
   */
  public void setProtocolSource(ProtocolSource source) {
    this.source = source;
  }
  
  /**
   * Gets the UrlGenerator used by this Validator.
   * @return configured UrlGenerator instance
   */
  public UrlGenerator getUrlGenerator() {
    return this.generator;
  }
  
  /**
   * Sets the UrlGenerator that will be used by this Validator.
   * @param generator UrlGenerator to configure.
   */
  public void setUrlGenerator(UrlGenerator generator) {
    this.generator = generator;
  }

  /**
   * @see Validator#serviceValidate(ValidationRequest)
   */
  public ServiceValidationResponse serviceValidate(ValidationRequest request) 
      throws NoTicketException {
    
    return (ServiceValidationResponse) 
        this.serviceValidateHandler.processResult(
            getProtocolSource().getSource(
                getUrlGenerator().getServiceValidateUrl(request.getTicket())));
  }

  /**
   * @see Validator#proxyValidate(ValidationRequest)
   */
  public ProxyValidationResponse proxyValidate(ValidationRequest request) 
      throws NoTicketException {

    return (ProxyValidationResponse) 
        this.proxyValidateHandler.processResult(
            getProtocolSource().getSource(
                getUrlGenerator().getProxyValidateUrl(request.getTicket())));
  }

}
