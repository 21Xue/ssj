package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xyp on 18/12/18.
 */
public class TCPServer {

    private static TCPServer tcp;
    private String forwardIP;
    private int forwardPort;
    private ServerSocket server ;

    private TCPServer(int port,String forwardIP,int forwardPort){
        this.forwardIP = forwardIP;
        this.forwardPort = forwardPort;
        try {
            this.server = new ServerSocket(port, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen(){
        new Thread(new Runnable(){

            public void run() {
                while (true) {
                    try {
                        Socket client = server.accept();
                        Socket forward = new Socket(forwardIP,forwardPort);
                        ClientThread clientThread = new ClientThread(client,forward);
                        clientThread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch(NullPointerException e){
                        break;
                    }
                }
            }}).start();

    }

    public void close(){
        if(this.server!=null){
            try {
                this.server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.server = null;
        }
    }

    /**
     * @param port
     * @param forwardIP
     * @param forwardPort
     * @return
     */
    public static TCPServer getInstance(int port,String forwardIP,int forwardPort){
        if(tcp==null){
            synchronized(TCPServer.class){
                if(tcp==null){
                    tcp = new TCPServer(port,forwardIP,forwardPort);
                }
            }
        }
        return tcp;
    }

    /**
     * @param port
     * @param forwardIP
     * @param forwardPort
     * @return
     */
    public static TCPServer createInstance(int port,String forwardIP,int forwardPort){
        return new TCPServer(port,forwardIP,forwardPort);
    }

}
