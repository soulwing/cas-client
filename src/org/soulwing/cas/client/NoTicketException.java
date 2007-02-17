/*
 * NoTicketException.java
 *
 * Created on Sep 8, 2006
 */
package org.soulwing.cas.client;


/**
 * An exception thrown when an attempt to validate a request via
 * CAS does not contain a service ticket.
 *
 * @author Carl Harris
 * 
 */
public class NoTicketException extends Exception {

  private static final long serialVersionUID = 1L;

  public NoTicketException() {
    super();
  }

  public NoTicketException(String message) {
    super(message);
  }

  public NoTicketException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoTicketException(Throwable cause) {
    super(cause);
  }

}
