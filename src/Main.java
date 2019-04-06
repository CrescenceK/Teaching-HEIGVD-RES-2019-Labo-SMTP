import ch.heigvd.res.mail.model.Mail.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Person s1 = new Person("monsieurprank@heig-vd.ch");
        Person s2 = new Person("monsieurprank2@heig-vd.ch");

        Person p1 = new Person("florian.polier@heig-vd.ch");
        Person p2 = new Person("polier.florian@gmail.com");
        Person p3 = new Person("mercierjordan@hotmail.com");

        ArrayList<Person> al1 = new ArrayList<>();
        al1.add(p1);
        al1.add(p2);

        ArrayList<Person> al2 = new ArrayList<>();
        al2.add(p3);

        try {
            Group g1 = new Group(s1, al1);
        } catch (Exception e) {
            System.out.println("erreur");
        }
        try {
            Group g2 = new Group(s2, al2);
        } catch (Exception e) {
            System.out.println("erreur");
        }
    }
}
