package ch.heigvd.res.mail.model.prank;

import ch.heigvd.res.mail.model.mail.*;
import ch.heigvd.res.mail.smtp.SMTPClient;

public class PrankGenerator{
public static void main(String[] args) {
        try{
            Mail m = new Mail("florian.polier@heig-vd.ch","polier.florian@gmail.com","Batman", "polier.florian@gmail.com", "dsfdsf", "sdfdsf");
            SMTPClient client = new SMTPClient("smtp.mailtrap.io", 25, "bb645f48e64664", "3db3a846ab9eac");
            client.sendMail(m);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
