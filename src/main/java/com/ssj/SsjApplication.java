package com.ssj;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication
public class SsjApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsjApplication.class, args);
        //为了简单起见，所有的异常信息都往外抛
        try {
            //创建一个服务器对象，端口3333
            ServerSocket serverSocket = new ServerSocket(3333);
            //创建一个客户端对象，这里的作用是用作多线程，必经服务器服务的不是一个客户端
            Socket client = null;

            Socket remoteServerSocket = new Socket("172.247.33.169", 54400);

            boolean flag = true;

            while (flag) {
                System.out.println("服务器已启动，等待客户端请求。。。。");
                //accept是阻塞式方法，对新手来说这里很有可能出错，下面的注意事项我会说到
                client = serverSocket.accept();


                //创建一个线程，每个客户端对应一个线程
                new Thread(new EchoThread(client, remoteServerSocket)).start();
            }
            client.close();
            serverSocket.close();
            System.out.println("服务器已关闭。");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

class EchoThread implements Runnable {
    private Socket client;

    private Socket remote;

    public EchoThread(Socket client, Socket remote) {
        this.client = client;
        this.remote = remote;

    }

    public void run() {
        try {
            String br = null;
            boolean flag = true;
            while (flag == true) {
                InputStream in = client.getInputStream();
                OutputStream out = remote.getOutputStream();
                //读入数据
                byte[] data = new byte[1024];
                int readlen = in.read(data);

                //如果没有数据，则暂停
                if (readlen <= 0) {
                    Thread.sleep(300);
                    continue;
                }
                out.write(data, 0, readlen);
                out.flush();
            }

        } catch (IOException e1) {
            // TODO 自动生成的 catch 块
            e1.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error");
        }


    }
//
//    public void recordMsg(String br) throws IOException {
//        File file = new File("test.data");
//        if (!file.exists()) {
//            file.createNewFile();
//        }
//        FileWriter writer = new FileWriter(file, true);
//        writer.write(br + "\r\n");
//        writer.close();
//
//    }

}

