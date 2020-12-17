import javafx.util.Pair;

import java.util.*;

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
    private int[][] d; //距离
    private int sum, suc, err;
    class Packet
    {
        boolean ack;
        int s,t,ttl,statT;
        String mess;
        Packet(int statT, int s, int t, int ttl, String mess)
        {
            this.ack = false;
            this.statT = statT; this.s = s; this.t = t; this.ttl = ttl; this.mess = mess;
        }
        Packet copy()
        {
            Packet packet = new Packet(statT,s,t,ttl,mess);
            packet.ack = ack;
            return packet;
        }
    }
    public int getSum() {return sum;}
    public int getSuc() {return suc;}
    public int getErr() {return err;}
    ArrayList<ArrayList<Packet>> buffer;
    public Prg5_2(int n,int siz)
    {
        N = n;
        Siz = siz;
        tot = 2; //从2开始计数，避开first的初值0
        first = new int[n + 1];
        next = new int[2 + (n * (n - 1) << 1)];
        to = new int[2 + (n * (n - 1) << 1)];
        d = new int[n + 1][n + 1];
        buffer = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            buffer.add(new ArrayList<>());
    }
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
    //跑最短路
    public void Floyed()
    {
        for(int i = 1; i <= N; i++)
            for(int j = 1; j <= N; j++)
                d[i][j] = N + 1;
        int u, v;
        for(int i = 2; i < tot; i++)
        {
            v = to[i];
            u = to[i ^ 1];
            d[u][v] = 1;
        }
        for(int i = 1; i <= N; i++)
            d[i][i] = 0;
        for(int k = 1; k <= N; k++)
            for(int i = 1; i <= N; i++)
                for(int j = 1; j <= N; j++)
                    if(d[i][k] + d[k][j] < d[i][j])
                        d[i][j] = d[i][k] + d[k][j];
    }
    //某个点的buffer中加入包
    private boolean addPacketInBuffer(Packet packet, int x)
    {
        if(buffer.get(x).size() >= Siz) return false;
        buffer.get(x).add(packet);
        return true;
    }
    //运行一段时间
    void work(int T, int interval)
    {
        sum = 0; //表示包的总数
        suc = 0; //表示成功到达的包的总数
        err = 0; //表示buffer里溢出的数量
        Random random = new Random();
        Set<Packet> set = new HashSet<>();
        for(int t = 0; t < T; t++)
        {
            //重发包
            Stack<Packet> packets = new Stack<>();
            for(Packet packet: set)
                if(packet.statT + interval == t)
                {
                    packets.push(packet);
                    set.add(packet);
                    if(!addPacketInBuffer(packet, packet.s))
                        ++err;
                    ++sum;
                }
            while(!packets.empty())
                set.remove(packets.pop());
            //每个节点随机产生包
            for(int u = 1; u <= N ; u++)
            {
                if(random.nextInt(100) > 10) continue;
                int v = random.nextInt(N - 1) + 1;
                if(v >= u) ++v;
                Packet packet = new Packet(t, u, v, 5, getRandomString(8));
                set.add(packet);
                if(!addPacketInBuffer(packet, u))
                    ++err;
                ++sum;
            }
            //每个节点(k优)flood转发
            Stack<Pair<Packet,Integer>> stack = new Stack<>();
            for(int u = 1; u <= N; u++)
            {
                if(buffer.get(u).size() == 0) continue;
                Packet packet = buffer.get(u).get(0);
                buffer.get(u).remove(0);
                if(--packet.ttl <= 0) continue;
                //泛洪 only the (statically chosen) best k lines are flooded--------------------------
                ArrayList<Integer> arrayList = new ArrayList<>();
                for(int i = first[u]; i != 0; i = next[i])
                    arrayList.add(to[i]);
                int[] array = new int[arrayList.size()];
                for(int i = 0; i < array.length; i++) array[i] = arrayList.get(i);
                Arrays.sort(array);
                for(int tmp,i = 0; i < array.length; i++)
                    for(int j = i + 1; j < array.length; j++)
                        if(d[array[j]][packet.t] < d[array[i]][packet.t])
                        {
                            tmp = array[j];
                            array[j] = array[i];
                            array[i] = tmp;
                        }
                //-------------------------------------------------------------------------------------
                for(int i = 0; i < Math.min(1, array.length); i++)
                {
                    int v = array[i];
                    if(v == packet.t)
                    {
                        if(packet.ack)
                            ++suc;
                        else
                        {
                            Packet ackPacket = packet.copy();
                            ackPacket.ack = true;
                            ackPacket.ttl = Math.max(N/3, 1);
                            stack.push(new Pair<>(ackPacket, v));
                        }
                        break;
                    }
                    stack.push(new Pair<>(packet,v));
                }
            }
            while(!stack.empty())
            {
                Pair<Packet,Integer> pair = stack.pop();
                if(!addPacketInBuffer(pair.getKey(), pair.getValue()))
                    ++err;
            }
        }
    }
}
