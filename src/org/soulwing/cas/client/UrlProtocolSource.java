/*
 * UrlProtocolSource.java
 *
 * Created on Sep 12, 2006
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
  
  private URL getURL(String url) {
    try {
      return new URL(url);
    }
    catch (MalformedURLException ex) {
      throw new ServiceAccessException(ex);
    }
  }

  /**
   * @see ProtocolSource#getSource
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

}
