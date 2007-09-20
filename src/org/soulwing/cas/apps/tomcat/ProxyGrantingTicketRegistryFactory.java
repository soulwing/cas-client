/*
 * ProxyGrantingTicketRegistryFactory.java
 *
 * Created on Sep 20, 2007 
 */
package org.soulwing.cas.apps.tomcat;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A JNDI object creation factory for ProxyGrantingTicketRegistry instances. 
 *
 * @author Carl Harris
 */
public class ProxyGrantingTicketRegistryFactory implements ObjectFactory {

  private static final Log log = 
      LogFactory.getLog(ProxyGrantingTicketRegistryFactory.class);
  
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
    if (!ProxyGrantingTicketRegistry.class.isAssignableFrom(
        Class.forName(ref.getClassName()))) {
      log.error("expected a reference type of "
          + ProxyGrantingTicketRegistry.class.getCanonicalName());
      return null;
    }
    
    log.debug("creating default " 
        + ProxyGrantingTicketRegistry.class.getCanonicalName());
    return new ProxyGrantingTicketRegistryImpl();
    
  }

}
