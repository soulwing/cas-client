/*
 * CasUserDatabaseRealm.java
 *
 * Created on Sep 21, 2007 
 */
package org.soulwing.cas.apps.tomcat;

import java.security.Principal;

import org.apache.catalina.realm.UserDatabaseRealm;

/**
 * A Catalina UserDatabaseRealm that allows a Principal (with associated
 * roles) to be obtained without performing any authentication of credentials.
 * 
 * @author Carl Harris
 */
public class CasUserDatabaseRealm extends UserDatabaseRealm 
    implements AuthorizationOnlyRealm {

  public Principal getPrincipal(String username) {
    return getPrincipal(username);
  }

}
