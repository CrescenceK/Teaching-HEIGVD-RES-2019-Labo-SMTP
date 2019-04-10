package ch.heigvd.res.mail.smtp;

import ch.heigvd.res.mail.model.mail.Mail;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class SMTPClient implements ISMTPClient {

    static public final String NODATA = "NODATA";
    static public final String term = "\r\n";

    private static Logger logger = Logger.getLogger("SMTPClient");
    private OutputStream os = null;
    private BufferedReader in = null;

    public Integer detectStatus(String s) {
        logger.info(s);
        if (s.length() < 3) {
            return -1;
        } else {
            return Integer.parseInt(s.substring(0, 3));
        }
    }

    @Override
    public void sendToServer(SMTPCommands command, String data) throws IOException {
        String realMessage = "";
        try {
            if (!data.equals(NODATA)) {
                realMessage = command.addData(data);

            } else {
                realMessage = command.toString();
            }
            logger.info(realMessage);
            os.write(realMessage.getBytes());
        } catch (Exception e) {

        }

    }

    public Integer receiveFromServ() throws IOException {
        String messageFromServer = in.readLine();
        return detectStatus(messageFromServer);
    }

    public void sendMail(Mail m) {
        Socket clientSocket = null;

        try {
            // Connection au serveur
            clientSocket = new Socket("vps665962.ovh.net", 2525);
            // Récupération des flux d'entrée / sortie
            os = clientSocket.getOutputStream();
            InputStream inS = clientSocket.getInputStream();
            in = new BufferedReader(new InputStreamReader(inS));

            // Écoute de la première réponse du serveur
            // Selon RFC, le serveur peut refuser la connexion. Alors, on quitte
            Integer statusFromServ;
            statusFromServ = receiveFromServ();
            if (statusFromServ == 554) {
                sendToServer(SMTPCommands.QUIT, NODATA);
                throw new Exception(ErrorMessages.CONNECTIONREFUSED.toString());
            } else if (statusFromServ != 220) {
                throw new Exception(ErrorMessages.WRONGSTATUS.toString());
            }

            // Envoi du premier message
            sendToServer(SMTPCommands.EHLO, NODATA);
            String answer;
            while ((answer = in.readLine()).charAt(3) != ' ') {
                if (detectStatus(answer) != 250) {
                    throw new Exception(ErrorMessages.WRONGSTATUS.toString());
                }
            }

            sendToServer(SMTPCommands.MAIL_FROM, m.getMail_from());
            if (receiveFromServ() != 250) {
                throw new Exception(ErrorMessages.WRONGSTATUS.toString());
            }

            sendToServer(SMTPCommands.RCPT_TO, m.getRcpt_to());
            if (receiveFromServ() != 250) {
                throw new Exception(ErrorMessages.WRONGSTATUS.toString());
            }

            sendToServer(SMTPCommands.DATA, NODATA);
            Integer serverStatus = receiveFromServ();
            switch (serverStatus) {
                case 354:
                    break;
                case 503:
                    throw new Exception(ErrorMessages.OUTOFSEQUENCE.toString());
                case 554:
                    throw new Exception(ErrorMessages.NOVALIDRCPT.toString());
                default:
                    throw new Exception(ErrorMessages.WRONGSTATUS.toString());
            }

            sendToServer(SMTPCommands.FROM, m.getFrom());
            sendToServer(SMTPCommands.TO, m.getTo());
            sendToServer(SMTPCommands.SUBJECT, m.getSubject());
            sendToServer(SMTPCommands.CONTENT, m.getText());
            sendToServer(SMTPCommands.END_DATA, NODATA);

            if (receiveFromServ() != 250) {
                throw new Exception(ErrorMessages.WRONGSTATUS.toString());
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (in != null)
                    in.close();
                if (os != null)
                    os.close();
                if (clientSocket != null)
                    clientSocket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private enum ErrorMessages {
        WRONGSTATUS("The server answered with wrong status code."),
        CONNECTIONREFUSED("The server refused the connection"),
        NOVALIDRCPT("The server send: no valid recipients"),
        OUTOFSEQUENCE("The server send: command out of sequence");
        private String errorMessage = "";

        ErrorMessages(String s) {
            this.errorMessage = s;
        }

        public String toString() {
            return errorMessage;
        }
    }

    public enum SMTPCommands {
        //Objets directement construits
        EHLO("EHLO Test" + SMTPClient.term),
        MAIL_FROM("MAIL FROM: "),
        RCPT_TO("RCPT TO: "),
        DATA("DATA" + SMTPClient.term),
        FROM("From: "),
        TO("To: "),
        SUBJECT("Subject: "),
        END_DATA(term + "." + term),
        CONTENT(""),
        QUIT("QUIT" + SMTPClient.term);

        private String command = "";

        //Constructeur
        SMTPCommands(String command) {
            this.command = command;
        }

        public String addData(String d) throws Exception {
            if (this == EHLO || this == DATA || this == END_DATA || this == QUIT)
                throw new Exception("This command does not take args.");
            if (this == SUBJECT)
                return command + d + term + term;
            else
                return command + d + term;
        }

        public String toString() {
            return command;
        }
    }
}
