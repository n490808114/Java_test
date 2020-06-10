package com.elastic_demo.alpha.util;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class ClientTest {
    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            new Thread(ClientTest::clientHandler).start();
        }
    }
    public static void clientHandler(){
        try {
            long start = System.currentTimeMillis();
            Socket socket = new Socket();
            socket.setSoTimeout(10000);
            socket.connect(new InetSocketAddress(9000));
            OutputStream outputStream = socket.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write("hello,I`m client <" + socket.getLocalSocketAddress()+ ">\r\n");
            bufferedWriter.flush();

            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            System.out.printf("接到服务器响应 %s : 处理的时间是%s\r\n",reader.readLine(),System.currentTimeMillis()-start);
            reader.close();
            inputStream.close();

            bufferedWriter.close();
            outputStream.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
