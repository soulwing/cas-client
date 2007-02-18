/*
 * ProtocolConstants.java
 *
 * Created on Sep 6, 2006
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
package org.soulwing.cas.client;

/**
 * Constants used in the CAS protocol.
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
  
  String LOGIN_PATH = "/login";
  String SERVICE_VALIDATE_PATH = "/serviceValidate";
  String PROXY_VALIDATE_PATH = "/proxyValidate";
  String PROXY_PATH = "/proxy";
  String LOGOUT_PATH = "/logout";
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
