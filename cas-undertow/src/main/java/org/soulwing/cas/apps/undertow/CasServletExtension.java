/*
 * File created on June 11, 2014 
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

import io.undertow.security.idm.IdentityManager;
import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.DeploymentInfo;

import javax.servlet.ServletContext;

public class CasServletExtension implements ServletExtension {

  @Override
  public void handleDeployment(DeploymentInfo deploymentInfo, 
      ServletContext servletContext) {
    CasConfiguration config = new CasConfigurationBean();
    CasAuthenticationMechanism authnMechanism = new CasAuthenticationMechanism(config);
    IdentityManager identityManager = new CasIdentityManager(config); 
    deploymentInfo.clearLoginMethods();
    deploymentInfo.addFirstAuthenticationMechanism("CAS", authnMechanism);
    deploymentInfo.setIdentityManager(identityManager);
  }

}
