package cn.gameboys.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {

	public static void main(String[] args) throws IOException {

		RandomAccessFile inFile = new RandomAccessFile("D:\\a.txt", "rw");
		RandomAccessFile outFile = new RandomAccessFile("D:\\b.txt", "rw");

		FileChannel inChannel = inFile.getChannel();
		FileChannel outChannel = outFile.getChannel();

		//从文件channel读取1024长度的数据
		ByteBuffer inBuf = ByteBuffer.allocate(1024);
		inChannel.read(inBuf);
		System.out.println(new String(inBuf.array()));

		
		//将一个1024长度的byteBuffer写入文件中
		ByteBuffer buf = ByteBuffer.allocate(1024);
		String newData = "New String to write to file..." + System.currentTimeMillis();
		buf.clear();
		buf.put(newData.getBytes());
		buf.flip();
		while (buf.hasRemaining()) {
			outChannel.write(buf);
		}
		
		//关掉流
		inFile.close();
		outFile.close();
		inChannel.close();
		outChannel.close();

	}

}
