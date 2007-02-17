/*
 * MockHttpSession.java
 *
 * Created on Sep 12, 2006
 */
package org.soulwing.servlet.http;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;


/**
 * A mock up of HttpSession.
 *
 * @author Carl Harris
 * 
 */
public class MockHttpSession implements HttpSession {

  private String id;
  private long creationTime = System.currentTimeMillis();
  private ServletContext servletContext;
  private int maxInactiveInterval;
  private Map attributes = new HashMap();
  
  public long getCreationTime() {
    return this.creationTime;
  }

  public String getId() {
    return this.id;
  }

  public long getLastAccessedTime() {
    return getCreationTime();
  }

  public ServletContext getServletContext() {
    return this.servletContext;
  }

  public void setMaxInactiveInterval(int maxInactiveInterval) {
    this.maxInactiveInterval = maxInactiveInterval;
  }

  public int getMaxInactiveInterval() {
    return this.maxInactiveInterval;
  }

  public HttpSessionContext getSessionContext() {
    throw new UnsupportedOperationException();
  }

  public Object getAttribute(String name) {
    return attributes.get(name);
  }

  public Object getValue(String name) {
    throw new UnsupportedOperationException();
  }

  public Enumeration getAttributeNames() {
    return new Vector(attributes.keySet()).elements();
  }

  public String[] getValueNames() {
    throw new UnsupportedOperationException();
  }

  public void setAttribute(String name, Object value) {
    attributes.put(name, value);
  }

  public void putValue(String arg0, Object arg1) {
    throw new UnsupportedOperationException();
  }

  public void removeAttribute(String name) {
    attributes.remove(name);
  }

  public void removeValue(String arg0) {
    throw new UnsupportedOperationException();
  }

  public void invalidate() {
    this.attributes = new HashMap();
  }

  public boolean isNew() {
    throw new UnsupportedOperationException();
  }

}
