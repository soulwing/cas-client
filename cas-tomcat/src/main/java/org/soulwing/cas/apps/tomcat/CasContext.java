/*
 * CasContext.java
 *
 * Created on Feb 25, 2011
 *
 * Copyright (C) 2011 Carl E Harris, Jr.
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

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.deploy.LoginConfig;

/**
 * A {@link StandardContext} that uses overrides the {@code <login-config>} 
 * specified in {@code web.xml}, using CAS instead.
 *
 * @author Carl Harris
 */
public class CasContext extends StandardContext {

  public LoginConfig getLoginConfig() {
    LoginConfig config = super.getLoginConfig();
    config.setAuthMethod(CasAuthenticator.CAS_AUTH);
    return config;
  }
  
}
