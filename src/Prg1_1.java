/**
 *  Prg1-1 Write a program that implements message flow from the top layer to the bottom layer of
 *  the 7-layer protocol model. Your program should include a separate protocol function for each
 *  layer. Protocol headers are sequence up to 64 characters. Each protocol function has two
 *  parameters: a message passed from the higher layer protocol (a char buffer) and the size of the
 *  message. This function attaches its header in front of the message, prints the new message on
 *  the standard output, and then invokes the protocol function of the lower-layer protocol.
 *  Program input is an applications message (a sequence of 80 characters or less).
 */
public class Prg1_1
{
    static final String[] headers = {
            "applicationLayerHeader:",
            "presentationLayerHeader:",
            "sessionLayerHeader:",
            "transportLayerHeader:",
            "networkLayerHeader:",
            "dataLinkLayerHeader:",
            "physicalLayerHeader:"
    };

    public static void applicationLayerFunction(String str)
    {
        String ret = headers[0] + str;
        System.out.println(ret);
        presentationLayerFunction(ret);
    }

    public static void presentationLayerFunction(String str)
    {
        String ret = headers[1] + str;
        System.out.println(ret);
        sessionLayerFunction(ret);
    }

    public static void sessionLayerFunction(String str)
    {
        String ret = headers[2] + str;
        System.out.println(ret);
        transportLayerFunction(ret);
    }

    public static void transportLayerFunction(String str)
    {
        String ret = headers[3] + str;
        System.out.println(ret);
        networkLayerFunction(ret);
    }

    public static void networkLayerFunction(String str)
    {
        String ret = headers[4] + str;
        System.out.println(ret);
        dataLinkLayerFunction(ret);
    }

    public static void dataLinkLayerFunction(String str)
    {
        String ret = headers[5] + str;
        System.out.println(ret);
        physicalLayerFunction(ret);
    }

    public static void physicalLayerFunction(String str)
    {
        String ret = headers[6] + str;
        System.out.println(ret);
    }
}
