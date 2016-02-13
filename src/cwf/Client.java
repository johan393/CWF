/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;


/**
 *
 * @author Michael
 */
public class Client extends JPanel{

    /**
     * @param args the command line arguments
     */
    
    public static int turn;
    public static int play;
    static public String[] ips;
    static boolean host = false;
    Image bg;
    
    JLabel status;
    
    public Client(){;
        status = new JLabel("Connecting to Server...");
        bg = new ImageIcon("themes\\" + MainFrame.bgtheme + "\\bg.png").getImage();
        
        Color color;
        try{
        Field field = Class.forName("java.awt.Color").getField(MainFrame.buttonColor);
        color = (Color)field.get(null);
        }
        catch(Exception e){
        color = null;
        }
        status.setForeground(color);
        status.setPreferredSize(new Dimension(400,100));
        status.setFont(new Font("Times New Roman", 0 , 28));
        this.add(status, BorderLayout.NORTH);
        this.setVisible(true);
    }
    
    public Socket[] connect() {
        this.repaint();
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
            status.setText("Searching for other players...");
            this.revalidate();

            
            buffer = in.readLine();
            if(buffer.equals("H")){
                host = true;
                System.out.println("host");
                for(int i=0;i<lobbycount; i++){
                    buffer =  in.readLine();
                    temp = buffer.split(":");
                    ips[i] = temp[0].substring(1);
                    System.out.println(ips[i]);
                    status.setText("Found game, awaiting clients...");
                    this.revalidate();

                }
                
                in.close();
                sock.close();//done with server, we have our 3 other players
                
                //forward the necessary port to this pc
                UpnpServiceImpl upnpService = new UpnpServiceImpl(new PortMappingListener(new PortMapping(7124,InetAddress.getLocalHost().getHostAddress(),PortMapping.Protocol.TCP,"My Port Mapping")));
                upnpService.getControlPoint().search(); 
                
                System.out.println("upnp con");
                //open connections for non-host clients
                
                
                Socket[] clients = new Socket[lobbycount-1];
                PrintWriter[] outs = new PrintWriter[lobbycount-1];
           
                
                ServerSocket hostSocket = new ServerSocket(7124,0,InetAddress.getLocalHost());
                
                for(int i = 0; i<lobbycount-1; i++){//subtract 1 due to him bein the host!
                    clients[i] = hostSocket.accept();
                }
                upnpService.shutdown();//all clients have connected, remove port forwarding.......................................................
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
                    status.setText("Found game, awaiting host...");
                    this.revalidate();
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
   public void paintComponent(Graphics g){
     super.paintComponent(g);
     g.drawImage(bg,0,0,null);

   }
    
}
