package cn.gameboys.nio.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MthreadHandler implements Runnable {
	final SocketChannel channel;
	final SelectionKey selectionKey;
	ByteBuffer input = ByteBuffer.allocate(1024 * 10);
	ByteBuffer output = ByteBuffer.allocate(1024 * 10);
	static final int READING = 0, SENDING = 1;
	int state = READING;

	ExecutorService pool = Executors.newFixedThreadPool(2);
	static final int PROCESSING = 3;

	MthreadHandler(Selector selector, SocketChannel c) throws IOException {
		channel = c;
		c.configureBlocking(false);
		// Optionally try first read now
		selectionKey = channel.register(selector, 0);

		// 将Handler作为callback对象
		selectionKey.attach(this);

		// 第二步,注册Read就绪事件
		selectionKey.interestOps(SelectionKey.OP_READ);
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

		System.out.println(Thread.currentThread().getName()+" 处理收到消息2："+new String(input.array()));	
		
		input.clear();
		
		
		
		//这里模拟添加数据，发送
		System.out.println(Thread.currentThread().getName()+"给发送缓存添加数据.");
		
	
		output.put(ByteBuffer.wrap(new String("现在演示的是单线程reactor模式").getBytes()));

		

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

	synchronized void read() throws IOException {
		// ...
		channel.read(input);
		
		System.out.println(Thread.currentThread().getName()+" read");
		
		if (inputIsComplete()) {
			state = PROCESSING;
			// 使用线程pool异步执行
			pool.execute(new Processer());
		}
	}

	

	void send() throws IOException {
			
		//切换成读模式。
		output.flip();
		
		channel.write(output);
		
		output.clear();
		
		
		
		System.out.println(Thread.currentThread().getName()+" send");
		
		// write完就结束了, 关闭select key
		if (outputIsComplete()) {
			selectionKey.cancel();
			state = READING;
		}
	}

	synchronized void processAndHandOff() {
		process();
		state = SENDING;
		// or rebind attachment
		// process完,开始等待write就绪,这里虽然注册到selector了，但是send不会立刻被调用，读者可以自己试验一下，直到下一次select有请求才会调用send。
		selectionKey.interestOps(SelectionKey.OP_WRITE);
		System.out.println("设置监听可以处理写了。");
	}

	class Processer implements Runnable {
		public void run() {
			processAndHandOff();
		}
	}

}
