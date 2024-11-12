package com.serotonin.bacnet4j.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author qxq
 * @date 2024/8/1
 */
public class UdpServer {


    public static void main(String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket();
        String message = "Hello from UDP sender!";
        byte[] buffer = message.getBytes();
        // 地址
        InetAddress address = InetAddress.getByName("localhost");
        // 发送报文
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 9876);
        socket.send(packet);
        socket.close();

    }

}
