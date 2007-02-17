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
 * A simple bypass filter that accepts a single servlet path to bypass.
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
    String[] paths = new FilterConfigurator(filterConfig)
        .getRequiredParameter(BYPASS_PATHS).split("\\s*,\\s*");
    if (paths.length > 0) {
      GlobCompiler compiler = new GlobCompiler();
      bypassPaths = new Pattern[paths.length];
      for (int i = 0; i < paths.length; i++) {
        try {
          bypassPaths[i] = compiler.compile(paths[i]);
        }
        catch (MalformedPatternException ex) {
          throw new ServletException("compile error for pattern "
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
