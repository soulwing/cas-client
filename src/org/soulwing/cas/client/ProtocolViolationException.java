/*
 * ProtocolViolationException.java
 *
 * Created on Sep 8, 2006
 */
package org.soulwing.cas.client;


/**
 * An exception thrown when the CAS server returns results that
 * are not consistent with the CAS protocol.
 *
 * @author Carl Harris
 * 
 */
public class ProtocolViolationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ProtocolViolationException() {
    super();
  }

  public ProtocolViolationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ProtocolViolationException(String message) {
    super(message);
  }

  public ProtocolViolationException(Throwable cause) {
    super(cause);
  }

}
