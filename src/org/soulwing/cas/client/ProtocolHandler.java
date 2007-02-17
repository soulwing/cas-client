/*
 * ProtocolHandler.java
 *
 * Created on Sep 7, 2006
 */
package org.soulwing.cas.client;

import org.xml.sax.InputSource;


/**
 * An implementation of ProtocolHandler processes a response from the
 * CAS server for a particular CAS function (e.g. 
 * <code>/serviceValidate</code>).  It parses the XML input that represents
 * the response from the CAS server, and instantiates and appropriate
 * subclass of the Response class. 
 *
 * @author Carl Harris
 * 
 */
public interface ProtocolHandler {

  /**
   * Processes a CAS response starting with the given <code>result</code>
   * Element.
   * @param result SAX input source containing the response from the CAS
   *    server.
   * @return protocol response as a subclass of Response.
   */
  Response processResult(InputSource result);
  
}
