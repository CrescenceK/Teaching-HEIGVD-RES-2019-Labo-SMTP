package ch.heigvd.res.mail.config;

import ch.heigvd.res.mail.model.mail.*;
import java.io.IOException;
import java.util.List;

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
    public List<Person> getVictims();

    /**@brief  : return mails prepared for jokes
     * @return : a List of mail
     */
    public List<Mail> getMails();

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
    public List<Person> preparedAdresses (String filename) throws IOException;

    /**@brief : get all mails messages
     * @param : filename, where messages were saved.
     * @return: a list of mail
     * @throws IOException
     */
    public List<Mail> preparedMails(String filename) throws IOException;
}
