/*
 * @COPYRIGHT_TEXT@
 * @LICENSE_TEXT@
 *
 * Created on Jun 20, 2008 
 */
package org.soulwing.cas.apps.tomcat;

/**
 * A bean that represents the status for a possible logout request.
 * 
 * @author Carl Harris
 */
public class LogoutStatus {

  private boolean logout;
  private boolean global;
  private String redirectUrl;

  public static final LogoutStatus NOT_LOGOUT = new LogoutStatus(false, false, null);
  
  public LogoutStatus() {
  }
  
  public LogoutStatus(boolean logout, boolean global, String redirectUrl) {
    setLogout(logout);
    setGlobal(global);
    setRedirectUrl(redirectUrl);
  }
  
  /**
   * Gets the <code>logout</code> property.
   */
  public boolean isLogout() {
    return logout;
  }

  /**
   * Sets the <code>logout</code> property.
   */
  public void setLogout(boolean logout) {
    this.logout = logout;
  }

  /**
   * Gets the <code>global</code> property.
   */
  public boolean isGlobal() {
    return global;
  }

  /**
   * Sets the <code>global</code> property.
   */
  public void setGlobal(boolean global) {
    this.global = global;
  }

  /**
   * Gets the <code>redirectUrl</code> property.
   */
  public String getRedirectUrl() {
    return redirectUrl;
  }

  /**
   * Sets the <code>redirectUrl</code> property.
   */
  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

}
