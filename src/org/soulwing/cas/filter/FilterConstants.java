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
  String SERVICE_URL = "serviceUrl";
  String PROXY_CALLBACK_URL = "proxyCallbackUrl";
  String GATEWAY = "gateway";
  String RENEW = "renew";
  String AUTH_FAILED_URL = "authFailedUrl";
  String TRUSTED_PROXIES = "trustedProxies";
  String SOURCE_CLASS_NAME = "sourceClassName";

  String ATTRIBUTE_PREFIX = "org.soulwing.cas.";
  
  String VALIDATION_ATTRIBUTE = ATTRIBUTE_PREFIX + "serviceValidation";
  
  String PROXY_GRANTING_TICKET_ATTRIBUTE = ATTRIBUTE_PREFIX 
      + "proxyGrantingTicket";

  String BYPASS_ATTRIBUTE = ATTRIBUTE_PREFIX + "bypass";
  public static final String AUTHORIZED_USERS_PARAM = "authorizedUsers";

}
