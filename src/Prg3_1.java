import java.util.Arrays;

/*
Prg3-1 CRC
 The goal of this lab exercise is to implement an error-detection mechanism using the standard
 CRC algorithm described in the text. Write two programs, generator and verifier.The
 generator program reads from standard input a line of ASCII text containing an n-bit message
 consisting of a string of 0s and 1s. The second line is the k-bit polynomial, also in ASCII. It
 outputs to standard output a line of ASCII text with n+k 0s and 1s representing the message
 to be transmitted. Then it outputs the polynomial, just as it read it in. The verifier program
 reads in the output of the generator program and outputs a message indicating whether it is
 correct or not. Finally, write a program, alter, that inverts 1 bit on the first line depending on its
 argument (the bit number counting the leftmost bit as 1) but copies the rest of the two lines
 correctly.
 By typing
    generator < file | verifier
 you should see that the message is correct, but by typing
    generator < file | alter arg | verifier
 you should get the error message.
 */
public class Prg3_1
{
    //最多允许输入31位
    class Generator
    {
        String work(String mess, String polynomial)
        {
            //Todo
        }
    }
    private String xor(String s1, String s2)
    {
        if(s1.length() < s2.length())
        {
            String tmp = s1;
            s1 = s2; s2 = tmp;
        }
        s2 = Arrays.toString((new byte[s1.length() - s2.length()])) + s2;
        byte[] b1 = s1.getBytes();
        byte[] b2 = s2.getBytes();
        for(int i = 0; i < b1.length; i++)
            b1[i] = (byte)(b1[i] ^ b2[i]);
        return Arrays.toString(b1);
    }
}
