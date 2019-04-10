# Teaching-HEIGVD-RES-2019-Labo-SMTP
This project is a lab from academic course "RES" in HEIG-VD. 
The goal is to send semi-random pranks by mails to configured address. 
To do so, we implement our own SMTP Client.

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
## A description of your implementation

##
## References

* [MockMock server](<https://github.com/tweakers/MockMock>) on GitHub
* The [mailtrap](<https://mailtrap.io/>) online service for testing SMTP
* The [SMTP RFC](<https://tools.ietf.org/html/rfc5321#appendix-D>), and in particular the [example scenario](<https://tools.ietf.org/html/rfc5321#appendix-D>)
* Testing SMTP with TLS: `openssl s_client -connect smtp.mailtrap.io:2525 -starttls smtp -crl`

