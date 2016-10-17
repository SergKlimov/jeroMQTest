import org.zeromq.ZMQ;

/**
 * Created by ASER on 14.10.2016.
 */

public class JeroMQServer {

    private static boolean makeShake(String hello, String resp, ZMQ.Socket responder) {
        boolean shakeSuc = false;
        byte[] request = responder.recv(0);
        if ((new String(request)).equals(hello)) {
            System.out.println("Received: " + new String(request));
            shakeSuc = true;
        } else {
            System.out.println("Received bad: " + new String(request));
        }
        responder.send((new String(resp)).getBytes(), 0);
        return shakeSuc;
    }

    public static void main(String[] args) throws Exception {

        DiscoveryThread discoveryThread = DiscoveryThread.getInstance();
        Thread discover = new Thread(discoveryThread);
        discover.start();

        String hello = "shalom";
        String resp = "jawohl";

        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to clients
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:1488");
        //responder.bind("tcp://10.16.161.129:1488");

        while (!Thread.currentThread().isInterrupted()) {
            /*if (makeShake(hello, resp, responder)) {
                System.out.println("Succ");
                String reqStr = "";
                while (!reqStr.equals("bb")){
                    Thread.sleep(1000);
                    byte[] request = responder.recv(0);
                    System.out.println("Received cmd: " + new String(request));
                    responder.send((new String(request)).getBytes(), 0);
                    reqStr = new String(request);
                }
                *//*byte[] request = responder.recv(0);
                System.out.println("Received cmd: " + new String(request));
                responder.send((new String(request)).getBytes(), 0);*//*
            }*/

            byte[] request = responder.recv(0);
            System.out.println("Rcvd: " + new String(request));
            responder.send(resp.getBytes(), 0);
        }
        responder.close();
        context.term();
    }
}
