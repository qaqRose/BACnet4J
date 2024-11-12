package com.serotonin.bacnet4j.socket;

import com.serotonin.bacnet4j.npdu.ip.InetAddrCache;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class DatagramSocketExample {
    public static void main(String[] args) {
        try {
            // 创建一个不绑定特定端口的DatagramSocket
            InetSocketAddress localBindAddress = InetAddrCache.get("192.168.0.150", 4444);
            DatagramSocket socket = new DatagramSocket(null);
            DatagramSocket socket2 = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket2.setReuseAddress(true);

            socket.bind(localBindAddress);
            socket2.bind(localBindAddress);

            // 打印分配的端口号
            System.out.println("DatagramSocket绑定到端口: " + socket.getLocalPort());
            System.out.println("DatagramSocket绑定到端口 2: " + socket2.getLocalPort());

            // 使用socket进行其他操作

            // 关闭socket
            socket.close();
            socket2.close();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
