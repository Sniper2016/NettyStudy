package cn.gameboys.nio;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPReceiveTest {

	public static void main(String[] args) throws Exception {
		// 创建接收端Socket对象
		DatagramSocket ds = new DatagramSocket(10086);
		// 接收数据
		byte[] bytes = new byte[1024];
		int length = bytes.length;
		DatagramPacket dp = new DatagramPacket(bytes, length);
		ds.receive(dp);
		// 解析数据
		InetAddress address = dp.getAddress();
		byte[] data = dp.getData();
		int len = dp.getLength();
		// 输出数据
		System.out.println(address);
		System.out.println(new String(data, 0, len));

	}
}
