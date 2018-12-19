package test;

/**
 * Created by xyp on 18/12/18.
 */
public class testssj {

    public static void main(String[] args) {
        TCPServer tcpServer = TCPServer.getInstance(3333, "172.247.33.169", 54400);
        tcpServer.listen();

    }
}
