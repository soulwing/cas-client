/*
 * File created on Sep 9, 2014 
 *
 * Copyright (c) 2014 Virginia Polytechnic Institute and State University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.soulwing.cas.apps.undertow;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ProtocolConfigurationImpl;

/**
 * A {@link ProtocolConfiguration} that gets loaded from a {@link Properties}
 * collection.
 *
 * @author Carl Harris
 */
public class PropertiesProtocolConfiguration
    extends ProtocolConfigurationImpl {

  /**
   * Loads the configuration from the properties resource at the given location.
   * @param location the subject location
   * @throws IOException
   */
  public void load(URL location) throws IOException {
    Properties properties = loadProperties(location);
    setServerUrl(properties.getProperty("serverUrl"));
    setServiceUrl(properties.getProperty("serviceUrl"));
    setProxyCallbackUrl(properties.getProperty("proxyCallbackUrl"));
    setRenewFlag(Boolean.parseBoolean(properties.getProperty("renew", 
        Boolean.FALSE.toString())));
    setGatewayFlag(Boolean.parseBoolean(properties.getProperty("gateway",
        Boolean.FALSE.toString())));
  }

  private Properties loadProperties(URL location) throws IOException {
    if (location == null) {
      throw new NullPointerException("location is required");
    }
    InputStream inputStream = location.openStream();
    return loadProperties(inputStream);
  }

  /**
   * Loads the configuration from the properties collection that appears
   * on the given stream.
   * @param inputStream the source stream
   * @throws IOException
   */
  private Properties loadProperties(InputStream inputStream) 
      throws IOException {
    if (inputStream == null) {
      throw new NullPointerException("inputStream is required");
    }
    try {
      Properties properties = new Properties();
      properties.load(inputStream);
      return properties;
    }
    finally {
      try {
        inputStream.close();
      }
      catch (IOException ex) {
        ex.printStackTrace(System.err);
      }
    }
  }
  
}
