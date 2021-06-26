/*
 * SampleServer.java
 * Copyright 2021 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package rpc.io;

/**
 * @author gusu
 * @date 2021/6/26
 */
public class SampleServer extends RpcServer{

    public String foo(String s) {
        return "foo" + s;
    }
}
