/*
 * ProtocolHandler.java
 *
 * Created on Sep 7, 2006
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
 * An implementation of ProtocolHandler processes a response from the CAS server
 * for a particular CAS function (e.g. <code>/serviceValidate</code>). It
 * parses the XML input that represents the response from the CAS server, and
 * instantiates and appropriate subclass of the Response class.
 * 
 * @author Carl Harris
 * 
 */
public interface ProtocolHandler {

  /**
   * Processes a CAS response from the given <code>result</code>
   * InputSource.
   * @param result SAX input source containing the response from the CAS
   *    server.
   * @return protocol response as a subclass of Response.
   */
  Response processResult(InputSource result);
  
}
