/*
 * File created on May 21, 2014 
 *
 * Copyright 2007-2014 Carl Harris, Jr.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.soulwing.cas.apps.undertow;

import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.SecurityContext;
import io.undertow.security.idm.Account;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.util.Deque;

import org.soulwing.cas.client.UrlGenerator;

/**
 * An {@link AuthenticationMechanism} that uses the CAS protocol.
 * 
 * @author Carl Harris
 */
public class CasAuthenticationMechanism implements AuthenticationMechanism {

  private final UrlGenerator generator;

  public CasAuthenticationMechanism(UrlGenerator generator) {
    this.generator = generator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AuthenticationMechanismOutcome authenticate(
      HttpServerExchange exchange, SecurityContext securityContext) {
    Deque<String> tickets = exchange.getQueryParameters().get("ticket");
    String ticket = tickets != null ? tickets.peekFirst() : null;
    if (ticket == null) {
      securityContext.setAuthenticationRequired();
      return AuthenticationMechanismOutcome.NOT_ATTEMPTED;
    }
    Account account = securityContext.getIdentityManager().verify(
        new CasTicketCredential(ticket));
    if (account != null) {
      securityContext.authenticationComplete(account, "CAS", true);
      return AuthenticationMechanismOutcome.AUTHENTICATED;
    }
    else {
      securityContext.setAuthenticationRequired();
      return AuthenticationMechanismOutcome.NOT_AUTHENTICATED;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChallengeResult sendChallenge(HttpServerExchange exchange,
      SecurityContext context) {
    exchange.getResponseHeaders().put(HttpString.tryFromString("Location"),
        generator.getLoginUrl());
    return new ChallengeResult(true, 302);
  }

}
