/*
 * Server.java
 * Copyright 2021 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package rpc;

/**
 * @author gusu
 * @date 2021/1/1
 */
public class Server {
    private String id;

    public Server() {
        this.id = "uuid";
        BeanFactory.putBean(this.getClass().getName(), this);
    }
}
