import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *  Write a program to simulate routing using flooding. Each packet should contain a counter that is
 *  decremented on each hop. When the counter gets to zero, the packet is discarded. Time is
 *  discrete, with each line handling one packet per time interval. Make three versions of the
 *  program: all lines are flooded, all lines except the input line are flooded, and only the
 *  (statically chosen) best k lines are flooded. Compare flooding with deterministic routing
 *  ( k = 1 ) in terms of both delay and the bandwidth used.
 */
public class Prg5_1
{
    private int N; //表示点的数量
    private int tot; //邻接表边数
    private int bandwidth; //带宽，每一次转发都算
    private int[] first, next, to; //邻接表
    private int[][] d; //距离
    public Prg5_1(int n)
    {
        N = n;
        tot = 2; //从2开始计数，避开first的初值0
        first = new int[n + 1];
        next = new int[2 + (n * (n - 1) << 1)];
        to = new int[2 + (n * (n - 1) << 1)];
        d = new int[n + 1][n + 1];
        setBandwidth();
    }
    public void setBandwidth()
    {
        bandwidth = 0;
    }
    public int getBandwidth()
    {
        return bandwidth;
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
    //函数递归，返回delay，发不出去则返回-1
    //1. all lines are flooded
    public int flood1(int counter, int s, int t)
    {
        if(counter == 0) return -1;
        if(s == t)
        {
            ++bandwidth;
            return 0;
        }
        int ret = -1;
        for(int i = first[s]; i != 0; i = next[i])
        {
            int v = to[i];
            int delay = flood1(counter - 1, v, t) + 1;
            if(delay == 0) continue;
            if(ret == -1) ret = delay;
            else if(delay < ret) ret = delay;
        }
        return ret;
    }
    //2. all lines except the input line are flooded
    public int flood2(int counter, int s, int t)
    {
        return flood2(counter, s, t, -1);
    }
    public int flood2(int counter, int s, int t, int lastP)
    {
        if(counter == 0) return -1;
        if(s == t)
        {
            ++bandwidth;
            return 0;
        }
        int ret = -1;
        for(int i = first[s]; i != 0; i = next[i])
        {
            int v = to[i];
            if(v != lastP)
            {
                int delay = flood2(counter - 1, v, t, s) + 1;
                if (delay == 0) continue;
                if (ret == -1) ret = delay;
                else if (delay < ret) ret = delay;
            }
        }
        return ret;
    }
    //3. only the (statically chosen) best k lines are flooded
    public int flood3(int counter, int s, int t, int k)
    {
        if(counter == 0) return -1;
        if(s == t)
        {
            ++bandwidth;
            return 0;
        }
        int ret = -1;
        ArrayList<Integer> arrayList = new ArrayList<>();
        for(int i = first[s]; i != 0; i = next[i])
            arrayList.add(to[i]);
        int[] array = new int[arrayList.size()];
        for(int i = 0; i < array.length; i++) array[i] = arrayList.get(i);
        Arrays.sort(array);
        for(int tmp,i = 0; i < array.length; i++)
            for(int j = i + 1; j < array.length; j++)
                if(d[array[j]][t] < d[array[i]][t])
                {
                    tmp = array[j];
                    array[j] = array[i];
                    array[i] = tmp;
                }
        for(int i = 0; i < Math.min(k, array.length); i++)
        {
            int delay = flood3(counter - 1, array[i], t, k) + 1;
            if(delay == 0) continue;
            if(ret == -1) ret = delay;
            else if(delay < ret) ret = delay;
        }
        return ret;
    }
}
