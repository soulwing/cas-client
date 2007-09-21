/*
 * CasProtocolConfigurationFactory.java
 *
 * Created on Sep 21, 2007 
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

/**
 * A JNDI resource factory for CAS ProtocolConfiguration resources.
 *
 * @author Carl Harris
 */
public class CasProtocolConfigurationFactory implements ObjectFactory {

  private static final Log log = 
      LogFactory.getLog(CasProtocolConfigurationFactory.class);
  
  /* (non-Javadoc)
   * @see javax.naming.spi.ObjectFactory#getObjectInstance(java.lang.Object, javax.naming.Name, javax.naming.Context, java.util.Hashtable)
   */
  public Object getObjectInstance(Object o, Name name, Context context,
      Hashtable env) throws Exception {
    
    if (o == null || !(o instanceof Reference)) {
      log.error("expected a Reference object");
      return null;
    }
    
    Reference ref = (Reference) o;
    if (ProtocolConfiguration.class.isAssignableFrom(
        Class.forName(ref.getClassName()))) {
      log.error("expected a " + ProtocolConfiguration.class + " type");
      return null;
    }
    
    ProtocolConfiguration protocolConfiguration = new ProtocolConfiguration();
    RefAddr ra = null;
    
    ra = ref.get("serverUrl");
    if (ra != null) {
      protocolConfiguration.setServerUrl(ra.getContent().toString());
    }
    
    ra = ref.get("serviceUrl");
    if (ra != null) {
      protocolConfiguration.setServiceUrl(ra.getContent().toString());
    }
    
    ra = ref.get("proxyCallbackUrl");
    if (ra != null) {
      protocolConfiguration.setProxyCallbackUrl(ra.getContent().toString());
    }
    
    ra = ref.get("renew");
    if (ra != null) {
      protocolConfiguration.setRenewFlag(
          Boolean.parseBoolean(ra.getContent().toString()));
    }

    ra = ref.get("gateway");
    if (ra != null) {
      protocolConfiguration.setGatewayFlag(
          Boolean.parseBoolean(ra.getContent().toString()));
    }

    return protocolConfiguration;
  }

}
