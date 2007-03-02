/*
 * MockHttpServletResponse.java
 *
 * Created on Sep 12, 2006
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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


/**
 * A mock up of HttpServletResponse.
 *
 * @author Carl Harris
 * 
 */
public class MockHttpServletResponse implements HttpServletResponse {

  private int statusCode;
  private String message;
  private String redirectUrl;
  private boolean committed;
  
  public void addCookie(Cookie arg0) {
    throw new UnsupportedOperationException();
  }

  public boolean containsHeader(String arg0) {
    throw new UnsupportedOperationException();
  }

  public String encodeURL(String arg0) {
    throw new UnsupportedOperationException();
  }

  public String encodeRedirectURL(String arg0) {
    throw new UnsupportedOperationException();
  }

  public String encodeUrl(String arg0) {
    throw new UnsupportedOperationException();
  }

  public String encodeRedirectUrl(String arg0) {
    throw new UnsupportedOperationException();
  }

  public void sendError(int statusCode, String message) throws IOException {
    setStatus(statusCode, message);
  }

  public void sendError(int statusCode) throws IOException {
    setStatus(statusCode);
  }

  public String getRedirect() {
    return redirectUrl;
  }
  
  public void sendRedirect(String redirectUrl) throws IOException {
    this.redirectUrl = redirectUrl;
    this.committed = true;
  }

  public boolean isCommitted() {
    return committed;
  }

  public void setDateHeader(String arg0, long arg1) {
    throw new UnsupportedOperationException();
  }

  public void addDateHeader(String arg0, long arg1) {
    throw new UnsupportedOperationException();
  }

  public void setHeader(String arg0, String arg1) {
    throw new UnsupportedOperationException();
  }

  public void addHeader(String arg0, String arg1) {
    throw new UnsupportedOperationException();
  }

  public void setIntHeader(String arg0, int arg1) {
    throw new UnsupportedOperationException();
  }

  public void addIntHeader(String arg0, int arg1) {
    throw new UnsupportedOperationException();
  }

  public int getStatus() {
    return statusCode;
  }
  
  public String getMessage() {
    return message;
  }
  
  public void setStatus(int statusCode) {
    this.statusCode = statusCode;
    this.committed = true;
  }

  public void setStatus(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
    this.committed = true;
  }

  public String getCharacterEncoding() {
    return "UTF-8";
  }

  public String getContentType() {
    throw new UnsupportedOperationException();
  }

  public ServletOutputStream getOutputStream() throws IOException {
    throw new UnsupportedOperationException();
  }

  public PrintWriter getWriter() throws IOException {
    throw new UnsupportedOperationException();
  }

  public void setCharacterEncoding(String arg0) {
    throw new UnsupportedOperationException();
  }

  public void setContentLength(int arg0) {
    throw new UnsupportedOperationException();
  }

  public void setContentType(String arg0) {
    throw new UnsupportedOperationException();
  }

  public void setBufferSize(int arg0) {
    throw new UnsupportedOperationException();
  }

  public int getBufferSize() {
    throw new UnsupportedOperationException();
  }

  public void flushBuffer() throws IOException {
    throw new UnsupportedOperationException();
  }

  public void resetBuffer() {
    throw new UnsupportedOperationException();
  }

  public void reset() {
    throw new UnsupportedOperationException();
  }

  public void setLocale(Locale arg0) {
    throw new UnsupportedOperationException();
  }

  public Locale getLocale() {
    throw new UnsupportedOperationException();
  }

}
