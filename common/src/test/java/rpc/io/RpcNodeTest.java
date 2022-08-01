package rpc.io;

import com.alibaba.fastjson.JSON;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class RpcNodeTest {

    @Test
    public void testCallMethod() throws Exception {
        SampleServer sampleServer1 = new SampleServer();
        SampleServer sampleServer2 = new SampleServer();
        sampleServer1.serve();
        sampleServer2.serve();
        Object r = sampleServer1
            .call(sampleServer2.getPort(), "foo", new Object[]{"1"});
        Assert.assertEquals(sampleServer2.foo("1"), JSON.parseObject(r.toString(), Map.class));
        sampleServer2.shutDown("xxxx");
        r = sampleServer1
            .call(sampleServer2.getPort(), "foo", new Object[]{"1"});
        Assert.assertEquals(null, JSON.parseObject(r.toString(), Map.class));
    }


}