/*
 * SimpleAuthorizationFilter.java
 *
 * Created on Sep 12, 2006
 */
package org.soulwing.cas.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * An authorization filter for which a list of authorized usernames is 
 * configured as an initialization parameter.
 *
 * @author Carl Harris
 * 
 */
public class SimpleAuthorizationFilter implements Filter {

  private static final String AUTHORIZED_UESRS_PARAM = "authorizedUsers";
  private List authorizedUsers = new ArrayList(0);
  
  public void init(FilterConfig config) throws ServletException {
    String authorizedUsers = config.getInitParameter(AUTHORIZED_UESRS_PARAM);
    if (authorizedUsers != null) {
      this.authorizedUsers = Arrays.asList(
          authorizedUsers.split("\\p{Space}+"));
    }
  }

  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {

    doHttpFilter(
        (HttpServletRequest) request,
        (HttpServletResponse) response,
        filterChain);
  }

  public void destroy() {
    // nothing to destroy
  }

  private void doHttpFilter(HttpServletRequest request, 
      HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {

    String userName = request.getUserPrincipal().getName();
    if (authorizedUsers.contains(userName)) {
      filterChain.doFilter(request, response);
    }
    else {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
  }
}
