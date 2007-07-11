Instructions for Configuring Souling CAS Client as a project in Eclipse 3.2

1. Configure the SVN Repository Explorer with a New Repository Location:
	
		https://casclient.svn.sourceforge.net/svnroot/casclient
		
2. Drill down into this repository location.  Right-click on the trunk 
folder (or on a desired subfolder in the branches or tags folder), and
select Checkout...  Select the option to checkout as a new project
configured using the New Project Wizard.

3.  In the New Project Wizard:
3a.   Select Java Project as the new project wizard and click Next.  
3b.   In the Create Java Project dialog:
        Project Name:    soulwing-casclient
        JRE:             Select a JRE for Java 1.4.2 or Java 1.5
        Project layout:  Create separate source and output folders
3c.   In the Java Settings dialog:
        Allow output folders for source folders: checked
        Default output folder: soulwing-casclient/build/classes
        
4.  In the Package Explorer of the Java perspective, find the project named
"soulwing-casclient", right-click on the project name and choose properties.

5.  In the Properties dialog, select Java Build Path from the properties
selector.
5a.    Select the Source tab, and click Add Folder... select the "test"
       folder and click OK.  Select the Output folder option for the "test"
       source folder, click Edit, and set the output folder name to
       "build/test-classes".
5b.    Also in the Libraries tab, click Add JARs... select all of the JAR
       files in soulwing-casclient/vendor/lib and 
       soulwing-casclient/vendor/test-lib and click OK.

6.  If you are using a JDK other than version 1.4.2, in the Properties 
    dialog, select Java Compiler from the properties selector.
6a.    Click the checkbox to Enable Project Specific Settings.
6b.    In the JDK Compliance pane, set the Compiler compliance level to 1.4.


7.  In the properties selector (on the left), twirl down Java Compiler,
    and click Errors/Warnings.
7a.    Click the checkbox to Enable Project Specific Settings.
7b.    In the Error/Warnings dialog, twirl down the option for
       Deprecated/Restricted API and set the Deprecated API option to
   		 "ignore".  (This project implements mock versions of several
   		 interfaces in the Servlet API, and some of the methods of these
   		 interfaces are marked as deprecated).

8.  Click OK to close the Properties dialog and then click OK to confirm the 
    request to rebuild the project.
