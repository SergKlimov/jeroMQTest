import java.io.IOException;
import java.net.*;

/**
 * Created by ASER on 17.10.2016.
 */
public class DiscoveryThread implements Runnable {
    private static DiscoveryThread instance;

    private static final String REQ = "DISC_REQUEST";
    private static final String RESP = "DISC_RESP";

    public static DiscoveryThread getInstance() {
        if(instance==null){
            instance = new DiscoveryThread();
        }
        return instance;
    }

    private DatagramSocket socket;

    public void run() {
        try {
            socket = new DatagramSocket(61488, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            while (true){
                System.out.println("Rcv broadcast");
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);
                System.out.println("Discovery packett recved: "+new String(packet.getData()));
                String msg = new String(packet.getData()).trim();
                if(msg.equals(REQ)){
                    byte[] sendData = RESP.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData,
                            sendData.length,
                            packet.getAddress(),
                            packet.getPort());
                    socket.send(sendPacket);
                    System.out.println("Sent packet to "+sendPacket.getAddress().getHostAddress());
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
