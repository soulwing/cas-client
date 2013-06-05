/*
 * FilterParameterException.java
 *
 * Created on Feb 7, 2007
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
package org.soulwing.cas.filter;

import javax.servlet.ServletException;


/**
 * An exception thrown by Configurator when there's a problem with
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
