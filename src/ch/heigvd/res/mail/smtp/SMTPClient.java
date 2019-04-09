package ch.heigvd.res.mail.smtp;

import ch.heigvd.res.mail.model.mail.Mail;

import java.io.*;
import java.net.Socket;

public class SMTPClient implements ISMTPClient {

    static public final String term = "\r\n";

    private OutputStream os = null;
    private BufferedReader in = null;

    public Integer detectStatus(String s){
        if(s.length()<3){
            return -1;
        }
        else {
            return Integer.parseInt(s.substring(0, 3));
        }
    }

    @Override
    public void sendToServer(String s) throws IOException{
        os.write(s.getBytes());
    }

    public Integer receiveFromServ() throws IOException{
        String messageFromServer = in.readLine();
        return detectStatus(messageFromServer);
    }

    public void sendMail(Mail m) {
        Socket clientSocket = null;

        try {
            // Connection au serveur
            clientSocket = new Socket("localhost", 25);
            // Récupération des flux d'entrée / sortie
            os = clientSocket.getOutputStream();
            InputStream inS = clientSocket.getInputStream();
            in = new BufferedReader(new InputStreamReader(inS));

            // Écoute de la première réponse du serveur
            Integer statusFromServ = 0;
            statusFromServ = receiveFromServ();
            System.out.println(statusFromServ);

            // Envoi du premier message
            os.write(SMTPCommands.EHLO.toString().getBytes());

            while ((in.readLine()).charAt(3) != ' ') {
                System.out.println(statusFromServ);
            }

            sendToServer(SMTPCommands.MAIL_FROM.addData(m.getMail_from()));
            statusFromServ = receiveFromServ();
            System.out.println(statusFromServ);

            sendToServer(SMTPCommands.RCPT_TO.addData(m.getRcpt_to()));
            statusFromServ = receiveFromServ();
            System.out.println(statusFromServ);

            sendToServer(SMTPCommands.DATA.toString());
            statusFromServ = receiveFromServ();
            System.out.println(statusFromServ);

            String dataContent = SMTPCommands.FROM.addData(m.getFrom());
            dataContent += SMTPCommands.TO.addData(m.getTo());
            dataContent += SMTPCommands.SUBJECT.addData(m.getSubject());
            dataContent += m.getText() + SMTPCommands.END_DATA;
            sendToServer(dataContent);
            statusFromServ = receiveFromServ();
            System.out.println(statusFromServ);

        } catch (Exception ex) {
        } finally {
            try {
                if(in != null)
                    in.close();
                if(os != null)
                    os.close();
                clientSocket.close();
            } catch (IOException ignored) {
            }
        }
    }

    public enum SMTPCommands {
        //Objets directement construits
        EHLO ("EHLO Test"+ SMTPClient.term),
        MAIL_FROM("MAIL FROM: "),
        RCPT_TO ("RCPT TO: "),
        DATA("DATA"+SMTPClient.term),
        FROM("From: "),
        TO ("To: "),
        SUBJECT("Subject: "),
        END_DATA(term+"."+term),
        QUIT("QUIT"+SMTPClient.term);

        private String command = "";

        //Constructeur
        SMTPCommands(String command){
            this.command = command;
        }

        public String addData(String d) throws Exception{
            if(this == EHLO || this == DATA || this == END_DATA || this == QUIT)
                throw new Exception("This command does not take args.");
            if(this == SUBJECT)
                return command + d + term + term;
            else
                return command + d + term;
        }
        public String toString(){
            return command;
        }
    }
}
