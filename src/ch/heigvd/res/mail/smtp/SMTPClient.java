package ch.heigvd.res.mail.smtp;

import ch.heigvd.res.mail.model.mail.Mail;

import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.logging.Logger;

import static ch.heigvd.res.utils.HMAC.hmacDigest;

public class SMTPClient implements ISMTPClient {

    // Here are constant flag / value we use
    static public final String NODATA = "NODATA";
    static public final String CRLF = "\r\n";

    private static Logger logger = Logger.getLogger("SMTPClient");
    private OutputStream os = null;
    private BufferedReader in = null;

    // Session dependant info
    private String username;
    private String password;
    private String hostname;
    private Integer port;
    private boolean auth;

    public SMTPClient(String hostname, Integer port, String username, String password, Boolean auth) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.auth = auth;
    }

    private Integer detectStatus(String s) {
        logger.info(s);
        if (s.length() < 3)
            return -1;
        else
            return Integer.parseInt(s.substring(0, 3));
    }

    @Override
    public void sendToServer(SMTPCommands command, String data) throws IOException {
        String realMessage = "";
        try {
            realMessage = !data.equals(NODATA) ? command.addData(data) : command.toString();
            logger.info(realMessage);
            os.write(realMessage.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Integer receiveFromServ() throws IOException {
        String messageFromServer = in.readLine();
        return detectStatus(messageFromServer);
    }

    public boolean authenticate(SMTPClient.AuthMethods authMethods) throws Exception {
        switch (authMethods) {
            case NO_AUTH:
                break;
            case AUTH_LOGIN:
                sendToServer(SMTPCommands.AUTH_LOGIN, NODATA);

                if (receiveFromServ() != 334)
                    throw new Exception(ErrorMessages.WRONGSTATUS.toString());

                String userEncoded = Base64.getEncoder().encodeToString(username.getBytes());
                String passEncoded = Base64.getEncoder().encodeToString(password.getBytes());
                sendToServer(SMTPCommands.CONTENT, userEncoded);

                if (receiveFromServ() != 334)
                    throw new Exception(ErrorMessages.WRONGSTATUS.toString());

                sendToServer(SMTPCommands.CONTENT, passEncoded);

                if (receiveFromServ() != 235)
                    throw new Exception(ErrorMessages.WRONGSTATUS.toString());
                break;

            case AUTH_CRAM_MD5:
                sendToServer(SMTPCommands.AUTH_CRAM_MD5, NODATA);
                String challenge = "";
                if (detectStatus(challenge = in.readLine()) != 334)
                    throw new Exception(ErrorMessages.WRONGSTATUS.toString());

                // Only the challenge part is ripped
                challenge = challenge.substring(4);
                // Decoded challenge
                byte[] tmpChall = Base64.getDecoder().decode(challenge);
                challenge = new String(tmpChall);
                // We compute the digest as specified in RFC 2195
                String digest = hmacDigest(challenge, password, "HmacMD5");
                String finalResponse = username + " " + digest;
                finalResponse = Base64.getEncoder().encodeToString(finalResponse.getBytes());

                sendToServer(SMTPCommands.CONTENT, finalResponse);
                if (receiveFromServ() != 235)
                    throw new Exception(ErrorMessages.WRONGSTATUS.toString());
                break;
            default:
                throw new Exception("Authentification not supported");
        }
        return true;
    }

    public void sendMail(Mail m) {
        Socket clientSocket = null;

        try {
            // Connection au serveur
            clientSocket = new Socket("smtp.mailtrap.io", 25);
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
            String answer, capabilities;
            AuthMethods authMethods = AuthMethods.NO_AUTH;
            while (true) {
                answer = in.readLine();
                capabilities = answer.substring(4, answer.length());
                if (capabilities.startsWith("AUTH") && capabilities.contains("CRAM-MD5"))
                    authMethods = AuthMethods.AUTH_CRAM_MD5;
                else if (capabilities.startsWith("AUTH") && capabilities.contains("LOGIN"))
                    authMethods = AuthMethods.AUTH_LOGIN;

                if (detectStatus(answer) != 250)
                    throw new Exception(ErrorMessages.WRONGSTATUS.toString());
                if (answer.charAt(3) == ' ')
                    break;
            }

            // Si la config et le serveur demande une authentification
            if(auth)
                authenticate(authMethods);

            sendToServer(SMTPCommands.MAIL_FROM, m.getMail_from());
            if (receiveFromServ() != 250)
                throw new Exception(ErrorMessages.WRONGSTATUS.toString());

            sendToServer(SMTPCommands.RCPT_TO, m.getRcpt_to());
            if (receiveFromServ() != 250)
                throw new Exception(ErrorMessages.WRONGSTATUS.toString());

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

            if (receiveFromServ() != 250)
                throw new Exception(ErrorMessages.WRONGSTATUS.toString());

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

    public enum AuthMethods {
        AUTH_LOGIN,
        AUTH_CRAM_MD5,
        NO_AUTH;
    }

    public enum SMTPCommands {
        //Objets directement construits
        EHLO("EHLO Test" + CRLF),
        AUTH_LOGIN("AUTH LOGIN" + CRLF),
        AUTH_CRAM_MD5("AUTH CRAM-MD5" + CRLF),
        MAIL_FROM("MAIL FROM: <"),
        RCPT_TO("RCPT TO: <"),
        DATA("DATA" + CRLF),
        FROM("From: "),
        TO("To: "),
        SUBJECT("Subject: "),
        END_DATA(CRLF + "." + CRLF),
        CONTENT(""),
        QUIT("QUIT" + CRLF);

        private String command = "";

        //Constructeur
        SMTPCommands(String command) {
            this.command = command;
        }

        public String addData(String d) throws Exception {
            if (this == EHLO || this == DATA || this == END_DATA || this == QUIT || this == AUTH_LOGIN)
                throw new Exception("This command does not take args.");
            else if (this == SUBJECT)
                return command + d + CRLF + CRLF;
            else if (this == MAIL_FROM || this == RCPT_TO)
                return command + d + ">" + CRLF;
            else
                return command + d + CRLF;
        }

        public String toString() {
            return command;
        }
    }
}
