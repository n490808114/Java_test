package com.learn.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class nio {
    public static void main(String[] args){
        Selector selector = initSelector();
        try {
            while (selector.select() > 0){
                Set<SelectionKey> keys = selector.keys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isAcceptable()){
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = channel.accept();
                        System.out.println("建立链接:"+accept.getRemoteAddress());
                        accept.configureBlocking(false);
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        accept.register(selector,SelectionKey.OP_READ,buffer);
                    }else if(key.isConnectable()){
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        buffer.clear();
                        StringBuilder stringBuilder = new StringBuilder();
                        int read = 0;
                        while ((read = channel.read(buffer)) > 0){
                            buffer.flip();
                            stringBuilder.append(StandardCharsets.UTF_8.newDecoder().decode(buffer));
                            buffer.clear();
                        }
                        System.out.printf("收到 %s 发来的: %s \n",channel.getRemoteAddress(),stringBuilder);
                        buffer.clear();
                        Thread.sleep((long) (Math.random()*1000));
                        buffer.put(("收到,你发来的是=======>" + stringBuilder + "\r\n").getBytes());
                        buffer.flip();
                        channel.write(buffer);
                        System.out.printf("回复 %s 的内容是 %s \n",channel.getRemoteAddress(),stringBuilder);
                        channel.register(selector,SelectionKey.OP_READ,buffer.clear());
                    }
                }
            }
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
    public static Selector initSelector() {
        Selector selector = null;
        try {
            ServerSocketChannel channel = ServerSocketChannel.open();

            channel.configureBlocking(false);

            channel.bind(new InetSocketAddress(9000));

            selector = Selector.open();

            channel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
        return selector;
    }
}
