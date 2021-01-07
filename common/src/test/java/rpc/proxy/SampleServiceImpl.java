/*
 * SampleServiceImpl.java
 * Copyright 2021 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package rpc.proxy;

/**
 * @author gusu
 * @date 2021/1/6
 */
public class SampleServiceImpl implements SampleService{

    public String foo(String greet) {
        return "foo" + greet;
    }
}
