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
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;


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
  public Enumeration getAttributeNames() {
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
  public Enumeration getInitParameterNames() {
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
  public Enumeration getServletNames() {
    throw new UnsupportedOperationException();
  }

  /* 
   * @see javax.servlet.ServletContext#getServlets()
   */
  public Enumeration getServlets() {
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

}
