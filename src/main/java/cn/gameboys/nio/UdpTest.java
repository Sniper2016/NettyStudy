package cn.gameboys.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UdpTest {

	public static void main(String[] args) throws IOException {

		//获得一个channel
		DatagramChannel channel = DatagramChannel.open();
		
		//发送消息
		String newData = "hi gameboys.cn ..." + System.currentTimeMillis();
		ByteBuffer buf = ByteBuffer.allocate(48);
		buf.clear();
		buf.put(newData.getBytes());
		buf.flip();
		channel.connect(new InetSocketAddress("127.0.0.1", 10086));
		channel.write(buf);
		
		

		
	}
}
