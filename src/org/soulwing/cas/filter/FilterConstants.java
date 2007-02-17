/*
 * FilterConstants.java
 *
 * Created on Feb 6, 2007 
 */
package org.soulwing.cas.filter;

/**
 * Constants used in filters.
 *
 * @author Carl Harris
 */
public interface FilterConstants {

  String SERVER_URL = "serverUrl";
  String FILTER_PATH = "filterPath";
  String LOGOUT_PATH = "logoutPath";
  String DEFAULT_PATH = "defaultPath";
  String SERVICE_URL = "serviceUrl";
  String PROXY_CALLBACK_URL = "proxyCallbackUrl";
  String GATEWAY = "gateway";
  String RENEW = "renew";
  String AUTH_FAILED_URL = "authFailedUrl";
  String TRUSTED_PROXIES = "trustedProxies";
  String SOURCE_CLASS_NAME = "sourceClassName";

  String ATTRIBUTE_PREFIX = "org.soulwing.cas.";
  
  String SAVED_REQUEST_ATTRIBUTE = ATTRIBUTE_PREFIX + "savedRequest";
  
  String VALIDATION_ATTRIBUTE = ATTRIBUTE_PREFIX + "serviceValidation";
  
  String PROXY_GRANTING_TICKET_ATTRIBUTE = ATTRIBUTE_PREFIX 
      + "proxyGrantingTicket";

}
