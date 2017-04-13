# ApiServer

HTTP ApiServer implemented with **ServerSocket** of Java using mutlti-threading 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for testing purposes.

### Prerequisites

### **Note : All installations are based on Windows Operating System**

It assumes the following are already installed on your system

* JDK 1.8+
* Maven 3.0+
* cURL 7.53+

### Verify installation 

Check whether the following are already installed on your system or not

* **Operating System architecture**
	1. Open command prompt on your OS as administrator by going to _Start -> Run_
	2. Enter `cmd.exe` and hit enter. The command prompt opens up
	3. Type `echo %PROCESSOR_ARCHITECTURE%` and hit enter
	4. This will return `x86` on **32-bit** systems and `AMD64 (or IA64) `on **64-bit** systems.

* **Java Installation**
	1. Open command prompt on your OS as administrator by going to _Start -> Run_
	2. Enter `cmd.exe` and hit enter. The command prompt opens up
	3. Type `java -version` and hit enter
	4. It should display the following output
	> **java version "1.8.0"**
	> Java(TM) SE Runtime Environment (build 1.8.0-b132)
	> Java HotSpot(TM) 64-Bit Server VM (build 25.0-b70, mixed mode)

* **Maven Installation**
	1. 1. Open command prompt on your OS as administrator by going to _Start -> Run_
	2. Enter `cmd.exe` and hit enter. The command prompt opens up
	3. Open command prompt on your OS as administrator
	4. Type `mvn -version` and hit enter
	5. It should display the following output
	> **Apache Maven 3.3.9 **
	> (bb52d8502b132ec0a5a3f4c09453c07478323dc5;20157+05:30)
Maven home: F:\Softwares\Software Tools\apache-maven-3.3.9
Java version: 1.8.0, vendor: Oracle Corporation
Java home: I:\Program Files\Java\jdk1.8.0\jre
Default locale: en_GB, platform encoding: Cp1252
OS name: "windows 8", version: "6.2", arch: "amd64", family: "dos"

* **cURLInstallation**
	1. Open command prompt on your OS as administrator
	2. Type `curl --version` and hit enter
	3. It should display the following output
	> **curl 7.46.0 (x86_64-pc-win32)** libcurl/7.46.0 OpenSSL/1.0.2e zlib/1.2.8 WinIDN libssh2/1.6.0
	> Protocols: dict file ftp ftps gopher http https imap imaps ldap pop3 pop3s rtsp scp sftp smtp smtps telnet tftp
Features: AsynchDNS IDN IPv6 Largefile SSPI Kerberos SPNEGO NTLM SSL libz

### Installing 

#### **Note : Install the following on your system in the order they are mentioned**

* **Java**

	1. Download the latest version of JDK from this [link](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) based on your OS architecture
	2. Install the downloaded package
	3. Note down the JDK installation directory. It should be like ` <Drive>:\Program Files\Java\jdk1.8.0 ` or ` <Drive>:\Program Files (x86)\Java\jdk1.8.0 ` based on your OS architecture
	4. Right click on _My Computer -> Properties -> Advanced System Settings_
	5. Click on _Advanced Tab -> Environment Variables_
	6. Make a new entry in System Variables with **Variable Name** as `JAVA_HOME` and **Variable Value** as your JDK installation directory based on your OS architecture
	7. Verify JDK installation

* **Maven**

	1. Download the latest distribution of Apache Maven from this [link](http://mirror.fibergrid.in/apache/maven/maven-3/3.5.0/binaries/apache-maven-3.5.0-bin.zip) 
	2. Unzip the downloaded package to a location such as  ` <Drive>:\Program Files\Apache\maven ` or ` <Drive>:\Program Files (x86)\Apache\maven ` based on your OS architecture or any other location of your choice. **Note down the path where you have unzipped your maven distribution**
	3. Right click on _My Computer -> Properties -> Advanced System Settings_
	4. Click on _Advanced Tab -> Environment Variables_
	5. Make a new entry in System Variables with **Variable Name** as `M2_HOME` and **Variable Value** as maven location on your system from the previous step
	6. Make a new entry in System Variables with **Variable Name** as `MAVEN_HOME` and **Variable Value** as maven location on your system from the previous step
	7. Verify Maven installation

* **cURL**

	1. Download the latest installer of cURL from this [link](http://www.confusedbycode.com/curl/) 
	2. Click on the downloaded installer and start installation by following the on screen steps in the instalaction wizard
	3. Once the installation is completed, verify cURL installation

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

## Deployment

 1. Download this project on your local system as a .zip package
 2. Unzip the package on to your local drive. **Remember the path to which you unzipped the project**
 3. Open command prompt and type `cd <path to downloaded package>`. Hit enter
 4. Type `<drive letter>:` and hit enter. You will now be in the directory where the package is present
 5. Type `cd ApiServer` to navigate inside the project
 6. Once you are inside the project, enter `mvn clean install` and hit enter. Maven will start gathering the dependencies, compile the project and build it into the target packaging. **Note : The first time you run the maven build it will take some time to download the required dependencies**. Once build is completed successfully, you will get a message  at the end of the console output of `mvn clean install`
 > [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 5.644 s
    [INFO] Finished at: 2017-04-13T23:01:20+05:30
	[INFO] Final Memory: 19M/89M
	[INFO] ------------------------------------------------------------------------
	
 7. Type `cd ..` at the command prompt to go back to the parent
    directory
 8. Type `cd target` to go into the directory where Maven has built the packaging of the project with the required dependencies into a jar file with the name **ApiServer-0.0.1-SNAPSHOT.jar**
 9.  Type `java -jar ApiServer-0.0.1-SNAPSHOT.jar <port number>` to start the API Server. The port number argument is optional. **If no argument is provided, the API Server will start on port 80 by default**
 
## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Anirban Das** - *Initial work* - [anirbandas18](https://github.com/anirbandas18)

## Acknowledgments

* Backend-End hiring assignment for Dipper
