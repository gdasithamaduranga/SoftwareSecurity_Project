## Software Security - OAuth 2.0 application

Google Drive File upload application using Open Autherization 2.0 Framework.

### Steps to run

There will be two methods to run the application

**** Method 1 ****
1. Extract project into your machine.
2. Go to "build_artifact" folder
3. Open "application.properties" file.
4. Change the path of "google.credentials.folder.path"(credential folder path) and "myapp.temp.path" (tempfolder path) according to your                         	download location.
5. Run "software-sec-oauth-0.0.1-SNAPSHOT.jar" using command prompt as below,

   open cmd> Go to "build_artifact" folder location in CMD.
   
   Run following command
   
   java -jar software-sec-oauth-0.0.1-SNAPSHOT.jar -spring.config.location=application.prpoerties
6. Application is accessible at localhost:8080
   
**** Method 2 ****   
   
1. Download and install mvn. (In windows set environment variable as well, If it is success when run "mvn - version " in cmd you can see the mvn version) 
2. Build using "mvn clean install" (Go inside project folder in CMD, Then run "mvn clean install")
2. Run "software-sec-oauth-0.0.1-SNAPSHOT.jar" file under /target (Go inside to the "target" folder in CMD, Then run "java -jar software-sec-oauth-0.0.1-SNAPSHOT.jar")
3. Application is accessible at localhost:8080
