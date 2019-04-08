package ch.heigvd.res.mail.model.prank;

import ch.heigvd.res.mail.model.mail.Group;
import ch.heigvd.res.mail.model.mail.Mail;
import ch.heigvd.res.mail.model.mail.Person;
import ch.heigvd.res.mail.smtp.SMTPClient;

import java.util.ArrayList;

public class PrankGenerator {
    public static void main(String[] args) {
        SMTPClient client = new SMTPClient();
        Person s1 = new Person("monsieurprank@heig-vd.ch");
        Person s2 = new Person("monsieurprank2@heig-vd.ch");

        Person p1 = new Person("florian.polier@heig-vd.ch");
        Person p2 = new Person("polier.florian@gmail.com");
        Person p3 = new Person("mercierjordan@hotmail.com");
        Person p4 = new Person("test@gmail.com");

        ArrayList<Person> al1 = new ArrayList<>();
        al1.add(p1);
        al1.add(p2);

        ArrayList<Person> al2 = new ArrayList<>();
        al2.add(p3);
        al2.add(p4);


        try {
            ArrayList<Group> groups = new ArrayList<>();

            Group g1 = new Group(s1, al1);
            Group g2 = new Group(s2, al2);
            groups.add(g1);
            groups.add(g2);
            Prank p = new Prank(groups, "AHAHAH", "You got pranked", "Batman");
            ArrayList<Mail> result = Mail.generateMail(p);
            for (Mail m:
                 result) {
                System.out.println(m);
                client.sendMail(m);
            }

        } catch (
                Exception e) {
            System.out.println("erreur");
        }


    }

}
