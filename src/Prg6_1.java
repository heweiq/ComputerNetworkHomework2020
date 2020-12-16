import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Prg6-1 Design and implement a chat system
 *  Design and implement a chat system that allows multiple groups of users to chat. A chat
 *  coordinator resides at a well-known network address, uses UDP for communication with chat
 *  clients, sets up chat servers for each chat session, and maintains a chat session directory.
 *  There is one chat server per chat session. A chat sever uses TCP for communication with clients.
 *  A chat client allows users to start, join, and leave a chat session. Design and implement the
 *  coordinator, server, and client code.
 */
public class Prg6_1
{
    private int UDPsend(String mess, InetAddress address, int port) throws SocketException
    {
        //System.out.println(mess + address + port);
        /** 1、建立udp socket端点 */
        DatagramSocket s = new DatagramSocket();
        /** 2、提供数据，封装打包  ---DatagramPacket(byte[] buf, int length, InetAddress address, int port)  */
        byte[] bs = mess.getBytes();
        DatagramPacket dp = new DatagramPacket(bs, bs.length, address, port);
        /** 3、使用send发送 */
        try {
            s.send(dp);
        }
        catch (IOException e) {
            System.out.println("发送失败： ");
            e.printStackTrace();
        }
        /** 4、关闭资源 */
        int ret = s.getLocalPort();
        s.close();
        return ret;
    }
    private DatagramPacket UDPaccept(int port)
    {
        try
        {
            /**1、建立udp socket，设置接收端口*/
            DatagramSocket ds = new DatagramSocket(port);
            /**2、预先创建数据存放的位置，封装*/
            byte[] bbuf = new byte[1024];
            DatagramPacket dp = new DatagramPacket(bbuf, bbuf.length);
            /**3、使用receive阻塞式接收*/
            ds.receive(dp);
            //System.out.println("ip::"+dp.getAddress().getHostAddress()+"\nport::"+dp.getPort()+"\ndata::"+new String(dp.getData()));
            /**4、关闭资源*/
            ds.close();
            return dp;
        }
        catch (IOException e) {
            System.out.println("发送失败： ");
            e.printStackTrace();
        }
        return null;
    }
    class Coordinator extends Thread
    {
        private static final int port = 100;

        private static final int MaxN = 100;
        private static final int MaxCnt = 80;
        private final Random random;
        private Map<Integer, Integer> userCounts;
        private ArrayList<String> sessionHosts;
        private ArrayList<Integer> sessionPorts;
        public Coordinator() throws IOException
        {
            random = new Random();
            userCounts = new HashMap<>();
            sessionHosts = new ArrayList<>();
            sessionPorts = new ArrayList<>();
            for(int i = 0; i < port; i++)
            {
                sessionHosts.add("127.0.0.1");
                sessionPorts.add(i);
            }
        }
        public void run()
        {
            while(true)
            {
                try
                {
                    DatagramPacket dp = UDPaccept(port);
                    int tmp;

                    String[] inString = new String(dp.getData()).replaceAll("(\\u0000)+$", "").split(" ");
                    String outString = "";
                    switch(inString[0])
                    {
                        case "start":
                            tmp = creatCharSession();
                            outString += tmp;
                            outString += " " + sessionHosts.get(tmp);
                            outString += " " + sessionPorts.get(tmp);
                            break;
                        case "join":
                            tmp = Integer.parseInt(inString[1]);
                            if(!userCounts.containsKey(tmp))
                                outString = "fail";
                            else
                            {
                                joinCharSession(tmp);
                                outString = "Success";
                                outString += " " + sessionHosts.get(tmp);
                                outString += " " + sessionPorts.get(tmp);
                            }
                            break;
                        case "leave":
                            tmp = Integer.parseInt(inString[1]);
                            leaveCharSession(tmp);
                            break;
                    }
                    if(!outString.equals(""))
                        UDPsend(outString, dp.getAddress(), dp.getPort());
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                    break;
                }
            }
        }
        private int creatCharSession()
        {
            if(userCounts.size() > MaxCnt) return -1;
            int ret = random.nextInt(MaxN);
            while(userCounts.containsKey(ret))
                ret = random.nextInt(MaxN);
            userCounts.put(ret, 1);
            return ret;
        }
        private void joinCharSession(int id)
        {
            if(!userCounts.containsKey(id)) return;
            userCounts.put(id, userCounts.get(id) + 1);
        }
        private void leaveCharSession(int id)
        {
            if(!userCounts.containsKey(id)) return;
            userCounts.put(id, userCounts.get(id) - 1);
            if(userCounts.get(id) == 0)
            {
                userCounts.remove(id);
            }
        }
    }
    class Server extends Thread
    {
        private int port;
        private ServerSocket serverSocket;

        private static final int MaxMessCnt = 30;
        private ArrayList<String> messages;
        public Server(int port) throws IOException
        {
            serverSocket = new ServerSocket(port);
            this.port = port;
            messages = new ArrayList<>();
        }
        public void run()
        {
            while(true)
            {
                try
                {
                    //System.out.println("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...");
                    Socket server = serverSocket.accept();
                    //System.out.println("远程主机地址：" + server.getRemoteSocketAddress());
                    DataInputStream in = new DataInputStream(server.getInputStream());
                    //System.out.println(in.readUTF());
                    String inString = in.readUTF();
                    String outString = "";
                    switch(inString)
                    {
                        case "send":
                            String user, mess;
                            user = in.readUTF();
                            mess = in.readUTF();
                            send(user,mess);
                            break;
                        case "check":
                            outString = check();
                    }
                    DataOutputStream out = new DataOutputStream(server.getOutputStream());
                    if(!outString.equals(""))
                        out.writeUTF(outString);
                    server.close();
                }
                catch(SocketTimeoutException s)
                {
                    System.out.println("Socket timed out!");
                    break;
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                    break;
                }
            }
        }
        public void send(String user, String mess)
        {
            messages.add(user + ":\r\n" + mess + "\r\n");
            if(messages.size() > MaxMessCnt) messages.remove(0);
        }
        public String check()
        {
            String ret = new String();
            for(int i = 0; i < messages.size(); i++)
                ret += messages.get(i);
            return ret;
        }
    }
    class Client
    {
        private String user;
        private int id;
        private String serverHost;
        private int serverPort;
        private final static String coordinatorHost = "127.0.0.1";
        private final static int coordinatorPort = 100;
        Client(String user)
        {
            this.user = user;
            id = -1;
            serverHost = "";
            serverPort = -1;
        }
        public int getId()
        {
            return id;
        }
        public void start()
        {
            if(this.id != -1) leave();
            try
            {
                int tmp = UDPsend("start",InetAddress.getByName(coordinatorHost),coordinatorPort);
                DatagramPacket dp = UDPaccept(tmp);
                String str[] = new String(dp.getData()).replaceAll("(\\u0000)+$", "").split(" ");
                id = Integer.parseInt(str[0]);
                if(id != -1)
                {
                    serverHost = str[1];
                    serverPort = Integer.parseInt(str[2]);
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        public void join(int id)
        {
            if(this.id != -1) leave();
            try
            {
                int tmp = UDPsend("join" + " " + id,InetAddress.getByName(coordinatorHost),coordinatorPort);
                DatagramPacket dp = UDPaccept(tmp);
                String str[] = new String(dp.getData()).replaceAll("(\\u0000)+$", "").split(" ");
                if(str[0].equals("Success"))
                {
                    serverHost = str[1];
                    serverPort = Integer.parseInt(str[2]);
                    this.id = id;
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        public void leave()
        {
            try
            {
                UDPsend("leave" + " " + id,InetAddress.getByName(coordinatorHost),coordinatorPort);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            id = -1;
            serverHost = "";
            serverPort = -1;
        }
        public void send(String mess)
        {
            if(serverPort == -1 || serverHost.equals(""))
                return;
            try
            {
                Socket socket = new Socket(serverHost,serverPort);
                OutputStream outToServer = socket.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF("send");
                out.writeUTF(user);
                out.writeUTF(mess);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        public String check()
        {
            if(serverPort == -1 || serverHost.equals(""))
                return null;
            try
            {
                Socket socket = new Socket(serverHost,serverPort);
                OutputStream outToServer = socket.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF("check");

                InputStream inFromServer = socket.getInputStream();
                DataInputStream in = new DataInputStream(inFromServer);
                return in.readUTF();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}