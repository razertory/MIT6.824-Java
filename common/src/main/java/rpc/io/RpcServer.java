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
import java.lang.reflect.Method;
import rpc.common.RpcDecoder;
import rpc.common.RpcEncoder.JSONRpcSerializer;
import rpc.common.RpcRequest;
import util.LogUtil;

public class RpcServer {

    /**
     * 启动这个 RPC 的网络服务
     * @throws Exception
     */
    public void serve(int port) throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                ChannelPipeline pipeline = nioSocketChannel.pipeline();
                pipeline.addFirst(new StringEncoder());
                pipeline.addLast(new RpcDecoder(RpcRequest.class, new JSONRpcSerializer()));
                pipeline.addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object o)
                        throws Exception {
                        ctx.writeAndFlush(invoke(o));
                    }
                });
            }
        });
        serverBootstrap.bind(port).sync();
        LogUtil.log("server started on port: " + port);
    }

    private Object invoke(Object o) throws Exception {
        if (!(o instanceof RpcRequest)) {
            throw new Exception("dam it!!!!");
        }
        RpcRequest rpcRequest = (RpcRequest) o;
        Object serverObj = this;
        Class<?> serverClass = serverObj.getClass();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Method method = serverClass.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        Object[] parameters = rpcRequest.getParameters();
        Object ret = method.invoke(serverObj, parameters);
        return ret;
    }
}
