package ch.heigvd.res.mail.model.mail;

public class Person {
    public String getMailAdress() {
        return mailAdress;
    }

    private String mailAdress;
    public Person(String mailAdress){
        this.mailAdress = mailAdress;
    }
}
