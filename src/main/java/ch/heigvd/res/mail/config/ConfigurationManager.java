package ch.heigvd.res.mail.config;

import ch.heigvd.res.mail.model.mail.*;
import ch.heigvd.res.mail.model.prank.Prank;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class ConfigurationManager implements IConfigurationManager {

    private int portSmtp;
    private int numberOfgroup;
    private String subject;
    private String addressOfServer;
    private String username;
    private String password;
    private String auth;
    private int timeout;
    private final ArrayList <Person> victims;
    private final  ArrayList <Prank> pranks;

    public ConfigurationManager(String configFolder) throws IOException{
        victims = preparedAdresses(configFolder+"/victims.utf8");
        pranks  = preparedPranks(configFolder+"/messages.utf8");
        properties(configFolder+"/config.properties");
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
    public String getAddressOfServer() {
        return addressOfServer;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getAuth() {
        return auth;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public ArrayList<Person> getVictims() {
        return victims;
    }

    @Override
    public ArrayList<Prank> getPranks() {
        return pranks;
    }

    @Override
    public void properties(String filename) throws IOException {
        FileInputStream file = new FileInputStream(filename);
        Properties prop = new Properties();
        prop.load(file);
        this.addressOfServer = prop.getProperty("smtpAddressOfServer");
        this.subject         = prop.getProperty("subject");
        this.username        = prop.getProperty("username");
        this.password        = prop.getProperty("password");
        this.portSmtp        = Integer.parseInt(prop.getProperty("smtpPortOfServer", "25"));
        this.auth = prop.getProperty("auth","true");
        this.timeout = Integer.parseInt(prop.getProperty("timeout", "5"));
        this.numberOfgroup  = Integer.parseInt(prop.getProperty("numberOfGroup"));
    }

    // @Override
    private ArrayList<Person> preparedAdresses(String filename) throws IOException {
        ArrayList<Person> persons;

        try(FileInputStream file = new FileInputStream(filename)){
            InputStreamReader sReader = new InputStreamReader(file, "UTF-8");

            try(BufferedReader bReader = new BufferedReader(sReader)){
                persons = new ArrayList<>();
                String address;

                while((address = bReader.readLine()) != null){
                    persons.add(new Person(address));
                }
            }
        }
        return persons;
    }

    // @Override
    private ArrayList<Prank> preparedPranks(String filename) throws IOException {
        ArrayList<Prank> pranks = new ArrayList<>();

        try(FileInputStream file = new FileInputStream(filename)){
            InputStreamReader sReader = new InputStreamReader(file, "UTF-8");

            try(BufferedReader bReader = new BufferedReader(sReader)){

                String line = bReader.readLine() ;
                while(line  != null){
                    Prank p = new Prank();
                    StringBuilder msgContent = new StringBuilder();
                    while(line != null && (!line.equals("******"))){
                        if(line.indexOf("Subject") != -1){
                            p.setPrankSubject((line.split(":"))[1]);
                        } else {
                            msgContent.append(line);
                            msgContent.append("\r\n");
                        }
                        p.setPrankContent(msgContent.toString());
                        line = bReader.readLine();
                    }
                    pranks.add(p);
                    line = bReader.readLine();
                }
                return pranks;
            }
        }
    }
}
