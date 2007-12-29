/*
 * ProtocolConfigurationFactory.java
 *
 * Created on Nov 21, 2007 
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

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A JNDI resource factory for ProtocolConfigurationImpl objects.
 *
 * @author Carl Harris
 */
public class ProtocolConfigurationFactory implements ObjectFactory {

  private static final Log log = 
      LogFactory.getLog(ProtocolConfigurationFactory.class);

  /* (non-Javadoc)
   * @see javax.naming.spi.ObjectFactory#getObjectInstance(java.lang.Object, javax.naming.Name, javax.naming.Context, java.util.Hashtable)
   */
  public Object getObjectInstance(Object o, Name name, Context ctx,
      Hashtable env) throws Exception {

    if (o == null || !(o instanceof Reference)) {
      log.error("expected a Reference object");
      return null;
    }
    
    Reference ref = (Reference) o;
    if (!ProtocolConfiguration.class.isAssignableFrom(
        Class.forName(ref.getClassName()))) {
      log.error("expected a reference type of "
          + ProtocolConfiguration.class.getCanonicalName());
      return null;
    }

    RefAddr ra = null;
    ProtocolConfigurationImpl config = new ProtocolConfigurationImpl();
    
    ra = ref.get("serverUrl");
    if (ra != null) {
      config.setServerUrl(ra.getContent().toString());
    }
    else {
      log.error("serverUrl property is required");
      return null;
    }
    
    ra = ref.get("serviceUrl");
    if (ra != null) {
      config.setServiceUrl(ra.getContent().toString());
    }
    
    ra = ref.get("proxyCallbackUrl");
    if (ra != null) {
      config.setProxyCallbackUrl(ra.getContent().toString());
    }
    
    ra = ref.get("renew");
    if (ra != null) {
      config.setRenewFlag(
          Boolean.parseBoolean(ra.getContent().toString().toLowerCase()));
    }
    
    ra = ref.get("gateway");
    if (ra != null) {
      config.setGatewayFlag(
          Boolean.parseBoolean(ra.getContent().toString().toLowerCase()));
    }
    
    return config;
  }

}
