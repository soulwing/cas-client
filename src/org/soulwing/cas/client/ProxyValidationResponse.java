/*
 * ProxyValidationResponse.java
 *
 * Created on Sep 8, 2006
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

import java.util.List;


/**
 * A response received for an invocation of the CAS <code>/proxyValidate</code>
 * operation.
 *
 * @author Carl Harris
 * @see ServiceValidationResponse
 */
public interface ProxyValidationResponse extends ServiceValidationResponse {

  /**
   * Gets the list of proxies from the CAS response.
   * @return list of String elements containing proxy names.
   */
  List getProxies();
  
}
