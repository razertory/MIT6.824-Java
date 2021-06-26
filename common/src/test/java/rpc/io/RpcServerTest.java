package rpc.io;

import org.junit.Assert;
import org.junit.Test;

public class RpcServerTest {

    private int port = 10001;
    private int port1 = 10002;

    @Test
    public void testCallMethod() throws Exception {
        new SampleServer().serve(port);
        new SampleServer().serve(port1);
        Object r = new RpcClient()
            .call(port, "foo", new Object[]{"1"});
        Assert.assertEquals(new SampleServer().foo("1"), r.toString());
        Object r1 = new RpcClient()
            .call(port1, "foo", new Object[]{"1"});
        Assert.assertEquals(new SampleServer().foo("1"), r1.toString());
    }


}