/*
 * NettyServer.java

 */

package rpc.io;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author razertory
 * @date 2021/1/6
 */
public class NettyServer {

    public void init() throws Exception {

        //1.创建 2 个线程池对象
        //负责接收用户连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //负责处理用户的 io 读写操作
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        //创建服务端端启动引导类。
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //配置启动引导类
        serverBootstrap.group(bossGroup, workGroup)
            //设置通道为 nio
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                //事件监听 Channel 通道
                @Override
                protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                    //获取 pipeline
                    ChannelPipeline pipeline = nioSocketChannel.pipeline();
                    //绑定编码
                    pipeline.addFirst(new StringEncoder());
                    //设置解码器为 JSON 对象
                    pipeline.addLast(new RpcDecoder(RpcRequest.class, new JSONSerializer()));

                    //绑定我们的业务逻辑
                    pipeline.addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object o)
                            throws Exception {
                            //获取入栈信息,打印客户端传递的数据
                            System.out.println(o);
                            ctx.writeAndFlush("foo");
                        }
                    });
                }
            });
        serverBootstrap.bind(10001).sync();
    }

    private Object invoke(String s) {
        System.out.println(s);
        RpcRequest rpcRequest = JSON.parseObject(s, RpcRequest.class);
        System.out.println(rpcRequest);
        return rpcRequest;
    }

    public static void main(String[] args) throws Exception {
        new NettyServer().init();
        System.out.println("started server");
    }
}
