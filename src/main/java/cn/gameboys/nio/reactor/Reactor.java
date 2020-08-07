package cn.gameboys.nio.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

class Reactor implements Runnable {
	final Selector selector;
	final ServerSocketChannel serverSocket;

	Reactor(int port) throws IOException { // Reactor初始化
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));
		// 非阻塞
		serverSocket.configureBlocking(false);

		// 分步处理,第一步,接收accept事件
		SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		// attach callback object, Acceptor
		sk.attach(new Acceptor());
	}

	public void run() {
		try {
			while (!Thread.interrupted()) {
				selector.select();
				
				Set selected = selector.selectedKeys();
				Iterator it = selected.iterator();

				System.out.println(Thread.currentThread().getName() + " select");

				while (it.hasNext()) {
					// Reactor负责dispatch收到的事件
					dispatch((SelectionKey) (it.next()));
				}
				selected.clear();
			}
		} catch (IOException ex) { /* ... */
		}
	}

	void dispatch(SelectionKey k) {
		Runnable r = (Runnable) (k.attachment());
		// 调用之前注册的callback对象
		if (r != null) {
			r.run();
		}
	}

	// inner class
	class Acceptor implements Runnable {
		public void run() {
			try {
				SocketChannel channel = serverSocket.accept();
				if (channel != null) {
					// 逻辑全在reactor线程
					 new Handler(selector, channel);

					// 这里将逻辑抛出去，reactor只负责read和send。
					//new MthreadHandler(selector, channel);
					System.out.println("有新连接来了，这里创建handler处理");

				}

			} catch (IOException ex) { /* ... */
			}
		}
	}
}
