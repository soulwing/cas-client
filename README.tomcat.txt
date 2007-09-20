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


