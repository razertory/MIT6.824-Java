/*
 * RpcChannelInitializer.java
 * Copyright 2021 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package rpc;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author gusu
 * @date 2021/1/1
 */
public class RpcServerInitializer extends ChannelInitializer<SocketChannel> {

    private Class<?> reqClass;
    private Class<?> respClass;

    RpcServerInitializer(Class<?> reqClass, Class<?> respClass) {
        this.reqClass = reqClass;
        this.respClass = respClass;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,4));
        pipeline.addLast(new Encoder(reqClass));
        pipeline.addLast(new Decoder(respClass));
        pipeline.addLast(new RpcServerHandler());
    }
}
