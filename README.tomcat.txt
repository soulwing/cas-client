Extract Authenticators.properties:

cd $CATALINA_HOME/server/classes
jar -xf ../lib/catalina.jar org/apache/catalina/startup/Authenticators.properties


Add to Authenticators.properties:

CAS=org.soulwing.cas.apps.tomcat.CasAuthenticator


Install CAS client into CATALINA_HOME/server/lib using Ant in base of
soulwing-casclient project:

ant install-tomcat-ext -Dcatalina.home=$CATALINA_HOME


Log message when CAS authenticator is not configured in
Authenticators.properties:

2007-09-19 08:23:01,445 ERROR org.apache.catalina.startup.ContextConfig - 
Cannot configure an authenticator for method CAS


Global naming resources configuration needed for placing PGT into session:

<GlobalNamingResources>
  <Resource 
      name="ProxyGrantingTicketRegistry" auth="Container"
      type="org.soulwing.cas.apps.tomcat.ProxyGrantingTicketRegistry"
      factory="org.soulwing.cas.apps.tomcat.ProxyGrantingTicketRegistryFactory"
      description="CAS proxy granting ticket registry" />
</GlobalNamingResources>


Container configuration for CAS authentication with access to PGT in session:
* A Valve listens for PGT callback and adds PGT to registry
* The CAS Realm obtains a reference to the registry and makes it available to
  the CAS Authenticator.
* The CAS Authenticator registers an authenticated session so that it can be
  updated when the PGT is received.

<Engine name="Catalina" defaultHost="localhost" debug="0">
  <Host name="localhost" debug="0" appBase="webapps" 
        unpackWARs="true" autoDeploy="true"/>
  <Valve className="org.soulwing.cas.apps.tomcat.ProxyCallbackValve"
         proxyCallbackUri="/casProxyCallback"
         ticketRegistryResourceName="ProxyGrantingTicketRegistry" />
  <Realm className="org.soulwing.cas.apps.tomcat.CasUserDatabaseRealm"
         serverUrl="https://localhost:8087/cas"
         strategy="org.soulwing.cas.apps.tomcat.ProxyValidationStrategy" 
         proxyCallbackUrl="https://localhost:8089/casProxyCallback"
         resourceName="UserDatabase"
         ticketRegistryResourceName="ProxyGrantingTicketRegistry" />
</Engine>



