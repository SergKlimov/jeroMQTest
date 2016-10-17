import org.zeromq.ZMQ;

/**
 * Created by ASER on 14.10.2016.
 */
public class Client implements Runnable {

    private String name;
    private int sleep;
    private String serv;

    public Client(int sleep, String name, String serv) {
        this.sleep = sleep;
        this.name = name;
        this.serv = serv;
    }

    public void run() {

        String hello = "shalom "+name;
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Connecting " +name+ " to hello world server…");
        ZMQ.Socket requester = context.socket(ZMQ.REQ);
        //requester.connect("tcp://10.16.161.78:1488");
        requester.connect("tcp://"+serv+":1488");

        for (int i = 0; i < 20; i++){
            requester.send(hello, 0);
            byte[] reply = requester.recv(0);
            System.out.println(name+" Recvd: "+new String(reply));
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        requester.close();
        context.term();
    }
}