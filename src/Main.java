import java.util.Random;

public class Main
{
    public static void main(String args[])
    {
        //Prg1_1.applicationLayerFunction("HWQ");

        /*
        Prg3_1 prg3_1 = new Prg3_1();
        Prg3_1.Generator generator = prg3_1.new Generator();
        Prg3_1.Alter alter = prg3_1.new Alter();
        Prg3_1.Verifier verifier = prg3_1.new Verifier();
        String mess = "11010111110000";
        String polynomial = "10011";
        mess = generator.work(mess, polynomial);
        //mess = alter.work(mess);
        System.out.println(verifier.work(mess,polynomial));
        */

        /*
        Prg4_1 prg4_1 = new Prg4_1();
        prg4_1.work(2);
        */

        /*
        Prg5_1 prg5_1 = new Prg5_1(5);
        prg5_1.addEdge(1,2);
        prg5_1.addEdge(2,3);
        prg5_1.addEdge(3,4);
        prg5_1.addEdge(2,4);
        prg5_1.addEdge(4,5);
        prg5_1.addEdge(2,5);
        prg5_1.Floyed();
        prg5_1.setBandwidth();
        System.out.print(prg5_1.flood1(6,1,5));
        System.out.println(" " + prg5_1.getBandwidth());
        prg5_1.setBandwidth();
        System.out.print(prg5_1.flood2(6,1,5));
        System.out.println(" " + prg5_1.getBandwidth());
        prg5_1.setBandwidth();
        System.out.print(prg5_1.flood3(6,1,5,1));
        System.out.println(" " + prg5_1.getBandwidth());
         */

        //System.out.println(Prg5_3.route("192.24.127.2"));

        Prg6_1 prg6_1 = new Prg6_1();
        Prg6_1.Client client0 = prg6_1.new Client("HWQ");
        Prg6_1.Client client1 = prg6_1.new Client("WZY");
        Prg6_1.Client client2 = prg6_1.new Client("YXK");
        client0.start();
        client1.join(client0.getId());
        client0.send("Hello!");
        System.out.println("1：\r\n" + client0.check());
        client1.send("Hi!");
        client0.send("Are you wzy?");
        client0.send("It's a really good name.");
        client0.send("I like this name.");
        client1.send("Thank you!");
        System.out.println("2：\r\n" + client1.check());
        client2.join(client1.getId());
        client2.send("lalala");
        client0.send("Oh, new guys coming.");
        client1.send("Welcome!");
        client0.send("Welcome!");
        System.out.println("3：\r\n" + client2.check());
        client2.send("Hello everyone!");
        client2.send("I need to talk to you alone, WZY.");
        client2.send("Let's take a new chat room.");
        client2.send("Sorry, HWQ.");
        client0.send("No problem.");
        System.out.println("3：\r\n" + client2.check());
        client2.leave();
        client0.leave();
        client2.start();
        client1.join(client2.getId());
        client2.send("Fuck you!");
        client2.send("Ha Ha Ha!");
        client2.leave();
        System.out.println("4：\r\n" + client1.check());
        client1.send("......");
        client1.leave();
    }
}