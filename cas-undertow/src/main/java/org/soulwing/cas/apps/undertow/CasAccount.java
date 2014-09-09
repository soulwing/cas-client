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

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

/**
 * An {@link Account} based on a {@link CasPrincipal}.
 *
 * @author Carl Harris
 */
public class CasAccount implements Account {

  private final CasPrincipal principal;  
  
  public CasAccount(CasPrincipal principal) {
    this.principal = principal;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Principal getPrincipal() {
    return principal;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> getRoles() {
    return Collections.emptySet();
  }

}
