/**
 * Created by ASER on 14.10.2016.
 */

import org.zeromq.ZMQ;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class JeroMQClient {

    public static boolean makeShake(String hello, String resp, ZMQ.Socket requester){
        boolean shakeSuc = false;
        requester.send(hello.getBytes(), 0);
        byte[] reply = requester.recv(0);
        if((new String(reply)).equals(resp)){
            System.out.println("Received: "+new String(reply));
            shakeSuc = true;
        }
        return shakeSuc;
    }

    private static final String REQ = "DISC_REQUEST";
    private static final String RESP = "DISC_RESP";

    public static void main(String[] args) {

        /*String hello = "shalom";
        String cmd = "{ 'a':'qq', 'b':'cmd' }";
        String resp = "jawohl";*/

        String serv = "";

        //service discovery
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.setBroadcast(true);
            byte[] sendData = REQ.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName("255.255.255.255"), 61488);
            datagramSocket.send(sendPacket);
            System.out.println("Sent to multi");
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()){
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
                if(networkInterface.isLoopback() || !networkInterface.isUp()){
                    if(networkInterface.isLoopback())
                        System.out.println("found loopback");
                    continue;
                }
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()){
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if(broadcast==null){
                        continue;
                    }
                    DatagramPacket sendPack = new DatagramPacket(sendData, sendData.length,
                            broadcast, 61488);
                    datagramSocket.send(sendPack);
                    System.out.println("Pack sent to: "+broadcast.getHostAddress());
                }
            }
            byte[] recvData = new byte[15000];
            DatagramPacket recvPacket = new DatagramPacket(recvData, recvData.length);
            datagramSocket.receive(recvPacket);
            System.out.println("resp: "+recvPacket.getAddress().getHostAddress());
            String msg = new String(recvPacket.getData()).trim();
            if(msg.equals(RESP)){
                serv = recvPacket.getAddress().getHostAddress();
                System.out.println("serv: "+serv);
            }
            datagramSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*Client c1 = new Client(1000, "1", serv);
        Client c2 = new Client(250, "2", serv);
        Thread t1 = new Thread(c1);
        Thread t2 = new Thread(c2);
        t1.start();
        t2.start();*/
    }
}
