/*
 * ProtocolConfigurationHolder.java
 *
 * Created on Mar 1, 2007
 */
package org.soulwing.cas.client;



/**
 * A filter that serves as a holder for a ProtocolConfiguration instance.
 * This filter can be used in applications that don't use a dependency
 * injection framework (e.g. Spring) to provide the CAS protocol configuration
 * to downstream filters.
 *
 * @author Carl Harris
 */
public class ProtocolConfigurationHolder {

  private static ProtocolConfiguration config;
  
  public static ProtocolConfiguration getRequiredConfiguration() {
    if (getConfiguration() == null) {
      throw new IllegalStateException(
          "Required protocol configuration is not available");
    }
    return getConfiguration();
  }

  public synchronized static ProtocolConfiguration getConfiguration() {
    return config;
  }

  public synchronized static void setConfiguration(ProtocolConfiguration config) {
    ProtocolConfigurationHolder.config = config;
  }
  
}
