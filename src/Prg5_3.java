/**
 *  Prg5-3 Do forwarding in an IP router
 *  Write a function to do forwarding in an IP router. The procedure has one parameter, an IP
 *  address. It also has access to a global table consisting of an array of triples. Each triple
 *  contains three integers: an IP address, a subnet mask, and the outline line to use. The function
 *  looks up the IP address in the table using CIDR and returns the line to use as its value.
 */
public class Prg5_3
{
    private static String[][] table = {
            {"192.24.0.0",  "255.255.128.0",    "1"},
            {"192.24.0.0",  "255.255.248.0",    "2"},
            {"192.24.16.0", "255.255.240.0",    "3"},
            {"192.24.8.0",  "255.255.252.0",    "4"}
    };
    private static String and(String s1, String s2)
    {
        String[] ss1 = s1.split("\\.");
        String[] ss2 = s2.split("\\.");
        String ret = String.valueOf(Integer.parseInt(ss1[0]) & Integer.parseInt(ss2[0]));
        for(int i = 1; i < ss1.length; i++)
            ret += "." + (Integer.parseInt(ss1[i]) & Integer.parseInt(ss2[i]));
        return ret;
    }
    private static boolean smaller(String s1, String s2)
    {
        String[] ss1 = s1.split("\\.");
        String[] ss2 = s2.split("\\.");
        for(int i = 0; i < ss1.length; i++)
            if(Integer.parseInt(ss1[i]) >= Integer.parseInt(ss2[i]))
                return false;
        return true;
    }
    public static int route(String ip)
    {
        int ret = -1;
        String mask = "0.0.0.0";
        for(int i = 0; i < table.length; i++)
        {
            if(!and(ip, table[i][1]).equals(and(table[i][0], table[i][1])))
                continue;
            if(smaller(table[i][1], mask))
                continue;
            ret = Integer.parseInt(table[i][2]);
            mask = table[i][1];
        }
        return ret;
    }
}
