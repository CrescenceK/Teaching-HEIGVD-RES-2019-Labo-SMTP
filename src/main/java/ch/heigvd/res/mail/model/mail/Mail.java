package main.java.ch.heigvd.res.mail.model.mail;

public class Mail {
    private String mail_from;
    private String rcpt_to;
    private String from;
    private String to;
    private String subject;
    private String text;

    public Mail(String mail_from, String rcpt_to, String to, String subject, String text) {
        this.mail_from  = mail_from;
        this.rcpt_to    = rcpt_to;
        this.from       = mail_from;
        this.to         = to;
        this.subject    = subject;
        this.text       = text;
    }

    public String getMail_from() {
        return mail_from;
    }

    public String getRcpt_to() {
        return rcpt_to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String toString(){
        String output = "";
        output+="mail from: " + mail_from + "\n";
        output+="RCPT to: " + rcpt_to + "\n";
        output+="From: " + from + "\n";
        output+="To: " + to + "\n";
        output+="Subject:  " + subject + "\n";
        output+="Text:  " + text + "\n";

        return output;
    }
}