package ch.heigvd.res.mail.smtp;

import ch.heigvd.res.mail.model.mail.Mail;

public interface ISMTPClient {
    void sendMail(Mail m);
}
