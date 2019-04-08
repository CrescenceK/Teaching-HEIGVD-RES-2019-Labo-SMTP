package ch.heigvd.res.mail.model.Mail;
import java.util.ArrayList;
import ch.heigvd.res.mail.model.prank.Prank;

public class Mail {
    private String mail_from;
    private String rcpt_to;
    private String from;
    private String to;
    private String subject;
    private String text;

    static public ArrayList<Mail> generateMail(Prank p){
        ArrayList<Mail> allMails = new ArrayList<>();
        for (Group victimGroup:
             p.getGroupsToPrank()) {
            String sender = victimGroup.getSender().getMailAdress();
            for(Person victim :
            victimGroup.getMembers()) {
                Mail newMail = new Mail(sender, victim.getMailAdress(), p.getFakeSender(), victim.getMailAdress(), p.getPrankSubject(), p.getPrankContent());
                allMails.add(newMail);
            }
        }
        return allMails;
    }


    public Mail(String mail_from, String rcpt_to, String from, String to, String subject, String text) {
        this.mail_from = mail_from;
        this.rcpt_to = rcpt_to;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
    }



    public String toString(){
        String output = "";
        output+="Mail from: " + mail_from + "\n";
        output+="RCPT to: " + rcpt_to + "\n";
        output+="From: " + from + "\n";
        output+="To: " + to + "\n";
        output+="Subject:  " + subject + "\n";
        output+="Text:  " + text + "\n";

        return output;
    }
}
