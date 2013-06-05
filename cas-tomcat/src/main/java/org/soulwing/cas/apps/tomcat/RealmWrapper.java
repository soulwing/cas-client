/*
 * RealmWrapper.java
 *
 * Created on Dec 6, 2007
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

import java.lang.reflect.Method;
import java.security.Principal;

import org.apache.catalina.Realm;
import org.apache.catalina.realm.RealmBase;

/**
 * A wrapper for a Tomcat <code>RealmBase</code> that adds the ability to
 * obtain a <code>Principal</code> for a username without performing
 * authentication.  It does this by invoking the protected 
 * <code>RealmBase.getPrincipal</code> method via reflection.
 * 
 * It would be really nice if Tomcat's <code>Realm</code> interface simply
 * exposed <code>getPrincipal</code> as a public method for authentication
 * mechanisms like CAS that simply need access to a <code>Realm</code> for
 * authorization.
 * 
 * @author Carl Harris
 */
class RealmWrapper {

  private final Realm realm;
  private final Method getPrincipalMethod;
  
  public RealmWrapper(Realm realm) {
    if (!(realm instanceof RealmBase)) {
      throw new IllegalArgumentException(
          "wrapper requires a subclass of " 
          + RealmBase.class.getCanonicalName());
    }
    this.realm = realm;
    this.getPrincipalMethod = findGetPrincipalMethod((RealmBase) realm);
  }

  private Method findGetPrincipalMethod(RealmBase realm) {
    try {
      Method method = realm.getClass().getDeclaredMethod("getPrincipal", 
          new Class[] { String.class });
      method.setAccessible(true);
      return method;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
  
  public Principal authenticate(String username, String password) {
    return realm.authenticate(username, password);
  }

  public Principal getPrincipal(String username) {
    try {
      return (Principal) getPrincipalMethod.invoke(realm, 
          new Object[] { username });
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
