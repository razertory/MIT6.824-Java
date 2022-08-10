/*
 * NettyServer.java

 */

package rpc.io;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import rpc.common.RpcDecoder;
import rpc.common.RpcEncoder;
import rpc.common.RpcEncoder.JSONRpcSerializer;
import rpc.common.RpcRequest;
import util.LogUtil;

public class RpcNode {

    private final ExecutorService executorService = Executors
        .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private Integer port;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workGroup;

    public RpcNode() {
        this.port = randPort();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * 启动这个 RPC 的网络服务
     *
     * @throws Exception
     */
    public void serve() throws Exception {
        this.bossGroup = new NioEventLoopGroup();
        this.workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(this.bossGroup, this.workGroup);
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
        LogUtil.log("server started on port:", port);
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
        return JSON.toJSONString(ret);
    }

    public Object call(int port, String methodName, Object[] args) {
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setMethodName(methodName);
        request.setParameters(args);
        Class<?>[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        request.setParameterTypes(parameterTypes);
        RpcChannel rpcChannel = new RpcChannel(port);
        rpcChannel.setRequest(request);
        try {
            bind(rpcChannel);
            Object ret = executorService.submit(rpcChannel).get();
            LogUtil.log("method: ", request.getMethodName(), "params: " + request.getParameters()
                , " resp: ", ret);

            return ret;
        } catch (Exception e) {
            LogUtil.log("fail to call err, " + e);
            e.printStackTrace();
            return null;
        }
    }

    private void bind(RpcChannel rpcChannel) throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addFirst(new RpcEncoder(RpcRequest.class, new JSONRpcSerializer()));
                    pipeline.addLast(new StringDecoder());
                    pipeline.addLast(rpcChannel);
                }
            });
        bootstrap.connect("127.0.0.1", rpcChannel.getServerPort()).sync();
    }

    private int randPort() {
        int start = 11000, end = 13000;
        return (int) ((Math.random() * (end - start)) + start);
    }

    protected void shutDownServer() {
        this.bossGroup.shutdownGracefully();
        this.workGroup.shutdownGracefully();
    }

}
