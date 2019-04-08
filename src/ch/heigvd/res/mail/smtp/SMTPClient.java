package ch.heigvd.res.mail.smtp;

import ch.heigvd.res.mail.model.mail.Mail;

import java.io.*;
import java.net.Socket;

public class SMTPClient implements ISMTPClient {
    public void sendMail(Mail m) {
        String term = "\r\n";
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
            String EHLO = "EHLO Obyka" + term;
            os.write(EHLO.getBytes());

            while ((messageFromServer = in.readLine()).charAt(3) != ' ') {
                System.out.println(messageFromServer);
            }

            String MAIL_FROM = "MAIL FROM: " + m.getMail_from() + term;
            os.write(MAIL_FROM.getBytes());
            messageFromServer = in.readLine();
            System.out.println(messageFromServer);

            String RCPT_TO = "RCPT TO: " + m.getRcpt_to() + term;
            os.write(RCPT_TO.getBytes());
            messageFromServer = in.readLine();
            System.out.println(messageFromServer);

            String DATA = "DATA" + term;
            os.write(DATA.getBytes());
            messageFromServer = in.readLine();
            System.out.println(messageFromServer);

            String dataContent = "From: " + m.getFrom() + term;
            dataContent += "To: " + m.getTo() + term;
            dataContent += "Subject: " + m.getSubject() + term + term;
            dataContent += m.getText() + term + "." + term;
            os.write(dataContent.getBytes());
            messageFromServer = in.readLine();
            System.out.println(messageFromServer);

        } catch (IOException ex) {
        } finally {
            try {
                in.close();
                inS.close();
                os.close();
                clientSocket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
