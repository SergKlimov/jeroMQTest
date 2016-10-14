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

    public static void main(String[] args){

        String hello = "shalom";
        String cmd = "{ 'a':'qq', 'b':'cmd' }";
        String resp = "jawohl";

        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Connecting to hello world server…");

        ZMQ.Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://localhost:1488");

        if(makeShake(hello, resp, requester)){
            for (int i = 0;i<3;i++){
                requester.send(cmd.getBytes(), 0);
                byte[] reply = requester.recv(0);
            }
            requester.send(new String("bb").getBytes(), 0);
            byte[] reply = requester.recv(0);
        }

        requester.close();
        context.term();
    }
}
