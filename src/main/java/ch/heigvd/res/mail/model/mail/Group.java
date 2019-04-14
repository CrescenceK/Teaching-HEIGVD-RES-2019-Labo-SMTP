package main.java.ch.heigvd.res.mail.model.mail;
import java.util.ArrayList;


public class Group {
    private ArrayList<Person> members = new ArrayList<>();
    private Person sender ;

    public Group(){};

    public void addMember(Person person){
        this.members.add(person);
    }

    public ArrayList<Person> getMembers() {
        return (ArrayList<Person>) members.clone();
    }

    public void setSender(Person p){ this.sender = p; }

    public Person getSender() {
        return sender;
    }
}