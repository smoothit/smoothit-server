=============================================================================
SmoothIT Information Server (SIS) v5.0 README
=============================================================================

RELEASE NOTES

The SmoothIT Information Server implements three (3) Economic Traffic
Management (ETM) mechanisms, namely:
- the BGP-based locality promotion mechanims (BGP-Loc),
- the ISP-owned Peer (IoP), and
- the Highly Active Peers (HAP).

More details on the specification of the ETM mechanisms and the design of the
SIS architecture can be found in the public deliverables of the FP7 SmoothIT
Project, located at http://www.smoothit.org/index.php?id=95.


PREREQUISITES

- Java 6 (JDK 1.6.0_10)
- JBoss 5.1 Application Server (use jboss-5.1.0.GA-jdk6.zip)
- Ant 1.7
- MySQL 5.1


INSTALLATION

Ensure that the root password of the MySQL server is set to "root" or change 
the configuration for accessing the database by changing the "mysql-ds.xml" 
file, under "/sis/resources".


ANT TASKS

"ant"
Builds and produces the binary version of the SIS under the "/sis/dist" 
directory.

"ant run-tests"
Same as ant but also performs the JUnit tests.

"ant clean"
Deletes the output of the previous build process.

"ant deploy –Djboss.dir=<path to JBoss deploy>"
Builds and produces the sis.war and deploys it to the application server. 
The path of the application server deploy folder must be declined after the 
argument –D. For example, if the application server is at C:\jboss-5.1.0.GA 
the command  should be "ant deploy –Djboss.dir=C:\jboss-5.1.0.GA".

"ant build-test-deploy –Djboss.dir=<path to JBoss deploy>"
Builds and produces the sis.war, performs the JUnit tests and deploys the 
sis.war to the application server. The path of the application server must be 
declined after the argument –D. For example, if the application server is at 
C:\jboss-5.1.0.GA the command should be 
"ant deploy –Djboss.dir=C:\jboss-5.1.0.GA".


DEPLOYMENT

1. Start up JBoss Application Server by running the following command under 
the directory "<path to Jboss>/bin": "start.bat -b 0.0.0.0".

2. Start MySQL

3. Run "ant deploy –Djboss.dir=<path to JBoss deploy>" under the directory 
"/sis".

4. Access the admin web interface at "http://localhost:8080/sis/" and use 
username "admin" and the password "admin" to log in.


BUGS/ISSUES

From the implementation of the HAP mechanism, the specific source that acts 
as the interface to the ISP's Billing system has been removed and a 
generic/abstract class has been placed (see eu.smoothit.sis.qos.BillingAdaptor).
To make the HAP mechanism functional, one needs to implement a client for the
specific Billing system.


LICENSE

The software is licensed under the Open Source GNU Lesser General Public 
License (LGPL) version 3. 


-----------------------------------------------------------------------------
For more details, visit FP7 project SmoothIT web site at:
http://www.smoothit.org









