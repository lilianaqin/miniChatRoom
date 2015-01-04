package Client;
import java.io.*;
import java.net.*;
import java.util.*;
 
public class MiniChatServer {
    boolean started = false;
    ServerSocket ss = null;
 
    List<Client> clients = new ArrayList<Client>();
    public static void main(String[] args) {
        new MiniChatServer().start();
    }
 
    public void start() {
        try {
            ss = new ServerSocket(8888);
            started = true;
            System.out.println("端口已开启,占用8888端口号....");
        } catch (BindException e) {
            System.out.println("端口使用中....");
            System.out.println("请关掉相关程序并重新运行服务器！");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        try {
 
            while (started) {
                Socket s = ss.accept();
                
                
                Client c = new Client(s);
                
                System.out.println("a client connected!");
                new Thread(c).start();
                if (c.bDuplicated == false)
                {
                	clients.add(c);
                	c.SendUserList();
                	BroadcastNewUser(c.UserName);
                } else {
                	//c.OS.writeUTF("!Duplicated\n");
            		//c.OS.flush();
                	stc info = new stc();
                	info.type = "command";
                	info.content = "Duplicated";
                	c.send(info);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void BroadcastNewUser(String UN)
    {
    	Iterator<Client> iter = clients.iterator();
    	Client c;
    	stc info = new stc();
    	info.type = "command";
    	info.content = "AddUser";
    	info.UserNameList = new String[1];
    	info.UserNameList[0] = UN;
    	while (iter.hasNext())
    	{
    		c = iter.next();
    		c.send(info);
    	}
    	
    }
    
    protected void BroadcastDeleteUser(String UN)
    {
    	Iterator<Client> iter = clients.iterator();
    	Client c;
    	stc info = new stc();
    	info.type = "command";
    	info.content = "DeleteUser";
    	info.UserNameList = new String[1];
    	info.UserNameList[0] = UN;
    	while (iter.hasNext())
    	{
    		c = iter.next();
    		c.send(info);
    	}
    }
    
    class Client implements Runnable {
        protected Socket s;
        //private DataInputStream IS = null;
        //private DataOutputStream OS = null;
        protected ObjectInputStream OIS = null;
        protected ObjectOutputStream OOS = null;
        protected boolean bConnected = false;
        protected boolean bDuplicated = false;
        public String UserName;
        
        public Client(Socket s) {
            this.s = s;
            try {
                //IS = new DataInputStream(s.getInputStream());
                //OS = new DataOutputStream(s.getOutputStream());
                OIS = new ObjectInputStream(s.getInputStream());
                OOS = new ObjectOutputStream(s.getOutputStream());
                //UserName = IS.readUTF();
                Object obj = OIS.readObject();
                
                HandleReceiveInfo((cts)obj);
                /*
                Iterator<Client> iter = clients.iterator();
                Client c;
                while (iter.hasNext())
                {
                	c = iter.next();
                	if (c.UserName.equals(UserName) == true)
                	{
                		
                		//BroadcastDeleteUser(UserName);
                		bDuplicated = true;
                	}
                }*/
                bConnected = true;
                System.out.println("UserName:"+UserName);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
            	e.printStackTrace();
            }
        }
 
        private void SendUserList()
        {
        	Iterator<Client> iter = clients.iterator();
        	Client c;
        	stc info = new stc();
        	info.type = "command";
        	info.content = "UserNameList";
        	info.UserNameList = new String[clients.size()-1];
        	int i = 0;
        	while (iter.hasNext())
        	{
        		c = iter.next();
        		if (iter.hasNext())
        			info.UserNameList[i++] = c.UserName;
        	}
        	this.send(info);
        	/*this.send("!UserNameList\n");
        	while (iter.hasNext())
        	{
        		c = iter.next();
        		if (iter.hasNext())
        			this.send(c.UserName);
        	}
        	this.send("!UserNameListEnd\n");
        	*/
        }
        public void send(stc info) {
            try {
                //OS.writeUTF(str);
                //OS.flush();
            	OOS.writeObject(info);
            	OOS.flush();
            } catch (IOException e) {
                if (clients.remove(this) == true)
                	BroadcastDeleteUser(this.UserName);
                System.out.println("对方退出了！我从List里面去掉了！");
            }
        }
        public void HandleReceiveInfo(cts CTS)
        {
        	if (CTS.tusername != null)
        	{
        		if (CTS.type.equals("txt") == true)
        		{
        			stc info = new stc();
	                info.type = "txt";
	                info.username = CTS.username;
	                info.txt = CTS.txt;
	                info.tusername = CTS.tusername;
	                Iterator<Client> iter = clients.iterator();
	            	Client c;
	            	if (info.txt != null)
	            		System.out.println("txt received.");
	            	while (iter.hasNext())
	            	{
	            		c = iter.next();
	            		if (c.UserName.equals(CTS.tusername) || c.UserName.equals(CTS.username)) {
	            			c.send(info);
	            		}
	            	}
        		} else if (CTS.type.equals("image") == true)
        		{
        			System.out.println("收到私聊图片");
	        		stc info = new stc();
	        		info.type = "image";
	        		info.username = CTS.username;
	        		info.tusername = CTS.tusername;
	        		info.image = CTS.image;
	        		Iterator<Client> iter = clients.iterator();
	            	Client c;
	            	while (iter.hasNext())
	            	{
	            		c = iter.next();
	            		if (c.UserName.equals(CTS.tusername) || c.UserName.equals(CTS.username)) {
	            			c.send(info);
	            		}
	            	}
        		}
        	} else {
        		if (CTS.type.equals("username") == true)
	        	{
	        		UserName = CTS.content;
	        		Iterator<Client> iter = clients.iterator();
	                Client c;
	                while (iter.hasNext())
	                {
	                	c = iter.next();
	                	if (c.UserName.equals(UserName) == true)
	                	{
	                		
	                		//BroadcastDeleteUser(UserName);
	                		bDuplicated = true;
	                	}
	                }
	        	} else if (CTS.type.equals("txt") == true )
	        	{
	                stc info = new stc();
	                info.type = "txt";
	                info.username = CTS.username;
	                info.txt = CTS.txt;
	                Iterator<Client> iter = clients.iterator();
	            	Client c;
	            	while (iter.hasNext())
	            	{
	            		c = iter.next();
	            		c.send(info);
	            	}
	        	} else if (CTS.type.equals("image") == true)
	        	{
	        		System.out.println("收到图片");
	        		stc info = new stc();
	        		info.type = "image";
	        		info.username = CTS.username;
	        		info.image = CTS.image;
	        		Iterator<Client> iter = clients.iterator();
	            	Client c;
	            	while (iter.hasNext())
	            	{
	            		c = iter.next();
	            		c.send(info);
	            	}
	        	}
        	}
        }
        public void run() {
            try {
                while (bConnected) {
                	
                    //String str = IS.readUTF();
                	Object obj = OIS.readObject();
                	System.out.println("received");
                	if (obj!=null) HandleReceiveInfo((cts)obj);
                	
                    //System.out.println((String)obj);
                    /*for (int i = 0; i < clients.size(); i++) {
                        Client c = clients.get(i);
                        
                        c.send(str);
                    }*/
                    
                }
            } catch (EOFException e) {
                System.out.println("Client closed!");
                if (clients.remove(this) == true)
                	BroadcastDeleteUser(this.UserName);
                System.out.println("对方退出了！我从List里面去掉了！");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e){
            	
            }
            finally {
                try {
                    if (OIS != null)
                        OIS.close();
                    if (OOS != null)
                        OOS.close();
                    if (s != null) {
                        s.close();
                    }
 
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
 
            }
        }
 
    }
    
}