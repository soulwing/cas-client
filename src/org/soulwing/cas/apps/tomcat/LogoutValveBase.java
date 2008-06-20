package org.soulwing.cas.apps.tomcat;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Session;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.valves.ValveBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.SimpleUrlGenerator;
import org.soulwing.cas.filter.FilterConstants;

public abstract class LogoutValveBase extends ValveBase implements Lifecycle {

  private static final Log log = LogFactory.getLog(LogoutValve.class);
  private final LifecycleSupport lifecycleSupport =
      new LifecycleSupport(this);

  public void invoke(Request request, Response response) throws IOException,
      ServletException {
    
    LogoutStatus logoutStatus = getLogoutStatus(request);
    
    // short-circuit if not a logout request
    if (!logoutStatus.isLogout()) {
      if (log.isTraceEnabled()) {
        log.trace("URI " + request.getRequestURI() + " is not a logout request");
      }
      getNext().invoke(request, response);
      return;
    }
    
    log.debug("URI " + request.getRequestURI() + " is a logout request");
    Session session = request.getSessionInternal();
    if (session != null && session.isValid()) {
      session.removeNote(FilterConstants.VALIDATION_ATTRIBUTE);
    }
    if (logoutStatus.isGlobal()) {
      ResourceHelper helper =
          (ResourceHelper) request
              .getNote(ResourceValve.RESOURCE_HELPER_ATTR);
      SimpleUrlGenerator urlGenerator =
          new SimpleUrlGenerator(helper.getProtocolConfiguration());
      response.sendRedirect(urlGenerator.getLogoutUrl());
    }
    else if (logoutStatus.getRedirectUrl() != null) {
      response.sendRedirect(logoutStatus.getRedirectUrl());
    }
    else {
      log.warn("no post-logout redirect URL available");
    }

  }

  public abstract LogoutStatus getLogoutStatus(Request request);
  
  public void start() throws LifecycleException {
  }

  public void stop() throws LifecycleException {
  }

  public void addLifecycleListener(LifecycleListener lifecycleListener) {
    lifecycleSupport.addLifecycleListener(lifecycleListener);
  }

  public LifecycleListener[] findLifecycleListeners() {
    return lifecycleSupport.findLifecycleListeners();
  }

  public void removeLifecycleListener(LifecycleListener lifecycleListener) {
    lifecycleSupport.removeLifecycleListener(lifecycleListener);
  }

}