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
import java.util.List;

import junit.framework.TestCase;

import org.soulwing.cas.client.ProxyValidationResponse;
import org.soulwing.cas.client.Response;
import org.xml.sax.InputSource;

// TODO: add tests for invalid responses and responses with optional
// elements omitted.

public class ProxyValidateMappingStrategyTest extends TestCase {

  private ProxyValidateMappingStrategy strategy;
  private JdomProtocolHandlerImpl handler;

  protected void setUp() throws Exception {
    strategy = new ProxyValidateMappingStrategy();
    handler = new JdomProtocolHandlerImpl();
  }

  public void testProcessSuccessResult() throws Exception {
    Response response = handler.processResult(
        constructSuccessResponse(), strategy);
    assertTrue(response instanceof ProxyValidationResponse);
    ProxyValidationResponse pvResponse = (ProxyValidationResponse) response;
    assertTrue(pvResponse.isSuccessful() == true);
    assertEquals("TEST USER", pvResponse.getUserName());
    assertEquals("TEST TICKET", pvResponse.getProxyGrantingTicketIou());
    List proxies = pvResponse.getProxies();
    assertNotNull(proxies);
    assertEquals(1, proxies.size());
    assertEquals("TEST PROXY", (String) proxies.get(0));
  }

  public void testProcessFailureResult() throws Exception {
    Response response = handler.processResult(
        constructFailureResponse(), strategy);
    assertTrue(response.isSuccessful() == false);
    assertEquals("TEST CODE", response.getResultCode());
    assertEquals("TEST MESSAGE", response.getResultMessage());
  }

  public InputSource constructFailureResponse() {
    StringBuilder sb = new StringBuilder();
    sb.append("<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>");
    sb.append("<cas:authenticationFailure code='TEST CODE'>");
    sb.append("TEST MESSAGE");
    sb.append("</cas:authenticationFailure>");
    sb.append("</cas:serviceResponse>");
    return new InputSource(new StringReader(sb.toString()));
  }

  public InputSource constructSuccessResponse() {
    StringBuilder sb = new StringBuilder();
    sb.append("<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>");
    sb.append("<cas:authenticationSuccess>");
    sb.append("<cas:user>TEST USER</cas:user>");
    sb.append("<cas:proxyGrantingTicket>TEST TICKET</cas:proxyGrantingTicket>");
    sb.append("<cas:proxies>");
    sb.append("<cas:proxy>TEST PROXY</cas:proxy>");
    sb.append("</cas:proxies>");
    sb.append("</cas:authenticationSuccess>");
    sb.append("</cas:serviceResponse>");
    return new InputSource(new StringReader(sb.toString()));
  }

}
