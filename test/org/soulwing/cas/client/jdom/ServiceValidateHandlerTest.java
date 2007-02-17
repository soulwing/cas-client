package org.soulwing.cas.client.jdom;

import java.io.StringReader;

import junit.framework.TestCase;

import org.soulwing.cas.client.Response;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.client.jdom.ServiceValidateHandler;
import org.xml.sax.InputSource;

//TODO: add tests for invalid responses and responses with optional
//elements omitted.

public class ServiceValidateHandlerTest extends TestCase {

  public void testProcessSuccessResult() throws Exception {
    ServiceValidateHandler callback = new ServiceValidateHandler();
    Response response = callback.processResult(constructSuccessResponse());
    assertTrue(response instanceof ServiceValidationResponse);
    ServiceValidationResponse svResponse = (ServiceValidationResponse) response;
    assertTrue(svResponse.isSuccessful() == true);
    assertEquals("TEST USER", svResponse.getUserName());
    assertEquals("TEST TICKET", svResponse.getProxyGrantingTicketIou());
  }

  public void testProcessFailureResult() throws Exception {
    ServiceValidateHandler callback = new ServiceValidateHandler();
    Response response = callback.processResult(constructFailureResponse());
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
    sb.append("</cas:authenticationSuccess>");
    sb.append("</cas:serviceResponse>");
    return new InputSource(new StringReader(sb.toString()));
  }

}
