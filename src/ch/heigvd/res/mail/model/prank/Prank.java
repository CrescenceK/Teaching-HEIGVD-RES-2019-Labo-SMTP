package ch.heigvd.res.mail.model.prank;

import ch.heigvd.res.mail.model.Mail.Group;

import java.util.ArrayList;

public class Prank {

    private String fakeSender;
    private ArrayList<Group> groupsToPrank;
    private String prankSubject;
    private String prankContent;

    public String getFakeSender(){
        return fakeSender;
    }

    public String getPrankSubject() {
        return prankSubject;
    }

    public ArrayList<Group> getGroupsToPrank() {
        return groupsToPrank;
    }

    public void setPrankSubject(String prankSubject) {
        this.prankSubject = prankSubject;
    }

    public String getPrankContent() {
        return prankContent;
    }

    public void setPrankContent(String prankContent) {
        this.prankContent = prankContent;
    }

    public Prank(ArrayList<Group> groupsToPrank, String prankSubject, String prankContent, String fakeSender) {
        this.groupsToPrank = groupsToPrank;
        this.prankSubject = prankSubject;
        this.prankContent = prankContent;
        this.fakeSender = fakeSender;
    }

    public void addGroup(Group group){
        groupsToPrank.add(group);
    }
}
