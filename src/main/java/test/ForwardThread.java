package test;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * Created by xyp on 18/12/18.
 */
public class ForwardThread extends Thread {

    private static final int BUFFER_SIZE = 8192;

    private InputStream input;
    private OutputStream output;
    ClientThread parent;

    private String threadName;


    public ForwardThread(ClientThread parent, InputStream input, OutputStream output, String threadName) {
        this.parent = parent;
        this.input = input;
        this.output = output;
        this.threadName = threadName;
    }

    public void run() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                int bytesRead = this.input.read(buffer);
                if (bytesRead == -1) {
                    break;
                }

//                StringWriter writer = new StringWriter();
//                IOUtils.copy(input, writer, "utf8");
                String theString = new String(buffer);
                System.out.print(threadName + "----" + theString);

                this.output.write(buffer, 0, bytesRead);
                this.output.flush();
            }
        } catch (IOException e) {

        }
        this.parent.close();
    }

}
