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
