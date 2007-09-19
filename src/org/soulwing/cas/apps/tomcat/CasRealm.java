/*
 * CasRealm.java
 *
 * Created on Sep 19, 2007
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
package org.soulwing.cas.apps.tomcat;

import java.security.Principal;

import org.apache.catalina.Realm;
import org.soulwing.cas.client.ProtocolConfiguration;


/**
 * Extends the Catalina Realm adding methods that allow access to the CAS
 * protocol configuration and authentication strategy.
 *
 * @author Carl Harris
 */
public interface CasRealm extends Realm {

  AuthenticationStrategy getAuthenticationStrategy();

  ProtocolConfiguration getProtocolConfiguration();

  Principal getPrincipal(String username);
  
}