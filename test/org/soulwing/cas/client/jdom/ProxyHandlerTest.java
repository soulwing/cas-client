/*
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
package org.soulwing.cas.client.jdom;

import java.io.StringReader;

import junit.framework.TestCase;

import org.soulwing.cas.client.ProxyResponse;
import org.soulwing.cas.client.Response;
import org.soulwing.cas.client.jdom.ProxyHandler;
import org.xml.sax.InputSource;

// TODO: add tests for invalid responses

public class ProxyHandlerTest extends TestCase {

  public void testProcessSuccessResult() throws Exception {
    ProxyHandler callback = new ProxyHandler();
    Response response = callback.processResult(constructSuccessResponse());
    assertTrue(response instanceof ProxyResponse);
    ProxyResponse proxyResponse = (ProxyResponse) response;
    assertTrue(proxyResponse.isSuccessful() == true);
    assertEquals("TEST TICKET", proxyResponse.getProxyTicket());
  }

  public void testProcessFailureResult() throws Exception {
    ProxyHandler callback = new ProxyHandler();
    Response response = callback.processResult(constructFailureResponse());
    assertTrue(response.isSuccessful() == false);
    assertEquals("TEST CODE", response.getResultCode());
    assertEquals("TEST MESSAGE", response.getResultMessage());
  }

  public InputSource constructFailureResponse() {
    StringBuilder sb = new StringBuilder();
    sb.append("<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>");
    sb.append("<cas:proxyFailure code='TEST CODE'>");
    sb.append("TEST MESSAGE");
    sb.append("</cas:proxyFailure>");
    sb.append("</cas:serviceResponse>");
    return new InputSource(new StringReader(sb.toString()));
  }

  public InputSource constructSuccessResponse() {
    StringBuilder sb = new StringBuilder();
    sb.append("<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>");
    sb.append("<cas:proxySuccess>");
    sb.append("<cas:proxyTicket>TEST TICKET</cas:proxyTicket>");
    sb.append("</cas:proxySuccess>");
    sb.append("</cas:serviceResponse>");
    return new InputSource(new StringReader(sb.toString()));
  }

}
