import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 * Created by ASER on 18.10.2016.
 */
public class ServerDiscovery {

    private int servPort;
    private String request;
    private String response;

    public ServerDiscovery(int servPort, String request, String response) {
        this.servPort = servPort;
        this.request = request;
        this.response = response;
    }

    public String getServer(){
        String address = "";

        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.setBroadcast(true);
            byte[] sendData = request.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(
                    sendData,
                    sendData.length,
                    InetAddress.getByName("255.255.255.255"),
                    servPort);
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
                            broadcast, servPort);
                    datagramSocket.send(sendPack);
                    System.out.println("Pack sent to: "+broadcast.getHostAddress());
                }
            }
            byte[] recvData = new byte[15000];
            DatagramPacket recvPacket = new DatagramPacket(recvData, recvData.length);
            datagramSocket.receive(recvPacket);
            System.out.println("resp: "+recvPacket.getAddress().getHostAddress());
            String msg = new String(recvPacket.getData()).trim();
            if(msg.equals(response)){
                address = recvPacket.getAddress().getHostAddress();
                System.out.println("serv: "+address);
            }
            datagramSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }
}
