/*
 * ProtocolConfigurationHolder.java
 *
 * Created on Mar 1, 2007
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
