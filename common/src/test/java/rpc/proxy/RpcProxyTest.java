package rpc.proxy;

import org.junit.Assert;
import org.junit.Test;
import rpc.io.NettyServer;

public class RpcProxyTest {
    private String host = "127.0.0.1";
    private int port = 10001;

    @Test
    public void createProxy() throws Exception {
        new NettyServer().init(host, port);
        SampleService sampleService = (SampleService) new RpcProxy().createProxy(SampleService.class, host, port);
        Assert.assertEquals(new SampleServiceImpl().foo(""), sampleService.foo(""));
    }
}
