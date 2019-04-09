package config;

import ch.heigvd.res.mail.model.mail.*;


import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class ConfigurationManager implements config.IConfigurationManager {
    private int portSmtp;
    private int numberOfgroup;
    private String subject;
    private String adressOfServer;
    private final ArrayList <Person> victims;
    private final ArrayList <Mail> mails ;

    public ConfigurationManager() throws IOException{
        victims = preparedAdresses("/Configuration/victims.utf8");
        mails   = preparedPranks("/Configurations/messages.utf8");
        properties("/Configuration/config.properties");
    }

    @Override
    public int getPortSmtp() {
        return portSmtp;
    }

    @Override
    public int getNumberOfGroups() {
        return numberOfgroup;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getAdressOfServer() {
        return adressOfServer;
    }

    @Override
    public ArrayList<Person> getVictims() {
        return victims;
    }

    @Override
    public ArrayList <Mail> getMails() {
        return mails;
    }

    @Override
    public void properties(String filename) throws IOException {
        FileInputStream file = new FileInputStream(filename);
        Properties prop = new Properties();
        prop.load(file);
        this.adressOfServer = prop.getProperty("smtpAdressOfServer");
        this.subject = prop.getProperty("subject");
        this.portSmtp = Integer.parseInt(prop.getProperty("smtpPortOfServer"));
        this.numberOfgroup = Integer.parseInt("numberOfGroup");
    }

    @Override
    public ArrayList<Person> preparedAdresses(String filename) throws IOException {
        ArrayList<Person> persons;

        try(FileInputStream file = new FileInputStream(filename)){
            InputStreamReader sReader = new InputStreamReader(file, "UTF-8");

            try(BufferedReader bReader = new BufferedReader(sReader)){
                persons = new ArrayList<>();
                String address = bReader.readLine();

                while(address != null){
                    persons.add(new Person(address));
                }
            }
        }
        return persons;
    }

    @Override
    public ArrayList<Mail> preparedPranks(String filename) throws IOException {
        ArrayList<Mail> mails;

        try(FileInputStream file = new FileInputStream(filename)){
            InputStreamReader sReader = new InputStreamReader(file, "UTF-8");

            try(BufferedReader bReader = new BufferedReader(sReader)){
                mails = new ArrayList<>();
                String line = bReader.readLine();

                while(line != null){
                    StringBuilder message = new StringBuilder();
                    String sub = null;

                    while((line != null) && (!line.equals("=="))){
                        if(line.indexOf("subject") != -1){
                            sub = (line.split(":"))[1];
                        } else {
                            message.append(line);
                            message.append("\r\n");
                            line = bReader.readLine();
                        }
                    }
                    mails.add(new Mail(sub, message.toString()));
                    line = bReader.readLine();
                }
            }
        }
        return mails;
    }
}
