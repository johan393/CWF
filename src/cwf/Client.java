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
import org.wetorrent.upnp.GatewayDevice;
import org.wetorrent.upnp.GatewayDiscover;
import org.wetorrent.upnp.PortMappingEntry;


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
    
    public static Socket[] connect() {
        Socket[] rsock = new Socket[1];
        
        try{
            int lobbycount = 4;
            String[] temp = new String[2];
            ips = new String[lobbycount];
            Socket sock = new Socket();
            InetAddress site = InetAddress.getByName("66.41.211.97");
            int port = 2150;
            
            SocketAddress sockaddress = new InetSocketAddress(site, port);
            
            sock.connect(sockaddress);//con to site
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
                sock.close();//done with server, we have our 3 other players
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
                
                
                Socket[] clients = new Socket[lobbycount-1];
                PrintWriter[] outs = new PrintWriter[lobbycount-1];
           
                
                ServerSocket hostSocket = new ServerSocket(7124,0,InetAddress.getLocalHost());
                
                for(int i = 0; i<lobbycount-1; i++){//subtract 1 due to him bein the host!
                    clients[i] = hostSocket.accept();
                }
                d.deletePortMapping(7124,"TCP");//all clients have connected
                System.out.println("port map deleted");
                rsock = clients;//pass client connections to game panel
                
            }
            
             else{
                System.out.println("client");
                for(int i=0;i<lobbycount; i++){
                    buffer =  in.readLine();
                    temp = buffer.split(":");
                    ips[i] = temp[0].substring(1);
                    System.out.println(ips[i]);
                }

                //close connection to site, try to connect to host
                in.close();
                sock.close();
                
                sock = new Socket();
                
                //InetAddress hostip = InetAddress.getByName(ips[0]);
                InetAddress hostip = InetAddress.getByName("66.41.211.97");
                System.out.println("trying to connect to " + ips[0]);
                SocketAddress hostaddress = new InetSocketAddress(hostip, 7124);
                System.out.println("connecting to host");
                Thread.sleep(6000);
                sock.connect(hostaddress);
                
                Socket[] hostsoc = new Socket[1];
                hostsoc[0] = sock;
                rsock = hostsoc;
                System.out.println("connected to host");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        return rsock;
        
    }
    
}
