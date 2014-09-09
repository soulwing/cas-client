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

import io.undertow.security.idm.Account;
import io.undertow.security.idm.Credential;
import io.undertow.security.idm.IdentityManager;

import org.soulwing.cas.client.NoTicketException;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.client.SimpleValidationRequest;
import org.soulwing.cas.client.Validator;

/**
 * An {@link IdentityManager} that supports CAS authentication.
 *
 * @author Carl Harris
 */
public class CasIdentityManager implements IdentityManager {

  private final Validator validator;
  
  public CasIdentityManager(Validator validator) {
    this.validator = validator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Account verify(Account account) {
    return account;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Account verify(Credential credential) {
    if (!(credential instanceof CasTicketCredential)) {
      throw new IllegalArgumentException();
    }

    String ticket = ((CasTicketCredential) credential).getTicket();
    try {
      ServiceValidationResponse response = 
          validator.serviceValidate(new SimpleValidationRequest(ticket));
      return response.isSuccessful() ? 
          new CasAccount(new CasPrincipal(response.getUserName())) : null;
    }
    catch (NoTicketException ex) {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Account verify(String user, Credential credential) {
    throw new UnsupportedOperationException();
  }

}
