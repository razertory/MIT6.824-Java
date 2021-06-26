/*
 * UserClientHandler.java

 */

package rpc.io;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.concurrent.Callable;
import lombok.Data;
import rpc.common.RpcRequest;

/**
 * @author razertory
 * @date 2021/1/6
 */
@Data
public class RpcChannel extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    private String result;
    private RpcRequest request;
    private int serverPort;

    public RpcChannel(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        notify();
    }

    public synchronized Object call() throws Exception {
        context.writeAndFlush(request);
        wait();
        return result;
    }

    public void setRequest(RpcRequest request) {
        this.request = request;
    }
}
