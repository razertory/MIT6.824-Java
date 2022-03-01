/*
 * RpcClient.java
 * Copyright 2021 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package rpc.io;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import rpc.common.RpcEncoder;
import rpc.common.RpcEncoder.JSONRpcSerializer;
import rpc.common.RpcRequest;
import util.LogUtil;

public class RpcClient {

    private ExecutorService executorService = Executors
        .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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
            LogUtil.log("id: " + request.getRequestId() + " resp: " + ret + " req: " + request);
            return ret;
        } catch (Exception e) {
            LogUtil.log("fail to call ");
        }
        return null;
    }

    private void bind(RpcChannel rpcChannel) throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
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
}
