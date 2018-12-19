package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by xyp on 18/12/18.
 */
public class ClientThread extends Thread {

    private Socket client;
    private Socket forward;
    private InputStream clientInput;
    private OutputStream clientOutput;
    private InputStream forwardInput;
    private OutputStream forwardOutput;
    private boolean forwardActive = false;

//    private final String user = "xyp";

    private final String pwd = "fsn031792";

    public ClientThread(Socket client, Socket forward) {
        this.client = client;
        this.forward = forward;
        try {
            this.client.setKeepAlive(true);
            this.forward.setKeepAlive(true);
            this.clientInput = this.client.getInputStream();
            this.clientOutput = this.client.getOutputStream();
            this.forwardInput = this.forward.getInputStream();
            this.forwardOutput = this.forward.getOutputStream();
        } catch (IOException e) {
            this.close();
        }
    }



    public void run() {
        this.forwardActive = true;


        ForwardThread clientForward = new ForwardThread(this, this.clientInput, this.forwardOutput,"clientForward");
        clientForward.start();
        ForwardThread serverForward = new ForwardThread(this, this.forwardInput, this.clientOutput,"serverForward");
        serverForward.start();
        //============================//
        StringBuilder sb = new StringBuilder();
        sb.append("TCP Forwarding " + client.getInetAddress().getHostAddress() + ":" + client.getPort());
        sb.append(" <--> ");
        sb.append(forward.getInetAddress().getHostAddress() + ":" + forward.getPort());
        sb.append("  started.");
        ForwardUtil.debug(sb.toString());
        //============================//
    }

    public synchronized void close() {
        ForwardUtil.close(this.forward);
        ForwardUtil.close(this.client);
        if (this.forwardActive) {
            //============================//
            StringBuilder sb = new StringBuilder();
            sb.append("TCP Forwarding " + client.getInetAddress().getHostAddress() + ":" + client.getPort());
            sb.append(" <--> ");
            sb.append(forward.getInetAddress().getHostAddress() + ":" + forward.getPort());
            sb.append("  stopped.");
            ForwardUtil.debug(sb.toString());
            //============================//
            this.forwardActive = false;
        }
    }
}
