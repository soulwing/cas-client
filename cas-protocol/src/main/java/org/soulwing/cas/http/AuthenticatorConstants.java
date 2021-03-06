/*
 * FilterConstants.java
 *
 * Created on Feb 6, 2007 
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
package org.soulwing.cas.http;

/**
 * Constants used in filters.
 *
 * @author Carl Harris
 */
public interface AuthenticatorConstants {

  String SERVER_URL = "casServerUrl";
  String SERVICE_URL = "casServiceUrl";
  String PROXY_CALLBACK_URL = "casProxyCallbackUrl";
  String GATEWAY = "casGateway";
  String RENEW = "casRenew";
  String FILTER_PATH = "filterPath";
  String REDIRECT_TO_LOGIN = "redirectToLogin";
  String AUTH_FAILED_URL = "authFailedUrl";
  String TRUSTED_PROXIES = "trustedProxies";
  String SOURCE_CLASS_NAME = "sourceClassName";
  String AUTHORIZED_USERS = "authorizedUsers";
  String LOGOUT_PATH = "logoutPath";
  String BYPASS_PATHS = "bypassPaths";
  String APPLICATION_LOGOUT = "applicationLogout";
  String GLOBAL_LOGOUT = "globalLogout";
  String REDIRECT_URL = "redirectUrl";
  String BYPASS_REDIRECT_URL = "bypassRedirectUrl";
  String POST_VALIDATION_REDIRECT_PARAMETER = "postValidationRedirectParameter";
  
  String ATTRIBUTE_PREFIX = "org.soulwing.cas.";
  
  String PROTOCOL_CONFIGURATION_ATTRIBUTE = ATTRIBUTE_PREFIX
      + "protocolConfiguration";
  
  String VALIDATION_ATTRIBUTE = ATTRIBUTE_PREFIX + "serviceValidation";
  
  String PROXY_GRANTING_TICKET_ATTRIBUTE = ATTRIBUTE_PREFIX 
      + "proxyGrantingTicket";

  String BYPASS_ATTRIBUTE = ATTRIBUTE_PREFIX + "bypass";
  
  String LOGOUT_ATTRIBUTE = ATTRIBUTE_PREFIX + "logout";

  String LOGIN_ATTRIBUTE = ATTRIBUTE_PREFIX + "login";

}
