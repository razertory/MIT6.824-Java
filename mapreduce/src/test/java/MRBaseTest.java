import com.alibaba.fastjson.JSON;
import common.KeyValue;
import common.Util;
import func.MapFunc;
import func.ReduceFunc;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MRBaseTest {

    private String tempFile = "src/main/resources/temp/mr-iterm-0-0", reduceFile = "src/main/resources/result/mr-reduce-test.txt";

    @Before
    public void clean() {
        Util.delete(tempFile);
        Util.delete(reduceFile);
    }

    @After
    public void after() {
        Util.delete(tempFile);
        Util.delete(reduceFile);
    }

    @Test
    public void doMap() {
        testDoMap();
        testDoReduce();
    }

    private void testDoMap() {
        CommonMap commonMap = new CommonMap();
        MapFunc mapFunc = (file, cnt) -> Arrays.asList(new KeyValue("1", "1"));
        commonMap.doMap(mapFunc, 0, "src/main/resources/articles/pg-being_ernest.txt", 1);
        String ret = Util.readFile(tempFile);
        KeyValue exp = new KeyValue("1", "1");
        Assert.assertEquals(exp, JSON.parseObject(ret, KeyValue.class));
    }

    private void testDoReduce() {
        CommonReduce commonReduce = new CommonReduce();
        ReduceFunc reduceFunc = (String key, List<String> values) -> "1";
        commonReduce.doReduce(reduceFunc, 0, 1, reduceFile);
        String act = Util.readFile(reduceFile), expect = "1";
        Assert.assertEquals(expect, act);
    }
}