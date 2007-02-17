/*
 * ServiceAccessException.java
 *
 * Created on Sep 8, 2006
 */
package org.soulwing.cas.client;


/**
 * An exception thrown when an I/O or other network-related exception occurs 
 * in connecting to the CAS server, making a request, and getting the
 * response. 
 *
 * @author Carl Harris
 * 
 */
public class ServiceAccessException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ServiceAccessException() {
    super();
  }

  public ServiceAccessException(String message) {
    super(message);
  }

  public ServiceAccessException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceAccessException(Throwable cause) {
    super(cause);
  }

}
