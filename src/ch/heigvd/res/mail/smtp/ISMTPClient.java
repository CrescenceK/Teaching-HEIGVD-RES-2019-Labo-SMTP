package ch.heigvd.res.mail.smtp;

import ch.heigvd.res.mail.model.mail.Mail;

import java.io.IOException;

public interface ISMTPClient {
    void sendMail(Mail m);
    Integer receiveFromServ() throws IOException;
    void sendToServer(String s) throws IOException;
}
