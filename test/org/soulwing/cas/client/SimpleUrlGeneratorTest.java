package org.soulwing.cas.client;


import org.soulwing.cas.client.SimpleUrlGenerator.URLEncoder;

import junit.framework.TestCase;


public class SimpleUrlGeneratorTest extends TestCase {


  private static final String SERVER_URL = "https://localhost/cas";
  private static final String SERVICE_URL = "https://localhost/myapp";
  private static final String LOGOUT_URL = "https://localhost/myapp/logout";
  private static final String TARGET_SERVICE = "https://localhost/target";
  private static final String PGT = "proxy_granting_ticket";
  private static final String PGT_URL = "https://localhost/pgtCallback";
  private static final String TICKET = "validation_ticket";

  private ProtocolConfiguration config;
  private SimpleUrlGenerator generator;
  
  protected void setUp() throws Exception {
    config = new ProtocolConfiguration();
    config.setServerUrl(SERVER_URL);
    config.setServiceUrl(SERVICE_URL);
    generator = new SimpleUrlGenerator(config);
  }

  public void testGetLoginUrl() {
    assertEquals(SERVER_URL + "/login?service=" 
        + URLEncoder.encode(SERVICE_URL),
        generator.getLoginUrl());
  }

  public void testGetLoginUrlWithGateway() {
    config.setGatewayFlag(true);
    assertEquals(SERVER_URL + "/login?service=" 
        + URLEncoder.encode(SERVICE_URL) + "&gateway=true",
        generator.getLoginUrl());
  }

  public void testGetLoginUrlWithRenew() {
    config.setRenewFlag(true);
    assertEquals(SERVER_URL + "/login?service=" 
        + URLEncoder.encode(SERVICE_URL) + "&renew=true",
        generator.getLoginUrl());
  }

  public void testGetLoginUrlWithGatewayAndRenew() {
    config.setGatewayFlag(true);
    config.setRenewFlag(true);
    assertEquals(SERVER_URL + "/login?service=" 
        + URLEncoder.encode(SERVICE_URL) + "&gateway=true&renew=true",
        generator.getLoginUrl());
  }

  public void testGetLogoutUrl() {
    assertEquals(SERVER_URL + "/logout", generator.getLogoutUrl());
  }

  public void testGetLogoutUrlWithUrl() {
    assertEquals(SERVER_URL + "/logout?url=" + URLEncoder.encode(LOGOUT_URL), 
        generator.getLogoutUrl(LOGOUT_URL));
  }

  public void testGetProxyUrl() {
    assertEquals(SERVER_URL + "/proxy?pgt=" + PGT 
        + "&targetService=" + URLEncoder.encode(TARGET_SERVICE), 
        generator.getProxyUrl(PGT, TARGET_SERVICE));
  }

  public void testGetServiceValidateUrl() {
    assertEquals(SERVER_URL + "/serviceValidate?service="
      + URLEncoder.encode(SERVICE_URL)
      + "&ticket=" + TICKET, generator.getServiceValidateUrl(TICKET));
  }

  public void testGetServiceValidateUrlWithPgtUrl() {
    config.setProxyCallbackUrl(PGT_URL);
    assertEquals(SERVER_URL + "/serviceValidate?service="
      + URLEncoder.encode(SERVICE_URL)
      + "&ticket=" + TICKET + "&pgtUrl=" + URLEncoder.encode(PGT_URL), 
      generator.getServiceValidateUrl(TICKET));
  }
  
  public void testGetServiceValidateUrlWithGateway() {
    config.setGatewayFlag(true);
    assertEquals(SERVER_URL + "/serviceValidate?service="
      + URLEncoder.encode(SERVICE_URL)
      + "&ticket=" + TICKET + "&gateway=true", 
      generator.getServiceValidateUrl(TICKET));
  }
  
  public void testGetServiceValidateUrlWithRenew() {
    config.setRenewFlag(true);
    assertEquals(SERVER_URL + "/serviceValidate?service="
      + URLEncoder.encode(SERVICE_URL)
      + "&ticket=" + TICKET + "&renew=true", 
      generator.getServiceValidateUrl(TICKET));
  }
  
  public void testGetServiceValidateUrlWithPgtUrlGatewayAndRenew() {
    config.setProxyCallbackUrl(PGT_URL);
    config.setGatewayFlag(true);
    config.setRenewFlag(true);
    assertEquals(SERVER_URL + "/serviceValidate?service="
      + URLEncoder.encode(SERVICE_URL)
      + "&ticket=" + TICKET + "&pgtUrl=" + URLEncoder.encode(PGT_URL)
      + "&gateway=true&renew=true", 
      generator.getServiceValidateUrl(TICKET));
  }
  
  public void testGetProxyValidateUrl() {
    assertEquals(SERVER_URL + "/proxyValidate?service="
      + URLEncoder.encode(SERVICE_URL)
      + "&ticket=" + TICKET, generator.getProxyValidateUrl(TICKET));
  }

  public void testGetProxyValidateUrlWithPgtUrl() {
    config.setProxyCallbackUrl(PGT_URL);
    assertEquals(SERVER_URL + "/proxyValidate?service="
      + URLEncoder.encode(SERVICE_URL)
      + "&ticket=" + TICKET + "&pgtUrl=" + URLEncoder.encode(PGT_URL), 
      generator.getProxyValidateUrl(TICKET));
  }
  
  public void testGetProxyValidateUrlWithGateway() {
    config.setGatewayFlag(true);
    assertEquals(SERVER_URL + "/proxyValidate?service="
      + URLEncoder.encode(SERVICE_URL)
      + "&ticket=" + TICKET + "&gateway=true", 
      generator.getProxyValidateUrl(TICKET));
  }
  
  public void testGetProxyValidateUrlWithRenew() {
    config.setRenewFlag(true);
    assertEquals(SERVER_URL + "/proxyValidate?service="
      + URLEncoder.encode(SERVICE_URL)
      + "&ticket=" + TICKET + "&renew=true", 
      generator.getProxyValidateUrl(TICKET));
  }

  public void testGetProxyValidateUrlWithPgtUrlGatewayAndRenew() {
    config.setProxyCallbackUrl(PGT_URL);
    config.setGatewayFlag(true);
    config.setRenewFlag(true);
    assertEquals(SERVER_URL + "/proxyValidate?service="
      + URLEncoder.encode(SERVICE_URL)
      + "&ticket=" + TICKET + "&pgtUrl=" + URLEncoder.encode(PGT_URL)
      + "&gateway=true&renew=true", 
      generator.getProxyValidateUrl(TICKET));
  }

  public void testGetProxyUrlNoPgt() throws Exception {
    try {
      generator.getProxyUrl(null, TARGET_SERVICE);
      fail("Expected IllegalArgumentException");
    }
    catch (IllegalArgumentException ex) {
      assertTrue(true);
    }
  }

  public void testGetProxyUrlNoTargetService() throws Exception {
    try {
      generator.getProxyUrl(PGT_URL, null);
      fail("Expected IllegalArgumentException");
    }
    catch (IllegalArgumentException ex) {
      assertTrue(true);
    }
  }

  public void testGetServiceValidateUrlNoTicket() throws Exception {
    try {
      generator.getServiceValidateUrl(null);
      fail("Expected IllegalArgumentException");
    }
    catch (IllegalArgumentException ex) {
      assertTrue(true);
    }
  }

  public void testGetProxyValidateUrlNoTicket() throws Exception {
    try {
      generator.getProxyValidateUrl(null);
      fail("Expected IllegalArgumentException");
    }
    catch (IllegalArgumentException ex) {
      assertTrue(true);
    }
  }

}
