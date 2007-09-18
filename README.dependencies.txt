
Library Component Dependencies for the Soulwing CAS Client
----------------------------------------------------------

JAR files for all of the dependent libraries are in the "vendor" folder.

commons-logging.jar (run-time, required)
    Apache Commons logging.
 
jakarta-oro-2.0.8.jar (run-time, optional)
		Provides glob pattern matching support for the CAS bypass filter.
		If you're not using CAS bypass, you can omit this library.
		
jdom.jar (run-time, required)
		Used by the CAS client classes to interpret CAS responses.

junit-4.1.jar (build-time)
		Provide support for unit tests in "test" folder.
				
seraph-0.7.23.jar (build-time)
		Supporting classes needed to compile the application adapters for 
		Atlassian JIRA and Confluence

confluence-2.2.7.jar (build-time)
		Supporting classes needed to compile the application adapters for
		Atlassian Confluence
		
servlet-api.jar (build-time)
		Supporting classes for Servlet API version 2.4

spring-beans.jar, spring-core.jar, 
spring-context.jar, spring-web.jar (run-time, optional)
		Needed only if you are using FilterToBeanProxy to configure your
		filters as beans in a Spring application context. 
    If your application includes the entire spring.jar,  it includes the 
    classes in these jar files.
