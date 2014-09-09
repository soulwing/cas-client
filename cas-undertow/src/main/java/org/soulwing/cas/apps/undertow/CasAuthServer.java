package org.soulwing.cas.apps.undertow;

import io.undertow.Undertow;
import io.undertow.io.IoCallback;
import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.AuthenticationMode;
import io.undertow.security.api.SecurityContext;
import io.undertow.security.handlers.AuthenticationCallHandler;
import io.undertow.security.handlers.AuthenticationConstraintHandler;
import io.undertow.security.handlers.AuthenticationMechanismsHandler;
import io.undertow.security.handlers.SecurityInitialHandler;
import io.undertow.security.idm.IdentityManager;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.Collections;
import java.util.List;

public class CasAuthServer {

  public static void main(String[] args) throws Exception {
    final CasConfiguration casConfig = new CasConfigurationBean();
    final IdentityManager identityManager = new CasIdentityManager(casConfig);
    Undertow server =
        Undertow.builder().addHttpListener(8080, "localhost")
            .setHandler(addSecurity(new HttpHandler() {
              @Override
              public void handleRequest(final HttpServerExchange exchange)
                  throws Exception {
                final SecurityContext context = exchange.getSecurityContext();
                exchange.getResponseSender().send(
                    "Hello "
                        + context.getAuthenticatedAccount().getPrincipal()
                            .getName(), IoCallback.END_EXCHANGE);
              }
            }, identityManager, casConfig)).build();
    server.start();

  }

  private static HttpHandler addSecurity(final HttpHandler toWrap,
      final IdentityManager identityManager, final CasConfiguration casConfig) {
    HttpHandler handler = toWrap;
    handler = new AuthenticationCallHandler(handler);
    handler = new AuthenticationConstraintHandler(handler);
    final List<AuthenticationMechanism> mechanisms =
        Collections.singletonList((AuthenticationMechanism) 
            new CasAuthenticationMechanism(casConfig));
    handler = new AuthenticationMechanismsHandler(handler, mechanisms);
    handler =
        new SecurityInitialHandler(AuthenticationMode.PRO_ACTIVE,
            identityManager, handler);
    return handler;
  }

}
