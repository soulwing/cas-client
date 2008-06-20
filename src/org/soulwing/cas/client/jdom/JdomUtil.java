/*
 * JdomUtil.java
 *
 * Created on Jul 7, 2007 
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
package org.soulwing.cas.client.jdom;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Static utility methods for JDOM and the CAS namespace.
 *
 * @author Carl Harris
 */
public class JdomUtil {

  private static final String PROTOCOL_NAMES_PROPERTIES = 
      "ProtocolNames.properties";

  private static final String NAMESPACE_URI = "namespace.uri";
  private static final String NAMESPACE_PREFIX = "namespace.prefix";
  
  protected static final Log log = LogFactory.getLog(JdomUtil.class);

  private static final Properties nameMap = new Properties();
  
  static final Namespace CAS_NAMESPACE;

  static {
    try {
      loadNameMap();
      CAS_NAMESPACE = Namespace.getNamespace(
          mapName(NAMESPACE_PREFIX), mapName(NAMESPACE_URI));
    }
    catch (Exception ex) {
      throw new ExceptionInInitializerError(ex);
    }
  }

  private static void loadNameMap() throws IOException {
    InputStream inputStream = null;
    try {
      inputStream = JdomUtil.class.getResourceAsStream(PROTOCOL_NAMES_PROPERTIES);
      nameMap.load(inputStream);
      log.info(PROTOCOL_NAMES_PROPERTIES);
    }
    finally {
      if (inputStream == null) {
        try {
          inputStream.close();
        }
        catch (IOException ex) {
          // oh, well
          log.error("failed to close input stream " + PROTOCOL_NAMES_PROPERTIES);
        }
      }
    }
  }
  
  /**
   * Maps a standard protocol name to the corresponding name defined
   * in ProtocolNames.properties.
   * @param name the name to map
   * @return the mapped name
   * @throws IllegalArgumentException if <code>name</code> is not mapped
   */
  protected static String mapName(String name) {
    String mappedName = nameMap.getProperty(name);
    if (mappedName == null) {
      throw new IllegalArgumentException("undefined protocol name: " + name);
    }
    return mappedName;
  }
  
  public static final Element getChild(Element element, String name) {
    return element.getChild(name, CAS_NAMESPACE);
  }

  public static final List getChildren(Element element, String name) {
    return element.getChildren(name, CAS_NAMESPACE);
  }

}
