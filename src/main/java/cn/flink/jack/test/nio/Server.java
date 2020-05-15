package cn.flink.jack.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        // 绑定端口
        ssc.bind(new InetSocketAddress("localhost",8090));
        SocketChannel sc = ssc.accept();
        System.out.println("finished");

        //判断接收到连接
        while (sc == null) {
            sc=ssc.accept();
        }

        //读取数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        sc.read(buffer);
        System.out.println(new String(buffer.array(),0,buffer.position()));

        // 服务器给客户端响应
        sc.write(ByteBuffer.wrap("accepted".getBytes()));

        ssc.close();

    }
}
