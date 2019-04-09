package ch.heigvd.res.mail.smtp;

import ch.heigvd.res.mail.model.mail.Mail;

import java.io.*;
import java.net.Socket;

public class SMTPClient implements ISMTPClient {

    public enum SMTPCommands {
        //Objets directement construits
        EHLO ("EHLO Test"+ SMTPClient.term),
        MAIL_FROM("MAIL FROM: "),
        RCPT_TO ("RCPT TO: "),
        DATA("DATA"+SMTPClient.term),
        FROM("From: "),
        TO ("To: "),
        SUBJECT("Subject: "+term+term),
        END_DATA(term+"."+term),
        QUIT("QUIT"+SMTPClient.term);

        private String command = "";

        //Constructeur
        SMTPCommands(String command){
            this.command = command;
        }

        public String addData(String d){
            return command + d + SMTPClient.term;
        }
        public String toString(){
            return command;
        }
    }

    static public final String term = "\r\n";

    public Integer detectStatus(String s){
        if(s.length()<3){
            return -1;
        }
        else {
            return Integer.parseInt(s.substring(0, 3));
        }
    }

    public void sendMail(Mail m) {
        Socket clientSocket = null;
        OutputStream os = null;
        InputStream inS = null;
        BufferedReader in = null;

        try {
            // Connection au serveur
            clientSocket = new Socket("localhost", 25);
            // Récupération des flux d'entrée / sortie
            os = clientSocket.getOutputStream();
            inS = clientSocket.getInputStream();
            in = new BufferedReader(new InputStreamReader(inS));

            // Écoute de la première réponse du serveur
            String messageFromServer = null;
            messageFromServer = in.readLine();
            System.out.println(messageFromServer);

            // Envoi du premier message
            os.write(SMTPCommands.EHLO.toString().getBytes());

            while ((messageFromServer = in.readLine()).charAt(3) != ' ') {
                System.out.println(messageFromServer);
            }

            os.write(SMTPCommands.MAIL_FROM.addData(m.getMail_from()).getBytes());
            messageFromServer = in.readLine();
            System.out.println(messageFromServer);

            os.write(SMTPCommands.RCPT_TO.addData(m.getRcpt_to()).getBytes());
            messageFromServer = in.readLine();
            System.out.println(messageFromServer);

            os.write(SMTPCommands.DATA.toString().getBytes());
            messageFromServer = in.readLine();
            System.out.println(messageFromServer);

            String dataContent = SMTPCommands.FROM.addData(m.getFrom());
            dataContent += SMTPCommands.TO.addData(m.getTo());
            dataContent += SMTPCommands.SUBJECT.addData(m.getSubject());
            dataContent += m.getText() + SMTPCommands.END_DATA;
            os.write(dataContent.getBytes());
            messageFromServer = in.readLine();
            System.out.println(messageFromServer);

        } catch (IOException ex) {
        } finally {
            try {
                if(in != null)
                    in.close();
                if(inS != null)
                    inS.close();
                if(os != null)
                    os.close();
                clientSocket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
