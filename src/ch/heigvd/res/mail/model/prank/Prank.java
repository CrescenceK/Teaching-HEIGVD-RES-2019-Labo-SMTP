package ch.heigvd.res.mail.model.prank;

public class Prank {

    private String prankSubject;
    private String prankContent;

    public Prank() {};

    public String getPrankSubject() {
        return prankSubject;
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

}