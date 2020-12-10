import java.util.ArrayList;
import java.util.Random;

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
    private int[] everysum, everysuc; //每个时刻的sum和suc
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
    public int[] getEverysum() {return everysum;}
    public int[] getEverysuc() {return everysum;}
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
    public void addPacketInBuffer(Packet packet, int x)
    {
        if(buffer.get(x).size() >= Siz) return;
        buffer.get(x).add(packet);
    }
    //判断该点的buffer中是否有这个包
    public boolean isInBuffer(Packet packet, int x)
    {
        return buffer.get(x).contains(packet);
    }
    //某个点的buffer中删除包
    public void removePacketInBuffer(Packet packet, int x)
    {
        buffer.get(x).remove(packet);
    }
    //运行一段时间，并绘制出图像
    void work(int T)
    {
        int sum = 0; //表示包的总数
        int suc = 0; //表示成功到达的包的总数
        everysum = new int[T + 1];
        everysuc = new int[T + 1];
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
            for(int u = 1; u <= N; u++)
            {
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
                    addPacketInBuffer(packet, v);
                }
            }
            everysum[t] = sum;
            everysuc[t] = suc;
        }
    }
}
