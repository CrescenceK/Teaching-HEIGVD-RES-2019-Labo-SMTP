package ch.heigvd.res.mail.model.prank;

import ch.heigvd.res.mail.config.IConfigurationManager;
import ch.heigvd.res.mail.model.mail.Group;
import ch.heigvd.res.mail.model.mail.Mail;
import ch.heigvd.res.mail.model.mail.Person;
import ch.heigvd.res.mail.smtp.SMTPClient;

import java.io.IOException;
import java.util.*;

public class PrankGenerator {

    private IConfigurationManager configurations;
    private List<Person> victims = new LinkedList<>();


    public PrankGenerator(IConfigurationManager conf){
        this.configurations = conf;
    }

    private ArrayList formedGroups(ArrayList<Person> persons, int numberOfGroup) throws Exception {

        //brassage des victimes
        Collections.shuffle(persons);

        for (Person p : persons) {
            victims.add(p);
        }

        int nbrePersonMaxParGroup = (victims.size() % numberOfGroup) == 0 ? victims.size() / numberOfGroup : victims.size() / numberOfGroup + 1;

        ArrayList<Group> groups = new ArrayList<>();
        for (int i = 0; i < numberOfGroup - 1; ++i) {
            ArrayList<Person> members = new ArrayList<>();
            Person sender = null;
            int j = 0;
            for (Person v : victims) {
                while (j < nbrePersonMaxParGroup) {
                    members.add(v);
                    ++j;
                }
                int randomIndex = (int) ((Math.random() * (nbrePersonMaxParGroup + 1))) % nbrePersonMaxParGroup;
                sender = members.remove(randomIndex);
            }
            groups.add(new Group(sender, members));
        }
        return groups;
    }

    public ArrayList<Prank> generatePranks() throws Exception {

        int numberOfGroup       = configurations.getNumberOfGroups();
        int numberOfVictims     = configurations.getVictims().size();
        ArrayList<Prank> pranks = new ArrayList<>();
        ArrayList<Mail> mails   = configurations.getMails();

        ArrayList<Group> groups = formedGroups(configurations.getVictims(), numberOfGroup);
            for(Mail mail : mails){
                pranks.add(new Prank(groups, mail.getSubject(), mail.getText(), mail.getFrom()));
            }
        return pranks;
    }


     public void main(String[] args) {
        /*SMTPClient client = new SMTPClient();
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
        al2.add(p4);*/


        try {

           ArrayList<Group> groups = new ArrayList<>();

           ArrayList<Prank> pranks = this.generatePranks();

         /*   Group g1 = new Group(s1, al1);
            Group g2 = new Group(s2, al2);
            groups.add(g1);
            groups.add(g2);
            Prank p = new Prank(groups, "AHAHAH", "You got pranked", "Batman");
            ArrayList<Mail> result = Mail.generateMail(p);
            for (Mail m:
                 result) {
                System.out.println(m);
                client.sendMail(m); */
            } catch (Exception e1) {
            e1.printStackTrace();
        }

    }
}
