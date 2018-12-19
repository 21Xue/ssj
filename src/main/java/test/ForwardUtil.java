package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by xyp on 18/12/18.
 */
public class ForwardUtil {

    private static final boolean debug = true;

    /**
     * @param input
     */
    public static void close(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            input = null;
        }
    }

    /**
     * @param output
     */
    public static void close(OutputStream output) {
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            output = null;
        }
    }

    /**
     * @param socket
     */
    public static void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
        }
    }

    /**
     * @param o
     */
    public static void debug(Object o) {
        if (debug) {
            System.out.println(o);
        }

    }

}
