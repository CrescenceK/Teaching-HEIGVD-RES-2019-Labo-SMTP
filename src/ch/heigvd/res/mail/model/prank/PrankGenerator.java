package ch.heigvd.res.mail.model.prank;

import ch.heigvd.res.mail.config.ConfigurationManager;
import ch.heigvd.res.mail.config.IConfigurationManager;
import ch.heigvd.res.mail.model.mail.*;
import ch.heigvd.res.mail.smtp.SMTPClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class PrankGenerator {
    private IConfigurationManager configuration;
    private ArrayList<Person> persons;

    public PrankGenerator(IConfigurationManager configuration) {
        this.configuration = configuration;
    }

    private ArrayList<Group> formedGroups(ArrayList<Person> victims, int numberOfGroup) throws Exception {

        ArrayList<Group> groups = new ArrayList<>();
        int nbPersPerGroup = victims.size() / numberOfGroup;

        if ((victims.size() / numberOfGroup) < 3) {
            throw new Exception("A group must contain at least 3 persons");

        } else {
            //brewing of the victims, so as not to have the same ones in a group each time.
            Collections.shuffle(victims);

            //group construction
            for (int i = 0; i < numberOfGroup; ++i) {
                Group group = new Group();
                int cnt = 0;

                while (cnt < nbPersPerGroup) {
                    group.addMember(victims.get(i + cnt));
                    ++cnt;
                }

                //help to choose a random sender in a group
                int randomIndex = (int) ((Math.random() * (nbPersPerGroup + 1))) % nbPersPerGroup;

                //the sender is remove from the list of receivers
                group.setSender(group.getMembers().get(randomIndex));
                group.getMembers().remove(randomIndex);
                groups.add(group);
            }
        }
        return groups;
    }

    public ArrayList<Mail> generateMails(Prank prank, ArrayList<Group> groups) {
        ArrayList<Mail> allMails = new ArrayList<>();


        for (Group group : groups) {
            for (Person p : group.getMembers()) {
                allMails.add(new Mail(group.getSender().getMailAdress(), p.getMailAdress(), p.getMailAdress(),
                        prank.getPrankSubject(), prank.getPrankContent()));
            }
        }
        return allMails;
    }


    public static void main(String[] args) {

        //helpful data to genrate pranks
        IConfigurationManager confManager = null;
        try {
            confManager = new ConfigurationManager("Configuration");

            // auth
            boolean auth = confManager.getAuth().equals("true");

            //pranks
            ArrayList<Prank> pranks = confManager.getPranks();
            PrankGenerator pg = new PrankGenerator(confManager);
            try {
                ArrayList<Group> groups = pg.formedGroups(confManager.getVictims(), confManager.getNumberOfGroups());

                Collections.shuffle(pranks);
                Collections.shuffle(groups);

                int randomIndex = (int) ((Math.random() * (pranks.size() + 1))) % pranks.size();

                //choose a prank randomly
                Prank p = pranks.get(randomIndex);

                //mails
                ArrayList<Mail> mails = pg.generateMails(p, groups);

                //SMTP customer
                SMTPClient client = new SMTPClient(confManager.getAddressOfServer(), confManager.getPortSmtp(),
                        confManager.getUsername(), confManager.getPassword(), auth);

                //send mails
                for (Mail mail : mails) {
                    // Timeout de 1 seconde pour éviter les erreurs
                    Thread.sleep(confManager.getTimeout() * 1000);
                    client.sendMail(mail);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}