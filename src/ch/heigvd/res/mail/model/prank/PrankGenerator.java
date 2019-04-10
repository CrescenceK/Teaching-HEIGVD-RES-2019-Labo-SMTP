package ch.heigvd.res.mail.model.prank;

import ch.heigvd.res.mail.config.ConfigurationManager;
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


     public void sendMails() {
        try {
            ArrayList<Prank> pranks = generatePranks();
            ArrayList<Mail> mails = new ArrayList<>();
            SMTPClient client = new SMTPClient("smtp.mailtrap.io", 25, "bb645f48e64664", "3db3a846ab9eac");
            for (Prank p:
                    pranks) {
                for (Group g:
                     p.getGroupsToPrank()) {
                    for (Person per:
                         g.getMembers()) {
                        mails.add(new Mail(g.getSender().getMailAdress(),per.getMailAdress(),p.getFakeSender(),per.getMailAdress(), p.getPrankSubject(), p.getPrankContent()));
                    }
                }
            }
            for (Mail m:
                 mails) {
                client.sendMail(m);
            }
        } catch (Exception e){
            System.out.println(e.getStackTrace());
        }
    }

    public static void main(String[] args) {
        try{
            Mail m = new Mail("florian.polier@heig-vd.ch","polier.florian@gmail.com","Batman", "polier.florian@gmail.com", "dsfdsf", "sdfdsf");
            SMTPClient client = new SMTPClient("smtp.mailtrap.io", 25, "bb645f48e64664", "3db3a846ab9eac");
            client.sendMail(m);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
