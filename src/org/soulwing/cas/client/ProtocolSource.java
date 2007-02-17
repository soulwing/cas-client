/*
 * ProtocolSource.java
 *
 * Created on Sep 8, 2006
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

import org.xml.sax.InputSource;


/**
 * A ProtocolSource abstracts the notion of a CAS request as a URL
 * string representing the service point (function) on the CAS server, 
 * and the server's response as an XML document.
 *
 * @author Carl Harris
 * 
 */
public interface ProtocolSource {

  /**
   * Gets the CAS response document for a request given as a URL.
   * @param url fully parameterized CAS request URL.
   * @return SAX input source containing the response from the CAS server.
   */
  InputSource getSource(String url);
  
}
