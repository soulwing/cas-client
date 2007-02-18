/*
 * SimpleBypassFilter.java
 *
 * Created on Feb 13, 2007
 */
package org.soulwing.cas.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.GlobCompiler;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Matcher;


/**
 * A filter that detects requests for servlet paths that should bypass
 * CAS authentication.  The <code>bypassPath</code> property is set to 
 * a comma-separated string of glob expressions that describe one or
 * more servlet paths that should be marked for CAS bypass.  When a request
 * for a path that matches one of the configured glob expressions is
 * received by the filter, it adds an attribute to the session that the
 * ValidationFilter will observe as a request to bypass validation of
 * CAS credentials.
 *
 * @author Carl Harris
 */
public class SimpleBypassFilter extends LogoutFilter {

  private static final String BYPASS_PATHS = "bypassPaths";
  
  private static final Log log = LogFactory.getLog(LogoutFilter.class);
  private Pattern[] bypassPaths;

  /*
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig filterConfig) throws ServletException {
    super.init(filterConfig);
    setBypassPaths(new FilterConfigurator(filterConfig)
        .getRequiredParameter(BYPASS_PATHS));
  }

  public void setBypassPaths(String bypassPaths) {
    setBypassPaths(bypassPaths.split("\\s*,\\s*"));
  }

  private void setBypassPaths(String[] paths) {
    if (paths.length > 0) {
      GlobCompiler compiler = new GlobCompiler();
      bypassPaths = new Pattern[paths.length];
      for (int i = 0; i < paths.length; i++) {
        try {
          bypassPaths[i] = compiler.compile(paths[i]);
        }
        catch (MalformedPatternException ex) {
          throw new IllegalArgumentException("compile error for pattern "
              + paths[i] + ": ", ex);
        }
      }
    }
  }
  
  /* 
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy() {
    // not needed
  }

  /* 
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    try {
      doHttpFilter(
          (HttpServletRequest) request, 
          (HttpServletResponse) response,
          filterChain);
    }
    catch (ClassCastException ex) {
      log.debug("Filter supports HTTP only");
      filterChain.doFilter(request, response);
    }
  }

  public void doHttpFilter(HttpServletRequest request, 
      HttpServletResponse response, FilterChain filterChain) 
      throws IOException, ServletException { 

    if (isBypassPath(request.getServletPath())) {
      log.debug("servlet path " + request.getServletPath() 
          + " marked for bypass");
      removeSessionState(request);
      request.getSession(true).setAttribute(FilterConstants.BYPASS_ATTRIBUTE,
          Boolean.valueOf(true));
    }
    super.doHttpFilter(request, response, filterChain);
  }

  private boolean isBypassPath(String servletPath) {
    boolean match = false;
    Perl5Matcher matcher = new Perl5Matcher();
    for (int i = 0; !match && i < bypassPaths.length; i++) {
      match = matcher.matches(servletPath, bypassPaths[i]);
    }
    return match;
  }
}
