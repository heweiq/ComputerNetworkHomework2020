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
    private Coordinator coordinator = new Coordinator();
    private class Coordinator
    {
        private boolean isFunc = false;
        private static final int MaxN = 100000;
        private static final int MaxCnt = 80000;
        private final Random random = new Random();
        private Map<Integer, Integer> userCounts = new HashMap<>();
        private Map<Integer, Server> charSessions = new HashMap<>();
        public Server getServer(int id)
        {
            while(isFunc == true); //盲等
            isFunc = true;
            if(!charSessions.containsKey(id)) return null;
            isFunc = false;
            return charSessions.get(id);
        }
        public int creatCharSession()
        {
            while(isFunc == true); //盲等
            isFunc = true;
            if(charSessions.size() > MaxCnt) return -1;
            int ret = random.nextInt(MaxN);
            while(charSessions.containsKey(ret))
                ret = random.nextInt(MaxN);
            userCounts.put(ret, 1);
            charSessions.put(ret, new Server());
            isFunc = false;
            return ret;
        }
        public void joinCharSession(int id)
        {
            while(isFunc == true); //盲等
            isFunc = true;
            if(!charSessions.containsKey(id)) return;
            userCounts.put(id, userCounts.get(id) + 1);
            isFunc = false;
        }
        public void leaveCharSession(int id)
        {
            while(isFunc == true); //盲等
            isFunc = true;
            if(!charSessions.containsKey(id)) return;
            userCounts.put(id, userCounts.get(id) - 1);
            if(userCounts.get(id) == 0)
            {
                userCounts.remove(id);
                charSessions.remove(id);
            }
            isFunc = false;
        }
    }
    private class Server
    {
        private boolean isFunc;
        private static final int MaxMessCnt = 30;
        private ArrayList<String> messages;
        public Server()
        {
            isFunc = false;
            messages = new ArrayList<>();
        }
        public void send(String user, String mess)
        {
            while(isFunc == true); //盲等
            isFunc = true;
            messages.add(user + ":\r\n" + mess + "\r\n");
            if(messages.size() > MaxMessCnt) messages.remove(0);
            isFunc = false;
        }
        public String check()
        {
            while(isFunc == true); //盲等
            isFunc = true;
            String ret = new String();
            for(int i = 0; i < messages.size(); i++)
                ret += messages.get(i);
            isFunc = false;
            return ret;
        }
    }
    public class Client
    {
        private String user;
        private int id;
        private Server server;
        Client(String user)
        {
            this.user = user;
            id = -1;
            server = null;
        }
        public int getId()
        {
            return id;
        }
        public void start()
        {
            if(this.id != -1) leave();
            id = coordinator.creatCharSession();
            if(id != -1)
                server = coordinator.getServer(id);
        }
        public void join(int id)
        {
            if(this.id != -1) leave();
            if(coordinator.getServer(id) != null)
            {
                coordinator.joinCharSession(id);
                this.id = id;
                server = coordinator.getServer(id);
            }
        }
        public void leave()
        {
            coordinator.leaveCharSession(id);
            id = -1;
            server = null;
        }
        public void send(String mess)
        {
            server.send(user, mess);
        }
        public String check()
        {
            return server.check();
        }
    }
}