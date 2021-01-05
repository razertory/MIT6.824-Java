package rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Rpc 服务
 * @author razertory
 * @date 2020/12/31
 */
public class RpcServer {
    public ServerBootstrap serverBootstrap(String serverId) throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        serverBootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new RpcServerInitializer(RpcReq.class, RpcResp.class));
        return serverBootstrap;
    }
}
