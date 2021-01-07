/*
 * NettyServer.java

 */

package rpc.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import rpc.common.RpcDecoder;
import rpc.common.RpcEncoder.JSONRpcSerializer;
import rpc.common.RpcRequest;
import rpc.common.RpcResponse;

/**
 * @author razertory
 * @date 2021/1/6
 */
public class NettyServer {

    public void init(String host, int port) throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                    ChannelPipeline pipeline = nioSocketChannel.pipeline();
                    pipeline.addFirst(new StringEncoder());
                    pipeline.addLast(new RpcDecoder(RpcRequest.class, new JSONRpcSerializer()));
                    pipeline.addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object o)
                            throws Exception {
                            ctx.writeAndFlush("foo");
                        }
                    });
                }
            });
        serverBootstrap.bind(port).sync();
        System.out.println("started server on port: " + port);
    }

    private RpcResponse invoke(Object o) {
        RpcResponse rpcResponse = new RpcResponse();
        if (o instanceof RpcRequest) {
            rpcResponse.setRequestId(((RpcRequest) o).getRequestId());
        }
        return rpcResponse;
    }

    public static void main(String[] args) throws Exception {
        new NettyServer().init("", new Integer(args[0]));
    }
}
