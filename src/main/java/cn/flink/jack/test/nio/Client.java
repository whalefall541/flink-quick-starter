package cn.flink.jack.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) throws Exception {
        // 开启客户端通道
        SocketChannel sc = SocketChannel.open();
        //设置为NIO模式
        sc.configureBlocking(false);
        sc.connect(new InetSocketAddress("localhost",8090));
        System.out.println("connected");

        //因为非阻塞 需判断是否连接 多次没连接上则放弃连接
        while (!sc.isConnected()) {
            sc.finishConnect();
        }
        // 发送数据
        ByteBuffer buffer = ByteBuffer.wrap("hello server".getBytes());
        sc.write(buffer);

        //Thread.sleep(1000);

        // 接收服务器数据
        ByteBuffer dst = ByteBuffer.allocate(1024);
        sc.read(dst);
        System.out.println(new String(dst.array(),0,buffer.position()));

        sc.close();
    }
}
