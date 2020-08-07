package cn.gameboys.nio.reactor;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {

		Reactor reactor = new Reactor(6666);
		reactor.run();

		
		//客户端自己telnet过来发消息。
		
		
	}

}
