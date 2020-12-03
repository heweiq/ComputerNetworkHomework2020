/*
Prg5-1 flooding
 Write a program to simulate routing using flooding. Each packet should contain a counter that is
 decremented on each hop. When the counter gets to zero, the packet is discarded. Time is
 discrete, with each line handling one packet per time interval. Make three versions of the
 program: all lines are flooded, all lines except the input line are flooded, and only the
 (statically chosen) best k lines are flooded. Compare flooding with deterministic routing
 ( k = 1 ) in terms of both delay and the bandwidth used.
 */
public class Prg5_1
{
    private int N; //表示点的数量
    private int tot; //邻接表边数
    private int[] first, next, to; //邻接表
    public Prg5_1(int n)
    {
        N = n;
        tot = 2; //从2开始计数，避开first的初值0
        first = new int[n];
        next = new int[n * (n - 1) << 1];
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
    //1. all lines are flooded
    public void flood1()
    {

    }
}
