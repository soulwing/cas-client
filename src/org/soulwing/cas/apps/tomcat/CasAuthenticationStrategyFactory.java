/*
 * CasAuthenticationStrategyFactory.java
 *
 * Created on Sep 21, 2007 
 */
package org.soulwing.cas.apps.tomcat;

import java.lang.reflect.Constructor;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import org.soulwing.cas.client.ProtocolConfiguration;

/**
 * A JNDI resource factory for CAS AuthenticationStrategy objects.
 *
 * @author Carl Harris
 */
public class CasAuthenticationStrategyFactory implements ObjectFactory {

  /* (non-Javadoc)
   * @see javax.naming.spi.ObjectFactory#getObjectInstance(java.lang.Object, javax.naming.Name, javax.naming.Context, java.util.Hashtable)
   */
  public Object getObjectInstance(Object o, Name name, Context context,
      Hashtable env) throws Exception {
    
    if (o == null || !(o instanceof Reference)) {
      throw new IllegalArgumentException("expected a Reference object");
    }
    
    Reference ref = (Reference) o;
    if (!(AuthenticationStrategy.class.isAssignableFrom(
        Class.forName(ref.getClassName())))) {
      throw new IllegalArgumentException("expected an " 
          + AuthenticationStrategy.class.getCanonicalName() + " type");
    }

    RefAddr ra = null;

    Class strategyClass = null;
    ra = ref.get("strategyClass");
    if (ra != null) {
      strategyClass = Class.forName(ra.getContent().toString());
    }

    String configResourceName = null;
    ra = ref.get("config");
    if (ra != null) {
      configResourceName = ra.getContent().toString();
    }

    ProtocolConfiguration protocolConfiguration = (ProtocolConfiguration) 
        context.lookup(configResourceName);
    
    if (!(AuthenticationStrategy.class.isAssignableFrom(strategyClass))) {
      throw new IllegalArgumentException("a subclass of "
          + AuthenticationStrategy.class.getCanonicalName()
          + " is required");
    }
    
    Constructor constructor = strategyClass.getConstructor(
        new Class[] { ProtocolConfiguration.class });
    return constructor.newInstance(new Object[] { protocolConfiguration });
  }

}
