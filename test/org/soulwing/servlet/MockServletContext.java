/*
 * MockServletContext.java
 *
 * Created on Feb 10, 2007
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
package org.soulwing.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;


/**
 * Mock ServletContext for unit testing.
 *
 * @author Carl Harris
 */
public class MockServletContext implements ServletContext {

  /* 
   * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
   */
  public Object getAttribute(String arg0) {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getAttributeNames()
   */
  public Enumeration<String> getAttributeNames() {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getContext(java.lang.String)
   */
  public ServletContext getContext(String arg0) {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
   */
  public String getInitParameter(String arg0) {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getInitParameterNames()
   */
  public Enumeration<String> getInitParameterNames() {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getMajorVersion()
   */
  public int getMajorVersion() {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
   */
  public String getMimeType(String arg0) {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getMinorVersion()
   */
  public int getMinorVersion() {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
   */
  public RequestDispatcher getNamedDispatcher(String arg0) {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
   */
  public String getRealPath(String arg0) {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
   */
  public RequestDispatcher getRequestDispatcher(String arg0) {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getResource(java.lang.String)
   */
  public URL getResource(String arg0) throws MalformedURLException {
    return new URL("https://localhost/app" + arg0);
  }

  /* 
   * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
   */
  public InputStream getResourceAsStream(String arg0) {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
   */
  public Set<String> getResourcePaths(String arg0) {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getServerInfo()
   */
  public String getServerInfo() {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getServlet(java.lang.String)
   */
  public Servlet getServlet(String arg0) throws ServletException {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getServletContextName()
   */
  public String getServletContextName() {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getServletNames()
   */
  public Enumeration<String> getServletNames() {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getServlets()
   */
  public Enumeration<Servlet> getServlets() {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#log(java.lang.String)
   */
  public void log(String arg0) {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#log(java.lang.Exception, java.lang.String)
   */
  public void log(Exception arg0, String arg1) {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#log(java.lang.String, java.lang.Throwable)
   */
  public void log(String arg0, Throwable arg1) {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
   */
  public void removeAttribute(String arg0) {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#setAttribute(java.lang.String, java.lang.Object)
   */
  public void setAttribute(String arg0, Object arg1) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Dynamic addFilter(String arg0, Filter arg1) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Dynamic addFilter(String arg0, String arg1) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addListener(Class<? extends EventListener> arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addListener(String arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T extends EventListener> void addListener(T arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
      Class<? extends Servlet> arg1) {
    throw new UnsupportedOperationException();
  }

  @Override
  public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
      Servlet arg1) {
    throw new UnsupportedOperationException();
  }

  @Override
  public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
      String arg1) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T extends Filter> T createFilter(Class<T> arg0)
      throws ServletException {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T extends EventListener> T createListener(Class<T> arg0)
      throws ServletException {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T extends Servlet> T createServlet(Class<T> arg0)
      throws ServletException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void declareRoles(String... arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ClassLoader getClassLoader() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getContextPath() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getEffectiveMajorVersion() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getEffectiveMinorVersion() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
    throw new UnsupportedOperationException();
  }

  @Override
  public FilterRegistration getFilterRegistration(String arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
    throw new UnsupportedOperationException();
  }

  @Override
  public JspConfigDescriptor getJspConfigDescriptor() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ServletRegistration getServletRegistration(String arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Map<String, ? extends ServletRegistration> getServletRegistrations() {
    throw new UnsupportedOperationException();
  }

  @Override
  public SessionCookieConfig getSessionCookieConfig() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean setInitParameter(String arg0, String arg1) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setSessionTrackingModes(Set<SessionTrackingMode> arg0)
      throws IllegalStateException, IllegalArgumentException {
    throw new UnsupportedOperationException();
  }

}
