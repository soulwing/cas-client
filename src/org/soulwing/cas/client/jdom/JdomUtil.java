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

import java.util.List;

import org.jdom.Element;
import org.jdom.Namespace;
import org.soulwing.cas.client.ProtocolConstants;

/**
 * Static utility methods for JDOM and the CAS namespace.
 *
 * @author Carl Harris
 */
public class JdomUtil {

  public static final Namespace CAS_NAMESPACE = 
      Namespace.getNamespace(ProtocolConstants.NAMESPACE_PREFIX,
          ProtocolConstants.NAMESPACE_URI);
  
  public static final Element getChild(Element element, String name) {
    return element.getChild(name, CAS_NAMESPACE);
  }

  public static final List getChildren(Element element, String name) {
    return element.getChildren(name, CAS_NAMESPACE);
  }

}
