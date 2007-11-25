/*
 * @COPYRIGHT_TEXT@
 * @LICENSE_TEXT@
 *
 * File created on Nov 21, 2007 
 */
package org.soulwing.cas.apps.tomcat;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ProtocolConfigurationImpl;

/**
 * A JDNI resource factory for ProtocolConfigurationImpl objects.
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
