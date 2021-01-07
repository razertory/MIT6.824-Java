/*
 * RpcProxy.java

 */

package rpc.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import rpc.common.RpcEncoder;
import rpc.common.RpcRequest;
import rpc.common.RpcEncoder.JSONRpcSerializer;
import rpc.io.NettyClient;

/**
 * @author razertory
 * @date 2021/1/6
 */
public class RpcProxy {

    private ExecutorService executorService = Executors
        .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private NettyClient nettyClient;

    public Object createProxy(final Class<?> serviceClass, String serverHost, int serverPort) {
        nettyClient = new NettyClient(serverHost, serverPort);
        return Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(),

            new Class[]{serviceClass},

            new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    RpcRequest request = new RpcRequest();
                    request.setRequestId(UUID.randomUUID().toString());
                    request.setClassName(serviceClass.getSimpleName());
                    request.setMethodName(method.getName());
                    request.setParameterTypes(method.getParameterTypes());
                    request.setParameters(args);
                    nettyClient.setRequest(request);
                    bind();
                    Object result = executorService.submit(nettyClient).get();
                    return result;
                }
            });
    }

    private void bind() throws Exception{
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addFirst(new RpcEncoder(RpcRequest.class, new JSONRpcSerializer()));
                    pipeline.addLast(new StringDecoder());
                    pipeline.addLast(nettyClient);
                }
            });
        bootstrap.connect(nettyClient.getServerHost(), nettyClient.getServerPort()).sync();
    }
}
