package cn.gameboys.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBffferTest {

	public static void main(String[] args) {
		byteBufTest();
	}

	public static void ioTest() throws IOException {
		String srcString = "";
		String destString = "";
		FileInputStream fis = new FileInputStream(srcString);
		FileOutputStream fos = new FileOutputStream(destString);
		byte[] bys = new byte[1024];
		int len = 0;
		while ((len = fis.read(bys)) != -1) {
			fos.write(bys, 0, len);
		}
		fos.close();
		fis.close();
	}

	public static void bufferTest() throws IOException {
		String srcString = "";
		String destString = "";
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcString));
		// 为什么不传递一个具体的文件或者文件路径，而是传递一个OutputStream对象?
		// 因为字节缓冲区流仅仅提供缓冲区，为高效而设计的。真正的读写操作还是基本的流对象实现。
		// 构造方法可以指定缓冲区的大小，但是我们一般不用，默认缓冲区大小就够了
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destString));
		byte[] bys = new byte[1024];
		int len = 0;
		while ((len = bis.read(bys)) != -1) {
			bos.write(bys, 0, len);
		}
		bos.close();
		bis.close();
	}

	public static void byteBufferTest() {

		ByteBuffer byteBuffer = ByteBuffer.allocate(88);
		String value = "gameboys";
		byteBuffer.put(value.getBytes());
		byteBuffer.flip();
		byte[] valueArray = new byte[byteBuffer.remaining()];
		byteBuffer.get(valueArray);
		String decodeVaule = new String(valueArray);
		System.out.println(decodeVaule);
		
	}
	
	

	public static void byteBufTest() {
		
		ByteBuf bf= Unpooled.buffer(10,100);
		bf.writeBoolean(true);
		bf.writeInt(666);
		bf.writeInt(777);
		bf.writeInt(888);
		System.out.println(bf.readBoolean());
		System.out.println(bf.readInt());
		System.out.println(bf.readInt());
		System.out.println(bf);	
	}
	
	
	
	

}
