package ch.heigvd.res.mail.model.Mail;
import java.util.ArrayList;

public class Group {
    private ArrayList<Person> members;
    private Person sender;

    public Group(Person sender, ArrayList<Person> members) throws Exception{
        if(members.size() < 2){
            throw new Exception("Un groupe doit contenir au minimum 2 personnes");
        } else {
            this.sender = sender;
            this.members = members;
        }
    }

    public void addMember(Person person){
        this.members.add(person);
    }
}
