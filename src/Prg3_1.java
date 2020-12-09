import java.util.Arrays;
import java.util.Random;

/**
 * Prg3-1 CRC
 *  The goal of this lab exercise is to implement an error-detection mechanism using the standard
 *  CRC algorithm described in the text. Write two programs, generator and verifier.The
 *  generator program reads from standard input a line of ASCII text containing an n-bit message
 *  consisting of a string of 0s and 1s. The second line is the k-bit polynomial, also in ASCII. It
 *  outputs to standard output a line of ASCII text with n+k 0s and 1s representing the message
 *  to be transmitted. Then it outputs the polynomial, just as it read it in. The verifier program
 *  reads in the output of the generator program and outputs a message indicating whether it is
 *  correct or not. Finally, write a program, alter, that inverts 1 bit on the first line depending on its
 *  argument (the bit number counting the leftmost bit as 1) but copies the rest of the two lines
 *  correctly.
 *  By typing
 *     generator < file | verifier
 *  you should see that the message is correct, but by typing
 *     generator < file | alter arg | verifier
 *  you should get the error message.
 */
public class Prg3_1
{
    class Generator
    {
        String work(String mess, String polynomial)
        {
            String s = mod(mess, polynomial);
            s = xor(mess, s);
            return s;
        }
    }
    class Alter
    {
        String work(String mess)
        {
            int pos = new Random().nextInt(mess.length());
            if(mess.charAt(pos) == '0')
                return mess.substring(0, pos) + "1" + mess.substring(pos + 1);
            return  mess.substring(0, pos) + "0" + mess.substring(pos + 1);
        }
    }
    class Verifier
    {
        boolean work(String mess, String polynomial)
        {
            String s = mod(mess, polynomial);
            return !s.contains("1");
        }
    }
    private String xor(String s1, String s2)
    {
        s1 = s1.replaceAll("^(0+)", "");
        s2 = s2.replaceAll("^(0+)", "");
        if(s1.length() < s2.length())
        {
            String tmp = s1;
            s1 = s2; s2 = tmp;
        }
        s2 = new String(new byte[s1.length() - s2.length()]).replace("\0", "0") + s2;
        byte[] b1 = s1.getBytes();
        byte[] b2 = s2.getBytes();
        for(int i = 0; i < b1.length; i++)
            b1[i] = (byte) ((b1[i] - '0' ^ b2[i] - '0') + '0');
        return new String(b1);
    }
    private String mod(String s1, String s2)
    {
        String ret = s1.substring(0, s2.length());
        for(int i = 0; i <= s1.length() - s2.length(); i++)
        {
            if(i != 0) ret += s1.charAt(i + s2.length() - 1);
            ret = ret.replaceAll("^(0+)", "");
            if(ret.length() >= s2.length())
                ret = xor(ret, s2);
        }
        return ret;
    }
}
