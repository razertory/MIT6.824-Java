/*
 * RpcServerHandler.java
 * Copyright 2021 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.Method;

/**
 * @author gusu
 * @date 2021/1/1
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcReq> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcReq req)
        throws Exception {
        RpcResp resp = new RpcResp();
        resp.setRpcId(req.getRpcId());
    }

    private Object handler(RpcReq req) throws Exception{
        String methodName = req.getMethod();
        Object object = BeanFactory.getBean(req.getClassName());
        Method method = object.getClass().getMethod(methodName);
        return method.invoke(object, req.getData());
    }
}
