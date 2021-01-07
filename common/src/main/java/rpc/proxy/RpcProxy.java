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
import rpc.io.JSONSerializer;
import rpc.io.RpcEncoder;
import rpc.io.RpcRequest;
import rpc.io.UserClientHandler;

/**
 * @author razertory
 * @date 2021/1/6
 */
public class RpcProxy {

    //1.创建一个线程池对象  -- 它要处理我们自定义事件
    private static ExecutorService executorService = Executors
        .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //2.声明一个自定义事件处理器  UserClientHandler
    private static UserClientHandler userClientHandler;

    public static Object createProxy(final Class<?> serviceClass) {

        return Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(),
            new Class[]{serviceClass},
            new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    if (userClientHandler == null) {
                        initClient();
                    }

                    //初始化一一个请求
                    RpcRequest request = new RpcRequest();
                    //填充请求 id
                    request.setRequestId(UUID.randomUUID().toString());

                    //填充类名
                    request.setClassName(serviceClass.getSimpleName());
                    //获取方法名
                    request.setMethodName(method.getName());
                    //填充参数类型
                    request.setParameterTypes(method.getParameterTypes());
                    //填充参数
                    request.setParameters(args);

                    //2)给 UserClientHandler 设置 param 参数
                    userClientHandler.setRequest(request);
                    //3).使用线程池,开启一个线程处理处理 call() 写操作,并返回结果
                    Object result = executorService.submit(userClientHandler).get();
                    //4)return 结果
                    return result;
                }
            });
    }

    static void initClient() throws Exception {
        //1) 初始化 UserClientHandler
        userClientHandler = new UserClientHandler();

        //2)创建连接池对象
        NioEventLoopGroup group = new NioEventLoopGroup();
        //3)创建客户端的引导对象
        Bootstrap bootstrap = new Bootstrap();
        //4)配置启动引导对象
        bootstrap.group(group)
            //设置通道为 NIO
            .channel(NioSocketChannel.class)
            //设置请求协议为 TCP
            .option(ChannelOption.TCP_NODELAY, true)
            //监听 channel 并初始化
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //获取 ChannelPipeline
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    //设置编码
                    pipeline.addFirst(new RpcEncoder(RpcRequest.class,new JSONSerializer()));
                    pipeline.addLast(new StringDecoder());
                    //添加自定义事件处理器
                    pipeline.addLast(userClientHandler);
                }
            });

        bootstrap.connect("127.0.0.1", 10001).sync();
    }
}
