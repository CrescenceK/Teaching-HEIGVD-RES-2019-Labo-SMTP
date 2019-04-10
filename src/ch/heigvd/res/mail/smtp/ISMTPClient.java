package ch.heigvd.res.mail.smtp;

import ch.heigvd.res.mail.model.mail.Mail;

import java.io.IOException;

public interface ISMTPClient {
    // The responsability to send an email via SMTP
    void sendMail(Mail m);

    // Get a line of data from a connected SMTP Server
    Integer receiveFromServ() throws IOException;

    // Send a command to a connected SMTP Server. It ensures only valid commands are sent via ENUM.
    void sendToServer(SMTPClient.SMTPCommands command, String data) throws IOException;

    // Should be able to authenticate to a SMTP Server.
    boolean authenticate(SMTPClient.AuthMethods authMethods);

}

