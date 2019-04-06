package ch.heigvd.res.mail.model.prank;

import ch.heigvd.res.mail.model.Mail.Group;
import ch.heigvd.res.mail.model.Mail.Mail;

import java.util.ArrayList;

public class Prank {
    private Mail prankMail;
    private ArrayList<Group> groupsToPrank;

    public Prank(Mail prankMail, ArrayList<Group> groupsToPrank) {
        this.prankMail = prankMail;
        this.groupsToPrank = groupsToPrank;
    }

    public void addGroup(Group group){
        groupsToPrank.add(group);
    }
}
