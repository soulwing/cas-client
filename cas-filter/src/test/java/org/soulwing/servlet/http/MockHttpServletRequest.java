/*
 * MockHttpServletRequest.java
 *
 * Created on Sep 8, 2006
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
package org.soulwing.servlet.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.soulwing.servlet.MockRequestDispatcher;


/**
 * A mock up for HttpServletRequest.
 *
 * @author Carl Harris
 * 
 */
public class MockHttpServletRequest implements HttpServletRequest {

  private final Map<String, List<String>> parameters = 
      new LinkedHashMap<String, List<String>>();
  
  private final Map<String, Object> attributes = 
      new LinkedHashMap<String, Object>();
  
  private final String contextPath;
  private String servletPath;
  private String pathInfo;
  private String scheme;
  private String serverName;
  private int serverPort;
  private HttpSession session;
  private MockRequestDispatcher requestDispatcher;

  public MockHttpServletRequest() {
    this.contextPath = "/";
  }
  
  public MockHttpServletRequest(String url) {
    this();
    setRequestURL(url);
  }

  public MockHttpServletRequest(String contextPath, String url) {
    this.contextPath = contextPath;
    setRequestURL(url);
  }

  public String getContextPath() {
    return contextPath;
  }

  public String getRequestURI() {
    StringBuilder sb = new StringBuilder();
    if (getContextPath().endsWith("/")) {
      sb.append(getContextPath().substring(0, getContextPath().length() - 1));
    }
    else {
      sb.append(getContextPath());
    }
    sb.append(getServletPath());
    return sb.toString();
  }

  public StringBuffer getRequestURL() {
    StringBuffer sb = new StringBuffer();
    if (getScheme() != null) {
      sb.append(getScheme());
      sb.append(':');
    }
    if (getServerName() != null  && getServerName().length() > 0) {
      sb.append("//");
      sb.append(getServerName());
    }
    if (getServerPort() >= 0) {
      sb.append(':');
      sb.append(getServerPort());
    }
    sb.append(getRequestURI());
    return sb;
  }
  
  public void setRequestURL(String url) {
    try {
      URL u = new URL(url);
      setScheme(u.getProtocol());
      setServerName(u.getHost());
      setServerPort(u.getPort());
      if (contextPath == null || contextPath.equals("/")) {
        setServletPath(u.getPath());
      }
      else {
        String path = u.getPath();
        if (path.startsWith(contextPath)) {
          setServletPath(path.substring(contextPath.length()));
        }
        else {
          setServletPath(path);
        }
      }
      setQueryString(u.getQuery());
    }
    catch (MalformedURLException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
  
  public String getQueryString() {
    StringBuilder sb = new StringBuilder();
    Map parameters = getParameterMap();
    for (Iterator i = parameters.keySet().iterator(); i.hasNext(); ) {
      String name = (String) i.next();
      List values = (List) parameters.get(name);
      for (Iterator j = values.iterator(); j.hasNext(); ) {
        sb.append(name);
        sb.append('=');
        sb.append((String) j.next());
        if (j.hasNext()) {
          sb.append('&');
        }
      }
      if (i.hasNext()) {
        sb.append('&');
      }
    }
    return sb.toString();
  }
  
  public void setQueryString(String queryString) {
    setParameterMap(new LinkedHashMap<String, List<String>>());
    if (queryString == null) {
      return;
    }
    String[] parameters = queryString.split("&");
    for (int i = 0; i < parameters.length; i++) {
      String parameter = parameters[i];
      int j = parameter.indexOf('=');
      String name = parameter.substring(0, j);
      String value = parameter.substring(j + 1);
      setParameter(name, value);
    }
  }

  public String getParameter(String name) {
    if (this.parameters.containsKey(name)) {
      return (String) ((List) this.parameters.get(name)).get(0);
    }
    else {
      return null;
    }
  }

  public Map getParameterMap() {
    return this.parameters;
  }

  public void setParameter(String name, String value) {
    if (!this.parameters.containsKey(name)) {
      this.parameters.put(name, new ArrayList<String>());
    }
    List<String> values = this.parameters.get(name);
    values.add(value);
  }
  
  @SuppressWarnings("unchecked")
  public void setParameterMap(Map parameters) {
    this.parameters.clear();
    this.parameters.putAll(parameters);
  }
  
  public Object getAttribute(String name) {
    return attributes.get(name);
  }

  public void setAttribute(String name, Object value) {
    attributes.put(name, value);
  }

  public void removeAttribute(String name) {
    attributes.remove(name);
  }

  public String getScheme() {
    return this.scheme;
  }
  
  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  public String getServerName() {
    return this.serverName;
  }

  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

  public int getServerPort() {
    return this.serverPort;
  }

  public void setServerPort(int serverPort) {
    this.serverPort = serverPort;
  }

  public HttpSession getSession() {
    return getSession(true);
  }

  public HttpSession getSession(boolean createSession) {
    if (session == null && createSession) {
      session = new MockHttpSession();
    }
    return session;
  }

  public String getServletPath() {
    return servletPath;
  }

  public void setServletPath(String servletPath) {
    this.servletPath = servletPath;
  }

  public RequestDispatcher getRequestDispatcher(String resourcePath) {
    requestDispatcher.setResourcePath(resourcePath);
    return requestDispatcher;
  }

  public void setRequestDispatcher(MockRequestDispatcher requestDispatcher) {
    this.requestDispatcher = requestDispatcher;
    
  }

  public String getPathInfo() {
    return pathInfo;
  }

  public void setPathInfo(String pathInfo) {
    this.pathInfo = pathInfo;
  }

  public Enumeration getAttributeNames() {
    throw new UnsupportedOperationException();
  }

  public String getAuthType() {
    throw new UnsupportedOperationException();
  }
  
  public Cookie[] getCookies() {
    throw new UnsupportedOperationException();
  }

  public long getDateHeader(String arg0) {
    throw new UnsupportedOperationException();
  }

  public String getHeader(String arg0) {
    throw new UnsupportedOperationException();
  }

  public Enumeration getHeaders(String arg0) {
    throw new UnsupportedOperationException();
  }

  public Enumeration getHeaderNames() {
    throw new UnsupportedOperationException();
  }

  public int getIntHeader(String arg0) {
    throw new UnsupportedOperationException();
  }

  public String getMethod() {
    throw new UnsupportedOperationException();
  }

  public String getPathTranslated() {
    throw new UnsupportedOperationException();
  }

  public String getRemoteUser() {
    throw new UnsupportedOperationException();
  }

  public boolean isUserInRole(String arg0) {
    throw new UnsupportedOperationException();
  }

  public Principal getUserPrincipal() {
    throw new UnsupportedOperationException();
  }

  public String getRequestedSessionId() {
    throw new UnsupportedOperationException();
  }

  public boolean isRequestedSessionIdValid() {
    throw new UnsupportedOperationException();
  }

  public boolean isRequestedSessionIdFromCookie() {
    throw new UnsupportedOperationException();
  }

  public boolean isRequestedSessionIdFromURL() {
    throw new UnsupportedOperationException();
  }

  public boolean isRequestedSessionIdFromUrl() {
    throw new UnsupportedOperationException();
  }

  public String getCharacterEncoding() {
    throw new UnsupportedOperationException();
  }

  public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
    throw new UnsupportedOperationException();
  }

  public int getContentLength() {
    throw new UnsupportedOperationException();
  }

  public String getContentType() {
    throw new UnsupportedOperationException();
  }

  public ServletInputStream getInputStream() throws IOException {
    throw new UnsupportedOperationException();
  }

  public Enumeration getParameterNames() {
    throw new UnsupportedOperationException();
  }

  public String[] getParameterValues(String arg0) {
    throw new UnsupportedOperationException();
  }

  public String getProtocol() {
    throw new UnsupportedOperationException();
  }

  public boolean isSecure() {
    throw new UnsupportedOperationException();
  }

  public BufferedReader getReader() throws IOException {
    throw new UnsupportedOperationException();
  }

  public String getRemoteAddr() {
    throw new UnsupportedOperationException();
  }

  public String getRemoteHost() {
    throw new UnsupportedOperationException();
  }

  public Locale getLocale() {
    throw new UnsupportedOperationException();
  }

  public Enumeration<Locale> getLocales() {
    throw new UnsupportedOperationException();
  }

  public String getRealPath(String arg0) {
    throw new UnsupportedOperationException();
  }

  public int getRemotePort() {
    throw new UnsupportedOperationException();
  }

  public String getLocalName() {
    throw new UnsupportedOperationException();
  }

  public String getLocalAddr() {
    throw new UnsupportedOperationException();
  }

  public int getLocalPort() {
    throw new UnsupportedOperationException();
  }

}
