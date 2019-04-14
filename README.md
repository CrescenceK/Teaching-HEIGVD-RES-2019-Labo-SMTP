# Teaching-HEIGVD-RES-2019-Labo-SMTP

Authors : Florian Polier && Crescence Yimnaing

Date : 10.04.2019

Brief : This project is a lab from academic course "RES" in HEIG-VD. 
The goal is to send semi-random pranks by mails to configured address. 
To do so, we implement our own SMTP Client.

----

## Instructions for setting up a mock SMTP server (with Docker)
In order to test our tool before sending real mails, you can easily set up a fake SMTP server.
Here are instructions to do so, with MockMock Server (see refs) on Docker :

### Prerequisite
- Docker
- Internet connexion
1) Get MockMock jar file (see ref)
2) Create your Docker image. Create a file named Dockerfile with this content: 
```
FROM openjdk:8-jre-alpine
# copy JAR into image
COPY MockMock.jar /MockMock.jar
# run application with this command line
CMD ["/usr/bin/java", "-jar", "/MockMock.jar", "-p", "yourSMTPPort", "-h", "yourHTTPPort"]
```
3) Build this image with the command `docker build --tag "repo:name" .`
4) Run a container based on this image with the command `docker run -p HTTPHostPort:HTTPContainerPort -p SMTPHostPort:SMTPContainerPort --name "MockMock" -d container_id`
Now, you should have access to your mockmock server via your browser on your IP address with correct port. Configure access info. in the config file and you're good to go!

## Clear and simple instructions for configuring your tool and running a prank campaign.

To run a prank campaign, you just have to :

first have to clone our project named ***Teaching-HEIGVD-RES-2019-Labo-SMTP***. Then set up Configuration files as you wish. We advise to use a mock server to test the programm first. You can also connect directly with another server, you just have to change the field ```smtpAddressOfServer``` int the file ```Configuration\config.properties.``` In the same file, you can also You can also choose the number of group, the username and the password(if you decided to use smtp.mailtrap.io). The  field ```timeout``` specify time to wait between two mails send. Otherwise mails will be considered as a spam. You can also set files of message and victims, to choose what you want to send, and to who.
Finally, just open the project with an IDE and run the ```Prankgenerator.java.```

## A description of your implementation

  We have separate our code into 2 principals package. ```mail``` and ```utils```.
  the mail package contains three packages:
  
  ```config```
  
- IConfigurationManager : is the interface of configurations that is implemented by ConfigurationManager.
- ConfigurationManage: The main rule of is to parse configuration files, so that we will obtain list of our victims, jokes and information about the server.
  
    
    ![](figures/DiagrammePackageconfig.PNG)


  
  ```model/mail```
  
- Group : a group of persons(sender and menbers).
- Mail  : mail by the sender, the recipients , Subject, text, etc...
- Person: he is defined by his address mail.

    ![](figures/DiagramPackageMail.PNG)

    
 ```model/prank```
 
- Prank: represents the body of a mail (suject, content).
- PrankGenerator.java: this class is used to form groups, generate mails, by reading the config files, create a SMTPClient, choose randomly a prank, call sendMail on client with each created mail.

    ![](figures/DiagramPackagePrank.PNG)
  
  ```smtp```
  
- ISMTPClient.java: is the interface of the client smtp that bind it to implement a "sendMail" method, this interface also contains SMTP commands.
- SMTPClient.java: represents the SMTP client which implements the interface ISmtpClient, this class allows to send emails.

    ![](figures/DiagramPackageSMTP.PNG)

##
## References

* [MockMock server](<https://github.com/tweakers/MockMock>) on GitHub
* The [mailtrap](<https://mailtrap.io/>) online service for testing SMTP
* The [SMTP RFC](<https://tools.ietf.org/html/rfc5321#appendix-D>), and in particular the [example scenario](<https://tools.ietf.org/html/rfc5321#appendix-D>)
* Testing SMTP with TLS: `openssl s_client -connect smtp.mailtrap.io:2525 -starttls smtp -crl`

