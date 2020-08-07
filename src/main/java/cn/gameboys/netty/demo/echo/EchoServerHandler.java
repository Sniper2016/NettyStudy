package cn.gameboys.netty.demo.echo;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 处理服务端 channel.
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { 
    	System.out.println(ctx.channel().remoteAddress()+"->Server :"+ msg.toString());
        ctx.write(msg); // (1)


        
        ChannelFuture future= ctx.channel().write(msg);
        future.addListener(new GenericFutureListener<Future<? super Void>>() {
			@Override
			public void operationComplete(Future<? super Void> future) throws Exception {
				System.out.println("asdf");
			}
		});
        
        
        ctx.flush(); // (2)
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { 
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}