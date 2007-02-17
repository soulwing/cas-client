/*
 * ValidatorFactory.java
 *
 * Created on Sep 14, 2006
 */
package org.soulwing.cas.client;

import org.soulwing.cas.client.jdom.ProxyValidateHandler;
import org.soulwing.cas.client.jdom.ServiceValidateHandler;


/**
 * A factory for creating Validator instances.  The default configuration
 * of this factory uses the JDOM implementations for serviceValidateHandler
 * and proxyValidateHandler.
 *
 * @author Carl Harris
 * 
 */
public class ValidatorFactory {

  private static ProtocolHandler serviceValidateHandler =
      new ServiceValidateHandler();
  
  private static ProtocolHandler proxyValidateHandler =
      new ProxyValidateHandler();
  
  private static ProtocolSource protocolSource =
      new UrlProtocolSource();
  
  /**
   * Gets a new validator instance that uses the JDOM implementation
   * of the various ProtocolHandler instances.
   * @return Validator instance.
   */
  public static final Validator getValidator(UrlGenerator generator) {

    ValidatorImpl validator = new ValidatorImpl(
        getServiceValidateHandler(),
        getProxyValidateHandler());
    
    validator.setProtocolSource(getProtocolSource());
    validator.setUrlGenerator(generator);
    return validator;
  }

  /**
   * Gets the handler for the CAS <code>/proxyValidate</code> function.
   */
  public static ProtocolHandler getProxyValidateHandler() {
    return proxyValidateHandler;
  }

  /**
   * Sets the handler for the CAS <code>/proxyValidate</code> function.
   */
  public static synchronized void setProxyValidateHandler(
      ProtocolHandler proxyValidateHandler) {
    ValidatorFactory.proxyValidateHandler = proxyValidateHandler;
  }

  /**
   * Gets the handler for the CAS <code>/serviceValidate</code> function.
   */
  public static ProtocolHandler getServiceValidateHandler() {
    return serviceValidateHandler;
  }

  /**
   * Sets the handler for the CAS <code>/serviceValidate</code> function.
   */
  public static synchronized void setServiceValidateHandler(
      ProtocolHandler serviceValidateHandler) {
    ValidatorFactory.serviceValidateHandler = serviceValidateHandler;
  }

  /**
   * Gets the ProtocolSource that will be used by Validator instances
   * created by this factory.
   */
  public static ProtocolSource getProtocolSource() {
    return protocolSource;
  }

  /**
   * Sets the ProtocolSource that will be used by Validator instances
   * created by this factory.
   */
  public static synchronized void setProtocolSource(
      ProtocolSource protocolSource) {
    ValidatorFactory.protocolSource = protocolSource;
  }

}
