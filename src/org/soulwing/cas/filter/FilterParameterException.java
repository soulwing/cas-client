/*
 * FilterParameterException.java
 *
 * Created on Feb 7, 2007
 */
package org.soulwing.cas.filter;

import javax.servlet.ServletException;


/**
 * An exception thrown by FilterConfigurator when there's a problem with
 * a parameter.
 *
 * @author Carl Harris
 */
public class FilterParameterException extends ServletException {

  private static final long serialVersionUID = 2320731399967506122L;

  public FilterParameterException() {
  }

  public FilterParameterException(String message) {
    super(message);
  }

  public FilterParameterException(Throwable cause) {
    super(cause);
  }

  public FilterParameterException(String message, Throwable cause) {
    super(message, cause);
  }

}
