/*
 * @COPYRIGHT_TEXT@
 * @LICENSE_TEXT@
 *
 * Created on Jun 20, 2008 
 */
package org.soulwing.cas.apps.tomcat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A <code>Valve</code> that listens for requests that match a configured
 * regular expression. If a matching request includes <code>global=true</code>
 * as a query parameter, the request is redirected to the configured CAS global
 * logout URL.
 * 
 * @author Ralf Lorenz
 * @author Carl Harris
 */
public class PatternMatchingLogoutValve extends LogoutValveBase {

  private static final Log log =
      LogFactory.getLog(PatternMatchingLogoutValve.class);
  
  private String urlRegex;
  private Pattern urlPattern;
  private String redirectUrl;

  public String getUrlRegex() {
    return urlRegex;
  }

  public void setUrlRegex(String urlRegex) {
    this.urlRegex = urlRegex;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  /*
   * (non-Javadoc)
   * @see org.soulwing.cas.apps.tomcat.LogoutValveBase#getLogoutStatus(org.apache.catalina.connector.Request)
   */
  public LogoutStatus getLogoutStatus(Request request) {
    Matcher matcher = urlPattern.matcher(request.getRequestURI());
    if (!matcher.matches()) {
      return LogoutStatus.NOT_LOGOUT;
    }
    
    LogoutStatus status = new LogoutStatus();
    status.setLogout(true);
    status.setGlobal(request.getParameter(LogoutValve.GLOBAL_LOGOUT_PARAM) != null);
    status.setRedirectUrl(getRedirectUrl());
    return status;
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.apps.tomcat.LogoutValveBase#start()
   */
  public void start() throws LifecycleException {
    super.start();
    if (this.urlRegex == null || this.urlRegex.length() == 0) {
      throw new LifecycleException(
          new IllegalArgumentException(
              "urlRegex must be configured"));
    }
    try {
      urlPattern = Pattern.compile(this.urlRegex);
    }
    catch (PatternSyntaxException psException) {
      log.error(
          "'" + this.urlRegex + "' is not a valid regular expression",
          psException);
      throw new LifecycleException(psException);
    }
  }

}
