/**
 * Created by ASER on 14.10.2016.
 */

import org.zeromq.ZMQ;

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

        String serv = "";
        ServerDiscovery discovery = new ServerDiscovery(61488, REQ, RESP);
        serv = discovery.getServer();
        Client c1 = new Client(1000, "1", serv);
        Client c2 = new Client(250, "2", serv);
        Thread t1 = new Thread(c1);
        Thread t2 = new Thread(c2);
        t1.start();
        t2.start();
    }
}
