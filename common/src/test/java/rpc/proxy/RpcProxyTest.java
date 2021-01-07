package rpc.proxy;

import org.junit.Test;

public class RpcProxyTest {

    @Test
    public void createProxy() throws Exception {
        SampleService sampleService = (SampleService) RpcProxy.createProxy(SampleService.class);
        sampleService.foo(System.currentTimeMillis() + "");
    }
}
