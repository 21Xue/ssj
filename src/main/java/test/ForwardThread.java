package test;

import org.apache.commons.io.IOUtils;
import test.rc4.ICrypt;
import test.rc4.RC4;

import java.io.*;
import java.util.List;

/**
 * Created by xyp on 18/12/18.
 */
public class ForwardThread extends Thread {

    private static final int BUFFER_SIZE = 8192;

    private InputStream input;
    private OutputStream output;
    ClientThread parent;

    private ICrypt _crypt = new RC4("", "123");

    private Socks5Proxy _proxy = new Socks5Proxy();

    private String threadName;


    public ForwardThread(ClientThread parent, InputStream input, OutputStream output, String threadName) {
        this.parent = parent;
        this.input = input;
        this.output = output;
        this.threadName = threadName;
    }


    private boolean _sendLocal(byte[] data, int length) {
        try {
            OutputStream outStream = output;
            outStream.write(data, 0, length);
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    public void run() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];

            List<byte[]> sendData = null;

            while (true) {

                int bytesRead = this.input.read(buffer);
                if (bytesRead == -1) {
                    break;
                }

                if (!_proxy.isReady()) {
                    byte[] temp;
                    buffer = new byte[bytesRead];

                    // dup dataBuffer to use in later
                    System.arraycopy(buffer, 0, buffer, 0, bytesRead);

                    temp = _proxy.getResponse(buffer);
                    if ((temp != null) && (!_sendLocal(temp, temp.length))) {
                        throw new IOException("Local socket closed (proxy-Write)!");
                    }
                    // packet for remote socket
                    sendData = _proxy.getRemoteResponse(buffer);
                    if (sendData == null) {
                        continue;
                    }
                } else {
                    sendData.clear();
                    sendData.add(buffer);
                }


//                StringWriter writer = new StringWriter();
//                IOUtils.copy(input, writer, "utf8");
//                String theString = new String(buffer);
//                System.out.print(threadName + "----" + theString);

                for (byte[] bytes : sendData) {

                    ByteArrayOutputStream _remoteOutStream=new ByteArrayOutputStream(16384);

                    _crypt.encrypt(bytes, bytes.length, _remoteOutStream);


                    byte[] sendData1 = _remoteOutStream.toByteArray();

                    this.output.write(buffer, 0, sendData1.length);
                }
                this.output.flush();
            }
        } catch (IOException e) {

        }
        this.parent.close();
    }

}
