public class Main
{
    public static void main(String args[])
    {
        //Prg1_1.applicationLayerFunction("HWQ");

        Prg3_1 prg3_1 = new Prg3_1();
        Prg3_1.Generator generator = prg3_1.new Generator();
        Prg3_1.Alter alter = prg3_1.new Alter();
        Prg3_1.Verifier verifier = prg3_1.new Verifier();
        String mess = "11010111110000";
        String polynomial = "10011";
        mess = generator.work(mess, polynomial);
        //mess = alter.work(mess);
        System.out.println(verifier.work(mess,polynomial));
    }
}