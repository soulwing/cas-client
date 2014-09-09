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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.SimpleUrlGenerator;
import org.soulwing.cas.client.UrlGenerator;
import org.soulwing.cas.client.Validator;
import org.soulwing.cas.client.ValidatorFactory;

public class CasServletExtension implements ServletExtension {

  @Override
  public void handleDeployment(DeploymentInfo deploymentInfo, 
      ServletContext servletContext) {
    
    UrlGenerator generator = new SimpleUrlGenerator(protocolConfiguration());
    Validator validator = ValidatorFactory.getValidator(generator);
    
    CasAuthenticationMechanism authnMechanism = 
        new CasAuthenticationMechanism(generator);
    
    IdentityManager identityManager = new CasIdentityManager(validator); 
    deploymentInfo.clearLoginMethods();
    deploymentInfo.addFirstAuthenticationMechanism("CAS", authnMechanism);
    deploymentInfo.setIdentityManager(identityManager);
  }

  private ProtocolConfiguration protocolConfiguration() {
    try {
      PropertiesProtocolConfiguration config = 
          new PropertiesProtocolConfiguration();
      config.load(new URL("file:/tmp/cas.properties"));
      return config;
    }
    catch (MalformedURLException ex) {
      throw new IllegalArgumentException(ex);
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

}
