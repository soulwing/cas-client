/*
 * UrlProtocolSource.java
 *
 * Created on Sep 12, 2006
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;


/**
 * A ProtocolSource fetched from a URL connection.
 *
 * @author Carl Harris
 * 
 */
public class UrlProtocolSource implements ProtocolSource {

  private static final Log log = LogFactory.getLog(UrlProtocolSource.class);
  
  /*
   * @see org.soulwing.cas.client.ProtocolSource#getSource(java.lang.String)
   */
  public InputSource getSource(String url) {    
    try {
      log.debug("requesting " + url);
      URLConnection connection = getURL(url).openConnection();
      connection.setRequestProperty("Connection", "close");
      return new InputSource(connection.getInputStream());
    }
    catch (IOException ex) {
      throw new ServiceAccessException(ex);
    }
  }

  private URL getURL(String url) {
    try {
      return new URL(url);
    }
    catch (MalformedURLException ex) {
      throw new ServiceAccessException(ex);
    }
  }

}
