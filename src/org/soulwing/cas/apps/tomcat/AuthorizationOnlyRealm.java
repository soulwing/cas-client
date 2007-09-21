/*
 * AuthorizationOnlyRealm.java
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


/**
 * A marker interface for a Realm that performs authorization related
 * tasks only.  This interface and the Realm subclasses in this package are
 * necessitated by Catalina's tight coupling of authentication and
 * authorization in the Realm abstraction.   A better design would have
 * separated these concerns and would make it much easier to leverage CAS
 * for authentication while utilizing a Realm merely to obtain role memberships.
 * 
 * This class would become unnecessary, if Realm exposed a method to simply
 * obtain the Principal that corresponds to a username without performing
 * any authentication.
 *
 * @author Carl Harris
 */
public interface AuthorizationOnlyRealm extends Realm {
  
  Principal getPrincipal(String username);

}