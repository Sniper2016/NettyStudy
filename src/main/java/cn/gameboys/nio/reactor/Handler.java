package cn.gameboys.nio.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

class Handler implements Runnable {
	final SocketChannel channel;
	final SelectionKey sk;
	ByteBuffer input = ByteBuffer.allocate(1024 * 10);
	ByteBuffer output = ByteBuffer.allocate(1024 * 10);
	static final int READING = 0, SENDING = 1;
	int state = READING;

	Handler(Selector selector, SocketChannel c) throws IOException {
		channel = c;
		c.configureBlocking(false);
		// Optionally try first read now
		sk = channel.register(selector, 0);

		// 将Handler作为callback对象
		sk.attach(this);

		// 第二步,注册Read就绪事件
		sk.interestOps(SelectionKey.OP_READ);
		selector.wakeup();
	}

	boolean inputIsComplete() {
		/* ... */
		return true;
	}

	boolean outputIsComplete() {

		/* ... */
		return true;
	}

	void process() {
		/* ... */
		
		//这里打印一下收到的消息
		
		System.out.println("收到消息："+new String(input.array()));
		
		input.clear();
		
		
		try {
			channel.write(ByteBuffer.wrap(new String("现在演示的是单线程reactor模式").getBytes()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
		
		return;
	}

	public void run() {
		try {
			if (state == READING) {
				read();
			} else if (state == SENDING) {
				send();
			}
		} catch (IOException ex) { /* ... */
		}
	}

	void read() throws IOException {
		channel.read(input);
		
		System.out.println(Thread.currentThread().getName()+" read");
		
		if (inputIsComplete()) {

			process();

			state = SENDING;
			// Normally also do first write now

			// 第三步,接收write就绪事件
			sk.interestOps(SelectionKey.OP_WRITE);
		}
	}

	void send() throws IOException {
		channel.write(output);

		
		System.out.println("发送消息："+new String(output.array()));
		
		System.out.println(Thread.currentThread().getName()+" read");
		
		
		// write完就结束了, 关闭select key
		if (outputIsComplete()) {
			sk.cancel();
		}
	}
}
