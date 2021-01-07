/*
 * UserClientHandler.java

 */

package rpc.io;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.concurrent.Callable;

/**
 * @author razertory
 * @date 2021/1/6
 */
public class UserClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    //事件处理器上下文对象 (存储 handler 信息,写操作)
    private ChannelHandlerContext context;
    // 记录服务器返回的数据
    private String result;
    //记录将要返送给服务器的数据
    private RpcRequest request;


    //2.实现 channelActive  客户端和服务器连接时,该方法就自动执行
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    //3.实现 channelRead 当我们读到服务器数据,该方法自动执行
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        notify();
    }


    //4.将客户端的数写到服务器
    public synchronized Object call() throws Exception {
        context.writeAndFlush(request);
        wait();
        return result;
    }

    //5.设置参数的方法
    public void setRequest(RpcRequest request) {
        this.request = request;
    }
}
