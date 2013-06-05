/*
 * ProxySuccessResponse.java
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
package org.soulwing.cas.client.jdom;

import org.soulwing.cas.client.AbstractResponse;
import org.soulwing.cas.client.ProxyResponse;


/**
 * A response received for an invocation of the CAS <code>/proxy</code>
 * operation.
 *
 * @author Carl Harris
 */
public class ProxyFailureResponse extends AbstractResponse 
    implements ProxyResponse {

  /*
   * (non-Javadoc)
   * @see org.soulwing.cas.client.ProxyResponse#getProxyTicket()
   */
  public String getProxyTicket() {
    throw new IllegalStateException("Can't get proxy ticket from failed response");
  }

}
