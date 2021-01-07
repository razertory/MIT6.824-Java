/*
 * SampleServiceImpl.java

 */

package rpc.proxy;

import rpc.io.RpcServer;

/**
 * @author razertory
 * @date 2021/1/6
 */
public class SampleServiceImpl extends RpcServer implements SampleService {

    public String foo(String greet) {
        return "foo" + greet;
    }
}
