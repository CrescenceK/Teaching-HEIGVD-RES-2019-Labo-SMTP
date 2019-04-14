package ch.heigvd.res.mail.config;

import ch.heigvd.res.mail.model.mail.*;
import ch.heigvd.res.mail.model.prank.Prank;

import java.io.IOException;
import java.util.ArrayList;

public interface IConfigurationManager {

    /**@brief  : return the port number of the server.
     * @return : an Integer.
     */
    int getPortSmtp();

    /**@brief  : return the number of groups that we have
     * @return : an Integer.
     */
    int  getNumberOfGroups();

    /**@brief  : return the ip address of server.
     * @return : a String
     */
    String getAddressOfServer();

    /**@brief  : return the username. useful while connecting to mailtrap
     * @return
     */
    String getUsername();

    /**@brief  : return the password. useful while connecting to mailtrap
     * @return : a string
     */
    String getPassword();

    /**
     * returns the boolean which define if an auth must be tried
     * @return a String
     */
    String getAuth();

    /**
     * return the timeout in sec. between 2 mails
     * @return a int
     */
    int getTimeout();

    /**@brief  : return the victims
     * @return : a List of Person
     */
    ArrayList<Person> getVictims();


    /**@brief  : return pranks genrate by parsing file message.utf8
     * @return : an ArrayList of Prank
     */
    ArrayList<Prank> getPranks();

    /**@Brief : get all properties of the protocol, set in conf.properties
     * @param :filename where properties were set
     * @throws: IOException
     */
    void properties(String filename) throws IOException;

}