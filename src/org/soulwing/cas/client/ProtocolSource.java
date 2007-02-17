/*
 * ProtocolSource.java
 *
 * Created on Sep 8, 2006
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
