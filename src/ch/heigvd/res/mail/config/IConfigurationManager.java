package config;

import ch.heigvd.res.mail.model.mail.*;
import ch.heigvd.res.mail.model.prank.Prank;

import java.io.IOException;
import java.util.ArrayList;

public interface IConfigurationManager {

    /**@brief  : return the port number of the server.
     * @return : an Integer.
     */
    public int getPortSmtp();

    /**@brief  : return the number of groups that we have
     * @return : an Integer.
     */
    public int  getNumberOfGroups();

    /**@brief  : return the subject of an email
     * @return : a String
     */
    public String getSubject();

    /**@brief  : return the ip address of server.
     * @return : a String
     */
    public String getAdressOfServer();

    /**@brief  : return the victims
     * @return : a List of Person
     */
    public ArrayList<Person> getVictims();

    /**@Brief : get all properties of the protocol, set in conf.properties
     * @param :filename where properties were set
     * @throws: IOException
     */
    public void properties(String filename) throws IOException;

    /**@brief : get all mails adresses
     * @param :filename where vadresses are enumarate
     * @return: a List of Person
     * @throws IOException
     */
    public ArrayList<Person> preparedAdresses (String filename) throws IOException;

    /**@brief : get all pranks
     * @param : filename, where pranks were saved.
     * @return: a list of Pranks
     * @throws IOException
     */
    public ArrayList<Prank> preparedPranks(String filename) throws IOException;
}
