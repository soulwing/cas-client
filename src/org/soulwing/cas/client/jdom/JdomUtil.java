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
 * String used in XML elements and attributes for the CAS protocol are loaded
 * by this class from the <code>ProtocolNames.properties</code> resource when
 * the class is loaded.  In the standard distribution, the file contains the
 * official names defined by the CAS 2 protocol.  Some institutions use a
 * modified version of the protocol which renames some of the protocol's 
 * XML elements and attributes.  In such cases the <code>ProtocolNames.properties</code> 
 * file and be overridden by placing a customized version on the classpath
 * ahead of the copy of the file stored in the JAR file with this class.
 * When the client is being used in a standard web application, a customized
 * version of <code>ProtocolNames.properties</code> can be placed in a
 * <code>WEB-INF/classes</code> in a subdirectory that corresponds to the 
 * package name of this class (as specified above).  When the client is integrated
 * with Tomcat, the file should be placed in a package-appropriate subdirectory
 * under <code>$CATALINA_BASE/common/classes</code>.
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
  private static String mapName(String name) {
    String mappedName = nameMap.getProperty(name);
    if (mappedName == null) {
      throw new IllegalArgumentException("undefined protocol name: " + name);
    }
    return mappedName;
  }
  
  public static final Element getChild(Element element, String name) {
    return element.getChild(mapName(name), CAS_NAMESPACE);
  }

  public static final List getChildren(Element element, String name) {
    return element.getChildren(mapName(name), CAS_NAMESPACE);
  }

  public static final String getAttributeValue(Element element, String name) {
    return element.getAttributeValue(mapName(name));
  }
}
