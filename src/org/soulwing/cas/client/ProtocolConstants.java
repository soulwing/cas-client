package org.soulwing.cas.client;

/**
 * Constants used in the CAS protocol.  Most of these are string element
 * names that appear in the CAS response document.
 *
 * @author Carl Harris
 *
 */
public interface ProtocolConstants {

  String SERVICE_RESPONSE = 
      "cas:serviceResponse";
  String AUTHENTICATION_SUCCESS = 
      "cas:authenticationSuccess";
  String AUTHENTICATION_FAILURE = 
      "cas:authenticationFailure";
  String PROXY_SUCCESS =
      "cas:proxySuccess";
  String PROXY_FAILURE =
      "cas:proxyFailure";
  String USER = 
      "cas:user";
  String PROXY_GRANTING_TICKET =
      "cas:proxyGrantingTicket";
  String PROXIES =
      "cas:proxies";
  String PROXY = 
      "cas:proxy";
  String PROXY_TICKET =
      "cas:proxyTicket";
  String CODE = "code";
  
  String LOGIN_URI = "login";
  String SERVICE_VALIDATE_URI = "serviceValidate";
  String PROXY_VALIDATE_URI = "proxyValidate";
  String PROXY_URI = "proxy";
  String LOGOUT_URI = "logout";
  String SERVICE_PARAM = "service";
  String TICKET_PARAM = "ticket";
  String PROXY_TICKET_PARAM = "pgtId";
  String PROXY_TICKET_IOU_PARAM = "pgtIou";
  String TARGET_SERVICE_PARAM = "targetService";
  String PGT_PARAM = "pgt";
  String URL_PARAM = "url";
  String PGT_URL_PARAM = "pgtUrl";
  String GATEWAY_PARAM = "gateway";
  String RENEW_PARAM = "renew";
  
}
