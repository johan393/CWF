/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;


/**
 *
 * @author Michael
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    
    public static int turn;
    public static int play;
    static public String[] ips;
    static boolean host = false;
    
    public static void connect() {

        
        try{
            int lobbycount = 2;
            String[] temp = new String[2];
            ips = new String[lobbycount];
            Socket sock = new Socket();
            InetAddress site = InetAddress.getByName("66.41.211.97");
            int port = 2150;
            
            SocketAddress sockaddress = new InetSocketAddress(site, port);
            
            sock.connect(sockaddress);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String buffer;
            
            //break the sequence up into stages
            
            buffer = in.readLine();//conneted print
            System.out.println(buffer);//should print "connected"
            
            buffer = in.readLine();
            if(buffer.equals("H")){
                host = true;
                System.out.println("host");
                for(int i=0;i<lobbycount; i++){
                    buffer =  in.readLine();
                    temp = buffer.split(":");
                    ips[i] = temp[0].substring(1);
                    System.out.println(ips[i]);
                }
                
                in.close();
                sock.close();
                //forward the necessary port to this pc
                
                GatewayDiscover discover = new GatewayDiscover();
                discover.discover();
                GatewayDevice d = discover.getValidGateway();
                if (null != d) {
                    System.out.println("found gateway");
                }
                else{
                    System.out.println("no gateway");
                }
                PortMappingEntry portMapping = new PortMappingEntry();
                /*if (!d.getSpecificPortMappingEntry(7124,"TCP",portMapping)) {
                    System.out.println("port already forwarded");
                }
                else{*/
                    if (!d.addPortMapping(7124, 7124,InetAddress.getLocalHost().getHostAddress(),"TCP","test")){
                        System.out.println("port forward suked");
                    }
                //}
                
                System.out.println("upnp con");
                //open connections for non-host clients
                
                
                Socket[] clients = new Socket[lobbycount];
                PrintWriter[] outs = new PrintWriter[lobbycount];
           
                
                ServerSocket hostSocket = new ServerSocket(7124,0,InetAddress.getLocalHost());
                
                for(int i = 0; i<lobbycount-1; i++){//subtract 1 due to him bein the host!
                    clients[i] = hostSocket.accept();
                    System.out.println("error here");
                    outs[i] = new PrintWriter(clients[i].getOutputStream());
                    outs[i].println("connected to host");
                    outs[i].flush();
                    

                }
                d.deletePortMapping(7124,"TCP");//all clients have connected
                while(true){
                    
                }
                
                
            }
            
            else{
                System.out.println("client");
                for(int i=0;i<lobbycount; i++){
                    buffer =  in.readLine();
                    temp = buffer.split(":");
                    ips[i] = temp[1].substring(1);
                    System.out.println(ips[i]);
                }

                while((buffer = in.readLine())!=null){
                    System.out.println("b" + buffer);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
   
    }
    
}
