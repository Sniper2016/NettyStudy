package cn.gameboys.nio;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSendTest {

	public static void main(String[] args) throws Exception {
		// 创建发送端Socket对象
		DatagramSocket ds = new DatagramSocket();
		// 创建数据并打包
		String s = "welcome to www.gameboys.cn";
		byte[] bytes = s.getBytes();
		int length = s.length();
		InetAddress ip = InetAddress.getByName("127.0.0.1");// 根据自己主机的ip地址或者主机名
		int port = 10086;
		DatagramPacket dp = new DatagramPacket(bytes, length, ip, port);
		// 发送数据
		ds.send(dp);
		// 释放资源
		ds.close();
	}

}
