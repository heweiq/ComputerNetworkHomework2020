import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * Prg5-2 Simulating a computer network using discrete time
 *  Write a program that simulates a computer network using discrete time. The first packet on each
 *  router queue makes one hop per time interval. Each router has only a finite number of buffers. If
 *  a packet arrives and there is no room for it, it is discarded and not retransmitted. Instead, there is
 *  an end-to-end protocol, complete with timeouts and acknowledgement packets, that eventually
 *  regenerates the packet from the source router. Plot the throughput of the network as a function
 *  of the end-to-end timeout interval, parameterized by error rate.
 */
class Prg5_2
{
    //length用户要求产生字符串的长度
    static final String theAbcStr="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String getRandomString(int length){
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(theAbcStr.charAt(number));
        }
        return sb.toString();
    }
    private int N; //表示点的数量
    private int Siz; //表示buffer的最大容量
    private int tot; //邻接表边数
    private int[] first, next, to; //邻接表
    private int[] everySum, everySuc; //每个时刻的sum和suc
    class Packet
    {
        int s,t,ttl;
        String mess;
        Packet(int s, int t, int ttl, String mess)
        {
            this.s = s; this.t = t; this.ttl = ttl; this.mess = mess;
        }
    }
    ArrayList<ArrayList<Packet>> buffer;
    public Prg5_2(int n,int siz)
    {
        N = n;
        Siz = siz;
        tot = 2; //从2开始计数，避开first的初值0
        first = new int[n + 1];
        next = new int[2 + (n * (n - 1) << 1)];
        to = new int[2 + (n * (n - 1) << 1)];
        buffer = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            buffer.add(new ArrayList<>());
    }
    public int[] getEverySum() {return everySum;}
    public int[] getEverySuc() {return everySuc;}
    //单向边
    private void add(int u, int v)
    {
        to[tot] = v;
        next[tot] = first[u];
        first[u] = tot++;
    }
    //双向边
    public void addEdge(int u, int v)
    {
        add(u, v);
        add(v, u);
    }

    //某个点的buffer中加入包
    private void addPacketInBuffer(Packet packet, int x)
    {
        if(buffer.get(x).size() >= Siz) return;
        buffer.get(x).add(packet);
    }
    //运行一段时间
    void work(int T)
    {
        int sum = 0; //表示包的总数
        int suc = 0; //表示成功到达的包的总数
        everySum = new int[T];
        everySuc = new int[T];
        Random random = new Random();
        for(int t = 0; t < T; t++)
        {
            //每个节点随机产生包
            for(int u = 1; u <= N ; u++)
            {
                if(random.nextInt(100) > 10) continue;
                int v = random.nextInt(N - 1) + 1;
                if(v >= u) ++v;
                addPacketInBuffer(new Packet(u, v, Math.max(N/3, 1), getRandomString(8)), u);
                ++sum;
            }
            //每个节点flood转发
            Stack<Pair<Packet,Integer>> stack = new Stack<>();
            for(int u = 1; u <= N; u++)
            {
                if(buffer.get(u).size() == 0) continue;
                Packet packet = buffer.get(u).get(0);
                buffer.get(u).remove(0);
                if(--packet.ttl <= 0) continue;
                for(int i = first[u]; i != 0; i = next[i])
                {
                    int v = to[i];
                    if(v == packet.t)
                    {
                        ++suc;
                        break;
                    }
                    stack.push(new Pair<>(packet,v));
                }
            }
            while(!stack.empty())
            {
                Pair<Packet,Integer> pair = stack.pop();
                addPacketInBuffer(pair.getKey(), pair.getValue());
            }
            everySum[t] = sum;
            everySuc[t] = suc;
        }
    }
}
