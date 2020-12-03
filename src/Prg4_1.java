import java.util.Random;

/*
Prg4-1 CSMA/CD
 Write a program to simulate the behavior of the CSMA/CD protocol over Ethernet when there are
 N stations ready to transmit while a frame is being transmitted. Your program should report the
 times when each station successfully starts sending its frame. Assume that a clock tick occurs
 once every slot time (51.2 usec) and a collision detection and sending of a jamming sequence
 takes one slot time. All frames are the maximum length allowed.
 */
public class Prg4_1
{
    private int N; //表示station的数量
    private int[] cnt; //表示该station冲突发生的次数，初值为0
    private int[] nextT; //表示该station下一次发送的slot time时间点，初值为0
    private int[] result; //表示该station最后发送的结果
    private void init(int n)
    {
        N = n;
        cnt = new int[n];
        nextT = new int[n];
        result = new int[n];
    }
    public void work(int n)
    {
        init(n);
        Random random = new Random();
        for(int t = 0, id, tmp;; t++)
        {
            boolean flag = false;
            for(int i = 0; !flag && i < N; i++)
                if(result[i] == 0) flag = true;
            if(!flag) break;

            id = -1;
            for(int i = 0; id != -2 && i < N; i ++)
                if(nextT[i] == t)
                {
                    if(id == -1)
                        id = i;
                    else id = -2;
                }
            if(id == -1) continue;
            if(id == -2)
            {
                for(int i = 0; i < N; i++)
                    if(nextT[i] == t)
                    {
                        if(cnt[i] > 16) result[i] = -1;
                        tmp = (1 << cnt[i]++) + 1;
                        tmp = random.nextInt(tmp);
                        if(tmp > 1023) tmp = 1023;
                        nextT[i] += tmp + 1;
                    }
            }
            else
            {
                //id可以发送信息
                result[id] = ++t;
                for(int i = 0; i < N; i++)
                    if(nextT[i] == t)
                        ++nextT[i];
            }
        }
        for(int i = 0; i < N; i++)
        {
            if(result[i] == -1)
                System.out.println(i + ":\tFailed!");
            else
                System.out.println(i + ":\tSuccess in " + result[i] + ".");
        }
    }
}